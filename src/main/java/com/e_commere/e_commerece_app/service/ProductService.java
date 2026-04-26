package com.e_commere.e_commerece_app.service;

import com.e_commere.e_commerece_app.dto.ProductRequestDto;
import com.e_commere.e_commerece_app.dto.ProductResponseDto;
import com.e_commere.e_commerece_app.entity.CategoryEntity;
import com.e_commere.e_commerece_app.entity.ProductEntity;
import com.e_commere.e_commerece_app.repository.CategoryRepository;
import com.e_commere.e_commerece_app.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto request){
        ProductEntity newProduct=mapToEntity(request);
        ProductEntity savedProduct=productRepo.save(newProduct);
        return mapToResponse(savedProduct);
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(Long id){
        ProductEntity product=productRepo.findById(id).orElseThrow(()-> new EntityNotFoundException("Product with id " + id + " was not found"));
        return mapToResponse(product);
    }

    public Page<ProductResponseDto> getProducts(String keyword, Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductEntity> productPage;
        if (keyword != null && !keyword.isBlank() && categoryId != null) {
            productPage = productRepo.findByNameContainingIgnoreCaseAndCategoryId(keyword, categoryId, pageable);
        } else if (keyword != null && !keyword.isBlank()) {
            productPage = productRepo.findByNameContainingIgnoreCase(keyword, pageable);
        } else if (categoryId != null) {
            productPage = productRepo.findByCategoryId(categoryId, pageable);
        } else {
            productPage = productRepo.findAll(pageable);
        }
        return productPage.map(this::mapToResponse);
    }
    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductRequestDto request){
        ProductEntity oldProduct=productRepo.findById(id).orElseThrow(()->new EntityNotFoundException("Product with id " + id + " was not found"));
        CategoryEntity category=categoryRepo.findById(request.getCategoryId()).orElseThrow(()->new EntityNotFoundException("Category was not found"));
        oldProduct.setName(request.getName());
        oldProduct.setPrice(request.getPrice());
        oldProduct.setCategory(category);
        oldProduct.setStockQuantity(request.getStockQuantity());
        ProductEntity updated=productRepo.save(oldProduct);
        return mapToResponse(updated);
    }
    @Transactional
    public void deleteProduct(Long id){
        productRepo.deleteById(id);
    }



    public ProductEntity mapToEntity(ProductRequestDto dto){
        ProductEntity product = new ProductEntity();
        product.setName(dto.getName());
        product.setCategory(categoryRepo.findById(dto.getCategoryId()).orElseThrow(()-> new EntityNotFoundException("the id you entered was not found in the database ")));
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        return product;
    }

    public ProductResponseDto mapToResponse(ProductEntity product){
        ProductResponseDto dto=new ProductResponseDto();
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setId(product.getId());
        dto.setCategoryId(product.getCategory().getId());
        dto.setCategoryName(product.getCategory().getName());
        return dto;

    }


}
