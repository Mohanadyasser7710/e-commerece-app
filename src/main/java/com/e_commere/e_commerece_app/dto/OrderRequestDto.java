package com.e_commere.e_commerece_app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
    @NotNull(message = "addressId cannot be null")
    private Long addressId;
    private String couponCode;

}