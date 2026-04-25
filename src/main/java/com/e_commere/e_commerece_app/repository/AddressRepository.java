package com.e_commere.e_commerece_app.repository;

import com.e_commere.e_commerece_app.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.e_commere.e_commerece_app.entity.AddressEntity;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    Optional<AddressEntity> findByIdAndUser(Long addressId, UserEntity user);
}