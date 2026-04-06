package com.e_commere.e_commerece_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistItemResponseDto {
    private Long id;
    private Long wishlistId;
    private ProductResponseDto product;
    private LocalDateTime addedAt;
}