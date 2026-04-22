package com.e_commere.e_commerece_app.controller;

import com.e_commere.e_commerece_app.dto.JwtResponseDto;
import com.e_commere.e_commerece_app.dto.LoginRequestDto;
import com.e_commere.e_commerece_app.dto.UserRequestDto;
import com.e_commere.e_commerece_app.dto.UserResponseDto;
import com.e_commere.e_commerece_app.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody @Valid UserRequestDto request) {
        UserResponseDto response = authService.newUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> loginUser(@RequestBody @Valid LoginRequestDto request) {
        String jwt = authService.loginAndGetToken(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new JwtResponseDto(jwt));
    }
}



