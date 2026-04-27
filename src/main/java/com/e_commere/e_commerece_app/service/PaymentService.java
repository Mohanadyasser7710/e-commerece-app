package com.e_commere.e_commerece_app.service;

import com.e_commere.e_commerece_app.entity.OrderEntity;
import com.e_commere.e_commerece_app.entity.PaymentEntity;
import com.e_commere.e_commerece_app.repository.OrderRepository;
import com.e_commere.e_commerece_app.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepo;
    private final OrderRepository orderRepo;
    @Value("${stripe.api.key}")
    private String stripeApiKey;
    @PostConstruct
    public void init(){
        Stripe.apiKey = stripeApiKey;
    }

    @Transactional
    public String createCheckOutSession(OrderEntity order) throws StripeException {
        long amountInPiastres=(long) (order.getTotalPrice()*100);
        SessionCreateParams params=SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8080/api/payments/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:8080/api/payments/cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("egp")
                                                .setUnitAmount(amountInPiastres)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Order #" + order.getId())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .putMetadata("order_id",order.getId().toString()).build();
        Session session = Session.create(params);


        PaymentEntity payment = PaymentEntity.builder()
                .order(order)
                .transactionId(session.getId())
                .status("PENDING")
                .amount(order.getTotalPrice())
                .paymentDate(LocalDateTime.now())
                .build();

        paymentRepo.save(payment);


        return session.getUrl();


    }

    @Transactional
    public void fulfillOrder(String sessionId) {
        PaymentEntity payment = paymentRepo.findByTransactionId(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Payment record not found for session: " + sessionId));
        payment.setStatus("SUCCESS");
        paymentRepo.save(payment);
        OrderEntity order = payment.getOrder();
        order.setStatus("PAID");
        order.getItems().forEach(item -> {
            var product = item.getProduct();
            int newStock = product.getStockQuantity() - item.getQuantity();
            product.setStockQuantity(newStock);
        });

        orderRepo.save(order);
    }
}
