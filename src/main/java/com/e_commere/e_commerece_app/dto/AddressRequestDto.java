package com.e_commere.e_commerece_app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequestDto {
    @NotBlank(message = "street cannot be blank")
    private String street;
    @NotBlank(message = "city cannot be blank")
    private String city;

}