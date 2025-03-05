package com.market.service;

import com.market.dto.ProductDto;
import com.market.entity.Category;
import com.market.entity.Product;
import com.market.mapper.ProductMapper;
import com.market.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CurrencyConversionService currencyConversionService;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;
    private final String DEFAULT_CURRENCY = "EUR";

    public Product createProduct(ProductDto productDto) {
        productDto = exchangeToDefaultCurrency(productDto);

        Product product = productMapper.toEntity(productDto);

        if (productDto.getCategoryId() != null) {
            Category category = categoryService.getCategoryById(productDto.getCategoryId());
            product.setCategory(category);
        }

        return productRepository.save(product);
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product updateProduct(Long id, ProductDto productDto) {
        productDto = exchangeToDefaultCurrency(productDto);
        productDto.setId(id);

        Product product = productMapper.toEntity(productDto);
        if (productDto.getCategoryId() != null) {
            Category category = categoryService.getCategoryById(productDto.getCategoryId());
            product.setCategory(category);
        }

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private ProductDto exchangeToDefaultCurrency(ProductDto productDto) {
        if (!DEFAULT_CURRENCY.equalsIgnoreCase(productDto.getCurrency())) {
            BigDecimal priceInEur = currencyConversionService.convertCurrency(productDto.getCurrency(), DEFAULT_CURRENCY, productDto.getPrice());
            productDto.setPrice(priceInEur);
            productDto.setCurrency(DEFAULT_CURRENCY);
        }
        return productDto;
    }
}
