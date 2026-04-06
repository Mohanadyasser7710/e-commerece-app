package com.e_commere.e_commerece_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponseDto {
    private Long id;
    private Long productId;
    private int quantity;
    private LocalDateTime lastUpdated;
}