package com.e_commere.e_commerece_app.controller;

import com.e_commere.e_commerece_app.dto.WishlistResponseDto;
import com.e_commere.e_commerece_app.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping
    public ResponseEntity<WishlistResponseDto> getWishlist(Authentication authentication) {
        return ResponseEntity.ok(wishlistService.getWishListByUserId(authentication.getName()));
    }

    @PostMapping("/{productId}")
    public ResponseEntity<String> addProductToWishlist(Authentication authentication, @PathVariable Long productId) {
        wishlistService.addProductToWishlist(authentication.getName(), productId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product added to wishlist successfully");
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> removeProductFromWishlist(Authentication authentication, @PathVariable Long productId) {
        wishlistService.removeProductFromWishlist(authentication.getName(), productId);
        return ResponseEntity.ok("deleted successfully");
    }
}