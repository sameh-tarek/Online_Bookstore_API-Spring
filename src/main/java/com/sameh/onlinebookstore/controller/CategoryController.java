package com.sameh.onlinebookstore.controller;

import com.sameh.onlinebookstore.entity.Category;
import com.sameh.onlinebookstore.model.category.CategoryRequestDTO;
import com.sameh.onlinebookstore.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<String> addNewCategory(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        String result = categoryService.addCategory(categoryRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable(name = "id") Long id, @Valid @RequestBody CategoryRequestDTO categoryRequestDTO){
        String result = categoryService.updateCategory(id, categoryRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping ("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable(name = "id") Long id){
        String result = categoryService.deleteCategory(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
