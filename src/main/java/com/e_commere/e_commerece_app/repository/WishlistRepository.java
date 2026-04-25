package com.e_commere.e_commerece_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.e_commere.e_commerece_app.entity.WishlistEntity;

import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistEntity, Long> {
    Optional<WishlistEntity> findByUserId(Long userId);
}