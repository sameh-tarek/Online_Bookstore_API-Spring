package com.sameh.onlinebookstore.service;

import com.sameh.onlinebookstore.model.category.CategoryRequestDTO;

public interface CategoryService {
    String addCategory(CategoryRequestDTO categoryRequestDTO);

    String updateCategory(Long id, CategoryRequestDTO categoryRequestDTO);

    String deleteCategory(Long id);
}
