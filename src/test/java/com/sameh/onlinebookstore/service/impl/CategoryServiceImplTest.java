package com.sameh.onlinebookstore.service.impl;


import com.sameh.onlinebookstore.entity.Category;
import com.sameh.onlinebookstore.exception.ConflictException;
import com.sameh.onlinebookstore.exception.NoUpdateFoundException;
import com.sameh.onlinebookstore.exception.RecordNotFoundException;
import com.sameh.onlinebookstore.mapper.CategoryMapper;
import com.sameh.onlinebookstore.model.category.CategoryRequestDTO;
import com.sameh.onlinebookstore.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    CategoryMapper categoryMapper;
    @Mock
    CategoryRepository categoryRepository;
    @InjectMocks
    CategoryServiceImpl categoryService;

    @Test
    public void addCategoryShouldSuccess(){
        // Arrange
        CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO("Fiction", "Description of fiction books");
        Category category = new Category(1L, "Fiction", "Description of fiction books");
        when(categoryMapper.toEntity(categoryRequestDTO)).thenReturn(category);

        // Act
        String result = categoryService.addCategory(categoryRequestDTO);

        // Assert
        assertThat(result).isEqualTo("Success");
    }

    @Test
    public void addCategoryShouldThrowConflictExceptionIfCategoryExist(){
        // Arrange
        CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO("Fiction", "Description of fiction books");
        Category category = new Category(1L, "Fiction", "Description of fiction books");
        when(categoryRepository.findByName(category.getName())).thenReturn(Optional.of(category));

        // Act & Assert
        assertThatThrownBy(() -> categoryService.addCategory(categoryRequestDTO))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Category with name: Fiction already exists");
    }

    @Test
    public void updateCategoryShouldSuccess(){
        // Arrange
        Category category = new Category(1L, "Fiction", "Description of fiction books");
        CategoryRequestDTO updatedCategoryRequestDTO = new CategoryRequestDTO("X-Fiction", "Description of fiction books");
        Category updatedCategory = new Category(1L, "X-Fiction", "Description of fiction books");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toEntity(updatedCategoryRequestDTO)).thenReturn(updatedCategory);

        // Act
        String result = categoryService.updateCategory(1L, updatedCategoryRequestDTO);

        // Assert
        assertThat(result).isEqualTo("The Category updated successfully");
    }

    @Test
    public void updateCategoryShouldThrowRecordNotFoundExceptionIfCategoryDoesNotExist(){
        // Arrange
        CategoryRequestDTO updatedCategoryRequestDTO = new CategoryRequestDTO("X-Fiction", "Description of fiction books");
        Category updatedCategory = new Category(1L, "X-Fiction", "Description of fiction books");

        // Act & Assert
        assertThatThrownBy(() -> categoryService.updateCategory(1L, updatedCategoryRequestDTO))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("This category with id 1 doesn't exist");
    }

    @Test
    public void updateCategoryShouldThrowNoUpdateFoundExceptionIfNoUpdate(){
        // Arrange
        Category category = new Category(1L, "Fiction", "Description of fiction books");
        CategoryRequestDTO updatedCategoryRequestDTO = new CategoryRequestDTO("Fiction", "Description of fiction books");
        Category updatedCategory = new Category(1L, "Fiction", "Description of fiction books");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toEntity(updatedCategoryRequestDTO)).thenReturn(updatedCategory);

        // Act & Assert
        assertThatThrownBy(() -> categoryService.updateCategory(1L, updatedCategoryRequestDTO))
                .isInstanceOf(NoUpdateFoundException.class)
                .hasMessageContaining("Not found Any update in the Category details");
    }

    @Test
    public void deleteCategoryShouldSuccess(){
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category()));

        // Act
        String result = categoryService.deleteCategory(1L);

        // Assert
        assertThat(result).isEqualTo("Category Deleted Successfully");
    }

    @Test
    public void deleteCategoryShouldThrowRecordNotFoundExceptionIfCategoryDoesNotExist(){
        // Act & Assert
        assertThatThrownBy(() -> categoryService.deleteCategory(1L))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("This category with id 1 doesn't exist");
    }

}