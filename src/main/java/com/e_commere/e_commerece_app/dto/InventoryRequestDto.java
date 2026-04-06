package com.e_commere.e_commerece_app.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRequestDto {
    @NotNull(message = "productId cannot be null")
    private Long productId;
    @Min(value = 0, message = "quantity cannot be negative")
    private int quantity;
}