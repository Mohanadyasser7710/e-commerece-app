package com.e_commere.e_commerece_app.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponRequestDto {
    @NotBlank(message = "code cannot be blank")
    private String code;
    @NotNull(message = "discountPercent cannot be null")
    @Positive(message = "discountPercent must be positive")
    @Max(value = 100, message = "discount value cant exceed 100")
    private Double discountPercent;
    @NotNull(message = "expirationDate cannot be null")
    @Future(message = "expiration date must be in the future")
    private LocalDate expirationDate;
    
}