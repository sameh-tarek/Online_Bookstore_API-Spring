package com.sameh.onlinebookstore.repository;

import com.sameh.onlinebookstore.entity.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    @AfterEach
    public void tearDown(){
        categoryRepository.deleteAll();
    }

    @BeforeEach
    public void setUp(){
        category = new Category();
        category.setName("Fiction");
        categoryRepository.save(category);
    }

    @DisplayName("Find By Name")
    @Test
    public void testFindByName(){
        Category category1 = categoryRepository.findByName(category.getName())
                .orElse(null);

        assertNotNull(category1);
    }
}