package com.e_commere.e_commerece_app.controller;

import com.e_commere.e_commerece_app.entity.OrderEntity;
import com.e_commere.e_commerece_app.repository.OrderRepository;
import com.e_commere.e_commerece_app.service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final OrderRepository orderRepo;
    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping("/checkout/{orderId}")
    public ResponseEntity<?> createCheckoutSession(@PathVariable Long orderId) {
        try {

            OrderEntity order = orderRepo.findById(orderId)
                    .orElseThrow(() -> new EntityNotFoundException("Order not found"));


            String checkoutUrl = paymentService.createCheckOutSession(order);


            return ResponseEntity.ok(Map.of("checkoutUrl", checkoutUrl));

        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Stripe payment failed to initialize: " + e.getMessage()));
        }
    }

    @GetMapping("/success")
    public ResponseEntity<String> paymentSuccess(@RequestParam("session_id") String sessionId) {
        return ResponseEntity.ok("Payment Successful! Thank you for your purchase. Session ID: " + sessionId);
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> paymentCancel() {
        return ResponseEntity.ok("Payment was cancelled. You can try again from your cart.");
    }


    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            System.out.println("⚠️ Webhook signature verification failed.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

        if ("checkout.session.completed".equals(event.getType())) {

            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);

            if (session != null) {
                System.out.println("✅ Payment Success for Session: " + session.getId());
                paymentService.fulfillOrder(session.getId());
            }
        }


        return ResponseEntity.ok("Success");
    }
}
