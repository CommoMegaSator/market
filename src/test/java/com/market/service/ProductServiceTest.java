package com.market.service;

import static org.junit.jupiter.api.Assertions.*;

import com.market.dto.ProductDto;
import com.market.entity.Category;
import com.market.entity.Product;
import com.market.mapper.ProductMapper;
import com.market.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CurrencyConversionService currencyConversionService;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductService productService;

    private final String DEFAULT_CURRENCY = "EUR";

    private ProductDto productDto;
    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Test Product");
        productDto.setPrice(BigDecimal.valueOf(100));
        productDto.setCurrency("USD");
        productDto.setCategoryId(10L);

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(85));
        product.setCurrency(DEFAULT_CURRENCY);

        category = new Category();
        category.setId(10L);
        category.setName("Test Category");
    }

    @Test
    void createProduct_ShouldConvertCurrencyAndSaveProduct() {
        when(currencyConversionService.convertCurrency("USD", DEFAULT_CURRENCY, productDto.getPrice()))
                .thenReturn(BigDecimal.valueOf(85));

        when(productMapper.toEntity(productDto)).thenReturn(product);

        when(categoryService.getCategoryById(productDto.getCategoryId())).thenReturn(category);
        product.setCategory(category);

        when(productRepository.save(product)).thenReturn(product);

        Product created = productService.createProduct(productDto);

        assertEquals(DEFAULT_CURRENCY, productDto.getCurrency());
        assertEquals(BigDecimal.valueOf(85), productDto.getPrice());
        assertNotNull(created);
        verify(currencyConversionService, times(1))
                .convertCurrency("USD", DEFAULT_CURRENCY, BigDecimal.valueOf(100));
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void getProduct_WhenProductExists_ShouldReturnProduct() {
        Long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Product found = productService.getProduct(productId);
        assertNotNull(found);
        assertEquals(productId, found.getId());
    }

    @Test
    void getProduct_WhenProductNotFound_ShouldThrowException() {
        Long productId = 2L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.getProduct(productId));
    }

    @Test
    void getAllProducts_ShouldReturnProductList() {
        List<Product> productList = List.of(product);
        when(productRepository.findAll()).thenReturn(productList);

        List<Product> result = productService.getAllProducts();
        assertEquals(1, result.size());
        assertEquals(product, result.get(0));
    }

    @Test
    void updateProduct_ShouldUpdateAndSaveProduct() {
        Long productId = productDto.getId();
        productDto.setPrice(BigDecimal.valueOf(200));
        productDto.setCurrency("USD");

        when(currencyConversionService.convertCurrency("USD", DEFAULT_CURRENCY, BigDecimal.valueOf(200)))
                .thenReturn(BigDecimal.valueOf(170));
        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName("Test Product Updated");
        updatedProduct.setPrice(BigDecimal.valueOf(170));
        updatedProduct.setCurrency(DEFAULT_CURRENCY);

        when(productMapper.toEntity(productDto)).thenReturn(updatedProduct);
        when(categoryService.getCategoryById(productDto.getCategoryId())).thenReturn(category);
        updatedProduct.setCategory(category);

        when(productRepository.save(updatedProduct)).thenReturn(updatedProduct);

        Product result = productService.updateProduct(productId, productDto);
        assertEquals(BigDecimal.valueOf(170), result.getPrice());
        assertEquals(DEFAULT_CURRENCY, result.getCurrency());
        verify(productRepository, times(1)).save(updatedProduct);
    }

    @Test
    void deleteProduct_ShouldCallRepositoryDelete() {
        Long productId = 1L;
        productService.deleteProduct(productId);
        verify(productRepository, times(1)).deleteById(productId);
    }
}
