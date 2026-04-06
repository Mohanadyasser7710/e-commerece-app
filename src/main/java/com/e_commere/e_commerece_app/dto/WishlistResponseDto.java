package com.e_commere.e_commerece_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistResponseDto {
    private Long id;
    private Long userId;
    private List<WishlistItemResponseDto> items;
}