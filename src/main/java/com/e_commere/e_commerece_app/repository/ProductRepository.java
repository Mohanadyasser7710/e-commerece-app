package com.e_commere.e_commerece_app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.e_commere.e_commerece_app.entity.ProductEntity;

;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    Page<ProductEntity> findByNameContainingIgnoreCase(String keyword, Pageable pageable);


    Page<ProductEntity> findByCategoryId(Long categoryId, Pageable pageable);


    Page<ProductEntity> findByNameContainingIgnoreCaseAndCategoryId(String keyword, Long categoryId, Pageable pageable);
}