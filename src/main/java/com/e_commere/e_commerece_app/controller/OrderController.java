package com.e_commere.e_commerece_app.controller;

import com.e_commere.e_commerece_app.dto.CheckoutRequestDto;
import com.e_commere.e_commerece_app.dto.OrderResponseDto;
import com.e_commere.e_commerece_app.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestBody CheckoutRequestDto request, Authentication authentication) {
        String email = authentication.getName();
        orderService.createOrder(email, request.getAddressId(), request.getCouponId());
        return ResponseEntity.status(HttpStatus.CREATED).body("Order placed successfully!");
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getUserOrders(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(orderService.getUserOrders(email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(orderService.getOrderById(email, id));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long id, Authentication authentication) {
        String email = authentication.getName();
        orderService.cancelOrder(email, id);
        return ResponseEntity.ok("Order cancelled successfully");
    }


    @PutMapping("/admin/{id}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok("Order status updated to: " + status);
    }

}
