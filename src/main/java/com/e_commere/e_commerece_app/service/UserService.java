package com.e_commere.e_commerece_app.service;

import java.util.List;

import com.e_commere.e_commerece_app.entity.CartEntity;
import com.e_commere.e_commerece_app.security.TokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.e_commere.e_commerece_app.dto.UserRequestDto;
import com.e_commere.e_commerece_app.dto.UserResponseDto;
import com.e_commere.e_commerece_app.entity.UserEntity;
import com.e_commere.e_commerece_app.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;



    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return repo.findAll().stream().map(this::mapToResponse).toList();
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        UserEntity user = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user with the id " + id + " cannot be found"));
        return mapToResponse(user);
    }



    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto dto) {
        UserEntity updateduser = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user with the id " + id + " cannot be found"));
        updateduser.setName(dto.getName());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            updateduser.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        updateduser.setEmail(dto.getEmail());
        repo.save(updateduser);
        return mapToResponse(updateduser);

    }

    @Transactional
    public String deleteUser(long id) {
        UserEntity user = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user with the id " + id + " cannot be found"));
        repo.delete(user);
        return ("the user with the id " + id + " was deleted");
    }




    public UserResponseDto mapToResponse(UserEntity user) {
        return new UserResponseDto(user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole());
    }

}
