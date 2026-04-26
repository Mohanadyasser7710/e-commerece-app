package com.e_commere.e_commerece_app.service;

import com.e_commere.e_commerece_app.dto.ReviewRequestDto;
import com.e_commere.e_commerece_app.dto.ReviewResponseDto;
import com.e_commere.e_commerece_app.entity.ProductEntity;
import com.e_commere.e_commerece_app.entity.ReviewEntity;
import com.e_commere.e_commerece_app.entity.UserEntity;
import com.e_commere.e_commerece_app.repository.ProductRepository;
import com.e_commere.e_commerece_app.repository.ReviewRepository;
import com.e_commere.e_commerece_app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    public List<ReviewResponseDto> getReviewsByProduct(Long id){
        if(!productRepo.existsById(id)){
            throw new EntityNotFoundException("Product with id " + id + " not found");
        }
        return reviewRepo.findByProductId(id).stream().map(this::mapToResponseDto).toList();

    }

    @Transactional
    public ReviewResponseDto addReview(String email,ReviewRequestDto dto){
        UserEntity user=userRepo.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("User with email " + email + " not found"));
        ProductEntity product=productRepo.findById(dto.getProductId()).orElseThrow(()-> new EntityNotFoundException("Product not found"));
        if(reviewRepo.existsByUserIdAndProductId(user.getId(),product.getId())){
            throw new IllegalStateException("you have already reviewed this product");
        }
        ReviewEntity review=mapToEntity(dto,user,product);
        return mapToResponseDto(reviewRepo.save(review));
    }

    @Transactional
    public void deleteReview(Long id,String email){
        ReviewEntity review=reviewRepo.findById(id).orElseThrow(()-> new EntityNotFoundException("Review not found"));
        if(!review.getUser().getEmail().equals(email)){
            throw new IllegalStateException("you can only delete your own reviews");
        }

        reviewRepo.delete(review);


    }


    private ReviewEntity mapToEntity(ReviewRequestDto request, UserEntity user, ProductEntity product) {
        return ReviewEntity.builder()
                .user(user)
                .product(product)
                .rating(request.getRating())
                .comment(request.getComment())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private ReviewResponseDto mapToResponseDto(ReviewEntity review) {
        return new ReviewResponseDto(
                review.getId(),
                review.getUser().getId(),
                review.getUser().getName(),
                review.getProduct().getId(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
}
