package com.sameh.onlinebookstore.service.impl;

import com.sameh.onlinebookstore.entity.Category;
import com.sameh.onlinebookstore.exception.ConflictException;
import com.sameh.onlinebookstore.exception.NoUpdateFoundException;
import com.sameh.onlinebookstore.exception.RecordNotFoundException;
import com.sameh.onlinebookstore.mapper.CategoryMapper;
import com.sameh.onlinebookstore.model.category.CategoryRequestDTO;
import com.sameh.onlinebookstore.repository.CategoryRepository;
import com.sameh.onlinebookstore.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
        log.info("Admin want to add this category {}", categoryRequestDTO);

        categoryRepository.findByName(categoryRequestDTO.getName())
                .ifPresent(existingCategory -> {
                    log.error("Category with name: {} already exists", categoryRequestDTO.getName());
                    throw new ConflictException("Category with name: " + categoryRequestDTO.getName() + " already exists");
                });

        Category category = categoryMapper.toEntity(categoryRequestDTO);
        categoryRepository.save(category);
        log.info("This new category has been added successfully {}", category);
        return "Success";
    }

    @Override
    public String updateCategory(Long id, CategoryRequestDTO categoryRequestDTO) {
        log.info("Admin want to update the category with id {}", id);
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("This category with id "+ id + "doesn't exist"));
        Category updatedCategory = categoryMapper.toEntity(categoryRequestDTO);
        updatedCategory.setId(existingCategory.getId());

        if(existingCategory.equals(updatedCategory)){
            log.error("not found any updates the updateCategory: {}, the existingCategory: {}", updatedCategory,existingCategory);
            throw new NoUpdateFoundException("Not found Any update in the Category details");
        }
        log.info("The category Before update {}", existingCategory);
        BeanUtils.copyProperties(updatedCategory, existingCategory, "id");
        categoryRepository.save(existingCategory);
        log.info("The Category Updated Successfully, the Category after update {}", existingCategory);
        return "The Category updated successfully";
    }

    @Override
    public String deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("This category with id "+ id + " doesn't exist"));
        log.info("Admin want to delete this Category {}" , category);
        categoryRepository.delete(category);
        log.info("The Category deleted Successfully");
        return "Category Deleted Successfully";
    }
}
