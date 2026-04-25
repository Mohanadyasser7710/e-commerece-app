package com.e_commere.e_commerece_app.service;

import com.e_commere.e_commerece_app.dto.OrderItemResponseDto;
import com.e_commere.e_commerece_app.dto.OrderResponseDto;
import com.e_commere.e_commerece_app.entity.*;
import com.e_commere.e_commerece_app.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserRepository userRepo;
    private final AddressRepository addressRepo;
    private final CartRepository cartRepo;
    private final OrderRepository orderRepo;
    private final CouponRepository couponRepo;

    @Transactional
    public void createOrder(String email, Long addressId, Long couponId){
        UserEntity user=userRepo.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("User with email " + email + " not found"));
        CartEntity cart=cartRepo.findByUser(user).orElseThrow(()-> new EntityNotFoundException("Cart for user with email " + email + " not found"));
        AddressEntity address=addressRepo.findByIdAndUser(addressId, user).orElseThrow(()-> new EntityNotFoundException("Address with id " + addressId + " not found for user with email " + email));

        if(cart.getItems().isEmpty()){
            throw new IllegalStateException("Cart is empty. Cannot create order.");
        }

        OrderEntity order=new OrderEntity();
        order.setUser(user);
        order.setAddress(address);
        order.setStatus("Pending");
        double calculatedTotal=0.0;
        double discountAmount=0.0;

        for(CartItemEntity item:cart.getItems()){
            ProductEntity product=item.getProduct();
            if(product.getStockQuantity()< item.getQuantity()){
                throw new IllegalStateException("Product " + product.getName() + " is out of stock. Available quantity: " + product.getStockQuantity());
            }
            product.setStockQuantity(product.getStockQuantity()- item.getQuantity());
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(product.getPrice());

            order.getItems().add(orderItem);
            calculatedTotal += (orderItem.getPrice() * item.getQuantity());
        }

        if (couponId != null) {
            CouponEntity coupon = couponRepo.findById(couponId)
                    .orElseThrow(() -> new EntityNotFoundException("Coupon not found"));

            if (!coupon.isActive() || coupon.getExpirationDate().isBefore(LocalDate.now())) {
                throw new IllegalStateException("Coupon is inactive or has expired");
            }

            order.setCoupon(coupon);

            discountAmount = calculatedTotal * (coupon.getDiscountPercent() / 100.0);
            order.setDiscountAmount(discountAmount);
        } else {
            order.setDiscountAmount(0.0);
        }

        order.setTotalPrice(calculatedTotal-discountAmount);
        orderRepo.save(order);

        cart.getItems().clear();
        cart.setTotalPrice(0.0);
        cartRepo.save(cart);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> getUserOrders(String email) {
        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<OrderEntity> orders = orderRepo.findOrderHistoryForUser(user);

        return orders.stream().map(this::mapToOrderResponseDto).toList();
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(String email, Long orderId) {
        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        OrderEntity order = orderRepo.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("You are not authorized to view this order.");
        }

        return mapToOrderResponseDto(order);
    }

    @Transactional
    public void cancelOrder(String email, Long orderId) {
        UserEntity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        OrderEntity order = orderRepo.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("You are not authorized to cancel this order.");
        }

        if (!order.getStatus().equalsIgnoreCase("Pending")) {
            throw new IllegalStateException("Only pending orders can be cancelled.");
        }

        for (OrderItemEntity item : order.getItems()) {
            ProductEntity product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
        }

        order.setStatus("Cancelled");
        orderRepo.save(order);
    }

    @Transactional
    public void updateOrderStatus(Long orderId, String newStatus) {
        OrderEntity order = orderRepo.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        order.setStatus(newStatus);
        orderRepo.save(order);
    }

    private OrderResponseDto mapToOrderResponseDto(OrderEntity order) {
        List<OrderItemResponseDto> itemDtos = order.getItems().stream().map(item ->
                new OrderItemResponseDto(
                        item.getId(),
                        order.getId(),
                        item.getProduct().getId(),
                        item.getQuantity(),
                        item.getPrice()
                )
        ).toList();

        Long couponId = (order.getCoupon() != null) ? order.getCoupon().getId() : null;

        return new OrderResponseDto(
                order.getId(),
                order.getUser().getId(),
                order.getCreated_at(),
                order.getStatus(),
                order.getAddress().getId(),
                couponId,
                order.getDiscountAmount(),
                order.getTotalPrice(),
                itemDtos
        );
    }
}