package com.e_commere.e_commerece_app.service;

import com.e_commere.e_commerece_app.dto.CategoryRequestDto;
import com.e_commere.e_commerece_app.dto.CategoryResponseDto;
import com.e_commere.e_commerece_app.entity.CategoryEntity;
import com.e_commere.e_commerece_app.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepo;

    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepo.findAll().stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public CategoryResponseDto getCategoryById(Long id) {
        CategoryEntity category = categoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        return mapToResponseDto(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        CategoryEntity category = categoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));


        if (category.getProducts() != null && !category.getProducts().isEmpty()) {
            throw new IllegalStateException("Cannot delete category; it still contains active products.");
        }

        categoryRepo.delete(category);
    }

    @Transactional
    public CategoryResponseDto createCategory(CategoryRequestDto request) {

        CategoryEntity category = mapToEntity(request);
        CategoryEntity savedCategory = categoryRepo.save(category);
        return mapToResponseDto(savedCategory);
    }

    @Transactional
    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto request) {
        CategoryEntity category = categoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        updateEntityFromDto(category, request);

        return mapToResponseDto(categoryRepo.save(category));
    }



    private CategoryEntity mapToEntity(CategoryRequestDto request) {
        CategoryEntity entity = new CategoryEntity();
        entity.setName(request.getName().trim());
        entity.setDescription(request.getDescription());
        return entity;
    }

    private void updateEntityFromDto(CategoryEntity entity, CategoryRequestDto request) {
        entity.setName(request.getName().trim());
        entity.setDescription(request.getDescription());
    }

    private CategoryResponseDto mapToResponseDto(CategoryEntity category) {
        return new CategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}