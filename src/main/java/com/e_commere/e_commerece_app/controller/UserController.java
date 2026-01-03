package com.e_commere.e_commerece_app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.e_commere.e_commerece_app.dto.UserRequestDto;
import com.e_commere.e_commerece_app.dto.UserResponseDto;
import com.e_commere.e_commerece_app.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = service.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        UserResponseDto dto = service.getUserbyId(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> postNewUser(@RequestBody @Valid UserRequestDto newdto) {
        UserResponseDto dto = service.newUser(newdto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> editUser(@PathVariable Long id, @RequestBody @Valid UserRequestDto oldDto) {
        UserResponseDto newDto = service.updateUser(id, oldDto);
        return ResponseEntity.ok(newDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) {
        service.deleteUser(id);
        return ResponseEntity.ok("user deleted");
    }

}
