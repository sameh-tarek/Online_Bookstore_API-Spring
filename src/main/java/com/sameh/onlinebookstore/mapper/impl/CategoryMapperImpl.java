package com.sameh.onlinebookstore.mapper.impl;

import com.sameh.onlinebookstore.entity.Category;
import com.sameh.onlinebookstore.mapper.CategoryMapper;
import com.sameh.onlinebookstore.model.category.CategoryRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapperImpl implements CategoryMapper {
    @Override
    public CategoryRequestDTO toDTO(Category category) {
        CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
        categoryRequestDTO.setName(category.getName());
        categoryRequestDTO.setDescription(category.getDescription());

        return categoryRequestDTO;
    }

    @Override
    public Category toEntity(CategoryRequestDTO categoryRequestDTO) {
        Category category = new Category();
        category.setName(categoryRequestDTO.getName());
        category.setDescription(categoryRequestDTO.getDescription());

        return category;
    }
}
