package com.market.controller;

import com.market.dto.CategoryDto;
import com.market.entity.Category;
import com.market.mapper.CategoryMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.market.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
@Tag(name = "Category")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create category", description = "Creates new category")
    public CategoryDto createCategory(@RequestBody CategoryDto dto) {
        Category category = categoryMapper.toEntity(dto);
        if (dto.getParentId() != null) {
            Category parent = categoryService.getCategoryById(dto.getParentId());
            category.setParent(parent);
        }
        Category saved = categoryService.createCategory(category);
        return categoryMapper.toDto(saved);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category", description = "Returns category by Id")
    public CategoryDto getCategory(@PathVariable Long id) {
        return categoryMapper.toDto(categoryService.getCategoryById(id));
    }

    @GetMapping("/all")
    @Operation(summary = "All categories", description = "Returns all categories")
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update category", description = "Updates existing category")
    public CategoryDto updateCategory(@PathVariable Long id, @RequestBody CategoryDto dto) {
        Category updated = categoryMapper.toEntity(dto);
        if (dto.getParentId() != null) {
            Category parent = categoryService.getCategoryById(dto.getParentId());
            updated.setParent(parent);
        }
        return categoryMapper.toDto(categoryService.updateCategory(id, updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete category", description = "Deletes category by Id")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}