package com.e_commere.e_commerece_app.controller;

import com.e_commere.e_commerece_app.dto.ProductRequestDto;
import com.e_commere.e_commerece_app.dto.ProductResponseDto;
import com.e_commere.e_commerece_app.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(service.getProducts(keyword, categoryId, page, size));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody @Valid ProductRequestDto dto){
        ProductResponseDto newDto=service.createProduct(dto);
        return new ResponseEntity<>(newDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getById(@PathVariable Long id){
        ProductResponseDto dto=service.getProductById(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@RequestBody @Valid ProductRequestDto update,@PathVariable Long id){
        ProductResponseDto newDto=service.updateProduct(id,update);
        return ResponseEntity.ok(newDto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        service.deleteProduct(id);
        return ResponseEntity.ok("deleted successfully");
    }

}
