package com.e_commere.e_commerece_app.repository;

import com.e_commere.e_commerece_app.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.e_commere.e_commerece_app.entity.OrderEntity;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    @Query("SELECT o FROM OrderEntity o WHERE o.user=:user ORDER BY o.created_at DESC")
    List<OrderEntity> findOrderHistoryForUser(@Param("user") UserEntity user);

}