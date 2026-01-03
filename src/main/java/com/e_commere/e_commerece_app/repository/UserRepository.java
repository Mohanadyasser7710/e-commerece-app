package com.e_commere.e_commerece_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.e_commere.e_commerece_app.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

}
