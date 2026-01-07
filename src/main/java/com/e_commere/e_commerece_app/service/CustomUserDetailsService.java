package com.e_commere.e_commerece_app.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.e_commere.e_commerece_app.config.CustomUserDetails;
import com.e_commere.e_commerece_app.entity.UserEntity;
import com.e_commere.e_commerece_app.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String Email) throws UsernameNotFoundException {
        UserEntity user = repo.findByEmail(Email).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        return new CustomUserDetails(user);
    }

}
