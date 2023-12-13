package com.sameh.onlinebookstore.service.impl;

import com.sameh.onlinebookstore.entity.Category;
import com.sameh.onlinebookstore.exception.ConflictException;
import com.sameh.onlinebookstore.mapper.CategoryMapper;
import com.sameh.onlinebookstore.model.category.CategoryRequestDTO;
import com.sameh.onlinebookstore.repository.CategoryRepository;
import com.sameh.onlinebookstore.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public String addCategory(CategoryRequestDTO categoryRequestDTO) {
        log.warn("Admin want to add this category {}", categoryRequestDTO);

        categoryRepository.findByName(categoryRequestDTO.getName())
                .ifPresent(existingCategory -> {
                    throw new ConflictException("Category with name: " + categoryRequestDTO.getName() + " already exists");
                });

        Category category = categoryMapper.toEntity(categoryRequestDTO);
        categoryRepository.save(category);
        log.warn("This new category has been added successfully {}", category);
        return "Success";
    }
}
