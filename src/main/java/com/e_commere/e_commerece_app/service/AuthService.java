package com.e_commere.e_commerece_app.service;


import com.e_commere.e_commerece_app.dto.UserRequestDto;
import com.e_commere.e_commerece_app.dto.UserResponseDto;
import com.e_commere.e_commerece_app.entity.CartEntity;
import com.e_commere.e_commerece_app.entity.UserEntity;
import com.e_commere.e_commerece_app.repository.UserRepository;
import com.e_commere.e_commerece_app.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public UserResponseDto newUser(UserRequestDto dto) {
        UserEntity newUser=mapToEntity(dto);
        CartEntity newCart= new CartEntity();
        newCart.setTotalPrice(0.0);
        newUser.addCart(newCart);
        userRepository.save(newUser);
        return mapToResponse(newUser);

    }
    public String loginAndGetToken(String username , String password) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return tokenProvider.createToken(authentication);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid username or password");
        }
    }

    public UserEntity mapToEntity(UserRequestDto dto) {
        UserEntity user = new UserEntity();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole("ROLE_USER");
        return user;
    }

    public UserResponseDto mapToResponse(UserEntity user) {
        return new UserResponseDto(user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole());
    }

}
