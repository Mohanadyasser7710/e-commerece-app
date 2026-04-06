package com.e_commere.e_commerece_app.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequestDto {
    @NotNull(message = "orderId cannot be null")
    private Long orderId;
    @NotNull(message = "productId cannot be null")
    private Long productId;
    @Min(value = 1, message = "quantity must be at least 1")
    private int quantity;
    @Min(value = 0, message = "price cannot be negative")
    private int price;
}