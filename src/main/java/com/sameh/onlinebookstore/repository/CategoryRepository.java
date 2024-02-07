package com.sameh.onlinebookstore.repository;

import com.sameh.onlinebookstore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends
        JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
