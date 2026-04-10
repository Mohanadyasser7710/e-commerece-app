package com.e_commere.e_commerece_app.repository;

import com.e_commere.e_commerece_app.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.e_commere.e_commerece_app.entity.CartEntity;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
    Optional<CartEntity> findByUser(UserEntity user);
}