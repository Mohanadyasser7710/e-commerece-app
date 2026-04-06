package com.e_commere.e_commerece_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDto {
    @NotNull(message = "order Id cannot be null")
    private Long orderId;
    @NotBlank(message = "paymentMethod cannot be blank")
    private String paymentMethod;

}