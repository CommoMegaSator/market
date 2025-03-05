package com.market.service;

import static org.junit.jupiter.api.Assertions.*;

import com.market.entity.Category;
import com.market.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");
    }

    @Test
    void createCategory_ShouldSaveAndReturnCategory() {
        when(categoryRepository.save(category)).thenReturn(category);

        Category created = categoryService.createCategory(category);

        assertNotNull(created);
        assertEquals("Electronics", created.getName());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void getCategoryById_WhenCategoryExists_ShouldReturnCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category found = categoryService.getCategoryById(1L);
        assertNotNull(found);
        assertEquals(1L, found.getId());
        assertEquals("Electronics", found.getName());
    }

    @Test
    void getCategoryById_WhenCategoryDoesNotExist_ShouldThrowException() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class,
                () -> categoryService.getCategoryById(2L));
        assertEquals("Category not found", exception.getMessage());
    }

    @Test
    void getAllCategories_ShouldReturnListOfCategories() {
        List<Category> categories = Arrays.asList(category, new Category());
        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getAllCategories();
        assertEquals(2, result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void updateCategory_ShouldUpdateAndSaveCategory() {
        Category newCategoryData = new Category();
        newCategoryData.setName("New Electronics");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Category updated = categoryService.updateCategory(1L, newCategoryData);

        assertNotNull(updated);
        assertEquals("New Electronics", updated.getName());
        verify(categoryRepository, times(1)).save(updated);
    }

    @Test
    void deleteCategory_ShouldCallRepositoryDelete() {
        doNothing().when(categoryRepository).deleteById(1L);

        categoryService.deleteCategory(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }
}
