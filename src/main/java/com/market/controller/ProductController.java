package com.market.controller;

import com.market.dto.ProductDto;
import com.market.entity.Product;
import com.market.mapper.ProductMapper;
import com.market.service.CategoryService;
import com.market.service.ProductService;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Products")
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create product", description = "Create new product")
    public ProductDto createProduct(@RequestBody ProductDto dto) {
        Product saved = productService.createProduct(dto);
        return productMapper.toDto(saved);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Product", description = "Returns existing product by Id")
    public ProductDto getProduct(@PathVariable Long id) {
        return productMapper.toDto(productService.getProduct(id));
    }

    @GetMapping
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts().stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update product", description = "Updates existing product")
    public ProductDto updateProduct(@PathVariable Long id, @RequestBody ProductDto dto) {
        Product updated = productService.updateProduct(id, dto);
        return productMapper.toDto(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete product", description = "Deletes existing product")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}