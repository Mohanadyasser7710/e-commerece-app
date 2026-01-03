package com.e_commere.e_commerece_app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    @NotBlank(message = "name cant be blank")
    @Size(min = 3, message = "name cant be less than three characters")
    private String name;
    @NotBlank(message = "password cant be blank")
    @Size(min = 8, max = 20, message = "password cant be less than 8 or more than 20")
    private String password;
    @NotBlank(message = "email cant be blank")
    @Email(message = "invalid email format")
    private String email;

}
