package com.e_commere.e_commerece_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private LocalDateTime createdAt;
    private String status;
    private Long addressId;
    private Long couponId;
    private double discountAmount;
    private Double totalPrice;
    private List<OrderItemResponseDto> items;
}