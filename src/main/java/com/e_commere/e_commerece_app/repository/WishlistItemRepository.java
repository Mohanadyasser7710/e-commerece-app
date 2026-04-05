package com.e_commere.e_commerece_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.e_commere.e_commerece_app.entity.WishlistItemEntity;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItemEntity, Long> {
}