package com.e_commere.e_commerece_app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e_commere.e_commerece_app.dto.AddToCartRequestDto;
import com.e_commere.e_commerece_app.dto.CartResponseDto;
import com.e_commere.e_commerece_app.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartResponseDto> addToCart(@RequestBody @Valid AddToCartRequestDto request, Principal principal) {
        CartResponseDto response = cartService.addToCart(request,principal.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<CartResponseDto> getCart(Principal principal) {
        CartResponseDto response = cartService.getCart(principal.getName());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartResponseDto> removeItemFromCart(Principal principal, @PathVariable Long productId) {
        CartResponseDto response = cartService.removeItemFromCart(principal.getName(), productId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping()
    public ResponseEntity<String> clearCart(Principal principal) {
        cartService.clearCart(principal.getName());
        return ResponseEntity.ok("Cart cleared");
    }
}
