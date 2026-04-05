package com.e_commere.e_commerece_app.security;

import com.e_commere.e_commerece_app.config.CustomUserDetails;
import com.e_commere.e_commerece_app.entity.UserEntity;
import com.e_commere.e_commerece_app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;



@AllArgsConstructor
@Component
@NullMarked
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repo;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user=repo.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("Username with this email"+email+"was not found"));
        return new CustomUserDetails(user);


    }
}
