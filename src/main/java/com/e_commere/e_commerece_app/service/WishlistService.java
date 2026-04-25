package com.e_commere.e_commerece_app.service;


import com.e_commere.e_commerece_app.dto.ProductResponseDto;
import com.e_commere.e_commerece_app.dto.WishlistItemResponseDto;
import com.e_commere.e_commerece_app.dto.WishlistResponseDto;
import com.e_commere.e_commerece_app.entity.ProductEntity;
import com.e_commere.e_commerece_app.entity.UserEntity;
import com.e_commere.e_commerece_app.entity.WishlistEntity;
import com.e_commere.e_commerece_app.entity.WishlistItemEntity;
import com.e_commere.e_commerece_app.repository.ProductRepository;
import com.e_commere.e_commerece_app.repository.UserRepository;
import com.e_commere.e_commerece_app.repository.WishlistItemRepository;
import com.e_commere.e_commerece_app.repository.WishlistRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final WishlistRepository wishlistRepo;
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final WishlistItemRepository wishlistItemRepo;
    @Transactional
    public WishlistResponseDto getWishListByUserId(String email){
        UserEntity user=userRepo.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("User with email " + email + " not found"));
        WishlistEntity wishlist=wishlistRepo.findByUserId(user.getId()).orElseThrow(()-> new EntityNotFoundException("Wishlist for user with email " + email + " not found"));
        return mapToWishlistResponseDto(wishlist);
    }

    @Transactional
    public void addProductToWishlist(String email, Long productId) {
        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        WishlistEntity wishlist = wishlistRepo.findByUserId(user.getId())
                .orElseGet(() -> createEmptyWishlist(user));

        ProductEntity product = productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        boolean alreadyExist=wishlist.getItems().stream()
                .anyMatch(item->item.getProduct().getId().equals(productId));
        if(alreadyExist){
            return;
        }

        WishlistItemEntity newItem = new WishlistItemEntity();
        newItem.setWishlist(wishlist);
        newItem.setProduct(product);
        newItem.setAddedAt(LocalDateTime.now());

        wishlist.getItems().add(newItem);
        wishlistRepo.save(wishlist);
    }

    @Transactional
    public void removeProductFromWishlist(String email, Long productId) {
        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        WishlistEntity wishlist = wishlistRepo.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found"));

        WishlistItemEntity itemToRemove = wishlist.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Product not found in wishlist"));

        wishlist.getItems().remove(itemToRemove);
        wishlistItemRepo.delete(itemToRemove);
        wishlistRepo.save(wishlist);
    }


    private WishlistResponseDto mapToWishlistResponseDto(WishlistEntity wishlist) {
        List<WishlistItemResponseDto> itemDtos = wishlist.getItems().stream()
                .map(this::mapToWishlistItemResponseDto)
                .toList();

        return new WishlistResponseDto(
                wishlist.getId(),
                wishlist.getUser().getId(),
                itemDtos
        );
    }

    private WishlistItemResponseDto mapToWishlistItemResponseDto(WishlistItemEntity item) {
        return new WishlistItemResponseDto(
                item.getId(),
                item.getWishlist().getId(),
                mapToProductResponseDto(item.getProduct()),
                item.getAddedAt()
        );
    }

    private ProductResponseDto mapToProductResponseDto(ProductEntity product) {
        Long categoryId = null;
        String categoryName = null;

        if (product.getCategory() != null) {
            categoryId = product.getCategory().getId();
            categoryName = product.getCategory().getName();
        }

        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                categoryId,
                categoryName
        );
    }
    private WishlistEntity createEmptyWishlist(UserEntity user) {
        WishlistEntity newWishlist = new WishlistEntity();
        newWishlist.setUser(user);
        newWishlist.setItems(new ArrayList<>());
        return wishlistRepo.save(newWishlist);
    }


}
