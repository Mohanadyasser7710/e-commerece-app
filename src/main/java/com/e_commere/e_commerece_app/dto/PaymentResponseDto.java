package com.e_commere.e_commerece_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDto {
    private Long id;
    private Long orderId;
    private LocalDateTime paymentDate;
    private Double amount;
    private String paymentMethod;
    private String status;
}