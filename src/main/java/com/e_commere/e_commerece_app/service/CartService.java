package com.e_commere.e_commerece_app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.e_commere.e_commerece_app.dto.AddToCartRequestDto;
import com.e_commere.e_commerece_app.dto.CartItemResponseDto;
import com.e_commere.e_commerece_app.dto.CartResponseDto;
import com.e_commere.e_commerece_app.entity.CartEntity;
import com.e_commere.e_commerece_app.entity.CartItemEntity;
import com.e_commere.e_commerece_app.entity.ProductEntity;
import com.e_commere.e_commerece_app.repository.CartItemRepository;
import com.e_commere.e_commerece_app.repository.CartRepository;
import com.e_commere.e_commerece_app.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Transactional
    public CartResponseDto addToCart(AddToCartRequestDto request) {
        CartEntity cart = cartRepository.findById(request.getCartId())
                .orElseThrow(() -> new EntityNotFoundException("Cart with id " + request.getCartId() + " not found"));

        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + request.getProductId() + " not found"));

        CartItemEntity existingItem = cartItemRepository.findByCartAndProduct(cart, product).orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
        } else {
            CartItemEntity newItem = CartItemEntity.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();
            cart.getItems().add(newItem);
        }

        recalculateTotalPrice(cart);

        CartEntity savedCart = cartRepository.save(cart);

        return mapToResponse(savedCart);
    }

    @Transactional(readOnly = true)
    public CartResponseDto getCart(Long cartId) {
        CartEntity cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart with id " + cartId + " not found"));
        return mapToResponse(cart);
    }

    @Transactional
    public CartResponseDto removeItemFromCart(Long cartId, Long productId) {
        CartEntity cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart with id " + cartId + " not found"));

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product with id " + productId + " not found"));

        CartItemEntity itemToRemove = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Item not found in cart"));

        cart.getItems().remove(itemToRemove);
        recalculateTotalPrice(cart);

        CartEntity savedCart = cartRepository.save(cart);
        return mapToResponse(savedCart);
    }

    @Transactional
    public void clearCart(Long cartId) {
        CartEntity cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart with id " + cartId + " not found"));

        cart.getItems().clear();
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);
    }

    private void recalculateTotalPrice(CartEntity cart) {
        double total = cart.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice())
                .sum();
        cart.setTotalPrice(total);
    }

    private CartResponseDto mapToResponse(CartEntity cart) {
        List<CartItemResponseDto> itemDtos = cart.getItems().stream()
                .map(this::mapItemToResponse)
                .collect(Collectors.toList());

        return new CartResponseDto(
                cart.getId(),
                cart.getUser() != null ? cart.getUser().getId() : null,
                itemDtos,
                cart.getTotalPrice()
        );
    }

    private CartItemResponseDto mapItemToResponse(CartItemEntity item) {
        return new CartItemResponseDto(
                item.getId(),
                item.getCart().getId(),
                item.getProduct().getId(),
                item.getQuantity()
        );
    }
}
