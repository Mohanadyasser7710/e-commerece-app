package com.e_commere.e_commerece_app.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {
    @NotBlank(message = "name cannot be blank")
    private String name;
    @NotNull(message = "price cannot be null")
    @Positive(message = "price must be positive")
    private Double price;
    @NotNull(message = "categoryId cannot be null")
    private Long categoryId;
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private int stockQuantity;
}