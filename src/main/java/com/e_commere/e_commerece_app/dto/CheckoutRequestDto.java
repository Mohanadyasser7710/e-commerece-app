package com.e_commere.e_commerece_app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CheckoutRequestDto {
    @NotNull(message = "Address ID is required for checkout")
    private Long addressId;
    private Long couponId;
}

