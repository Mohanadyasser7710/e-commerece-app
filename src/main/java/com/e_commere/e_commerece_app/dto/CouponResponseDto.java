package com.e_commere.e_commerece_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponResponseDto {
    private Long id;
    private String code;
    private Double discountPercent;
    private LocalDate expirationDate;
    private boolean isActive;
}