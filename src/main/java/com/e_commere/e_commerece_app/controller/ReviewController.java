package com.e_commere.e_commerece_app.controller;

import com.e_commere.e_commerece_app.dto.ReviewRequestDto;
import com.e_commere.e_commerece_app.dto.ReviewResponseDto;
import com.e_commere.e_commerece_app.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;


    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProduct(productId));
    }


    @PostMapping
    public ResponseEntity<ReviewResponseDto> addReview(Authentication authentication, @Valid @RequestBody ReviewRequestDto request) {

        ReviewResponseDto newReview = reviewService.addReview(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newReview);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id, Authentication authentication) {

        reviewService.deleteReview(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
