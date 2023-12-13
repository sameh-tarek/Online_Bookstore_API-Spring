package com.sameh.onlinebookstore.mapper;

import com.sameh.onlinebookstore.entity.Category;
import com.sameh.onlinebookstore.model.category.CategoryRequestDTO;

public interface CategoryMapper {
    CategoryRequestDTO toDTO(Category category);

    Category toEntity(CategoryRequestDTO categoryRequestDTO);

}
