package com.sameh.onlinebookstore.repository;

import com.sameh.onlinebookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends
        JpaRepository<Book, Long> {
    Optional<Book> findByTitle(String title);

    List<Book> findByCategoryId(Long categoryId);

    List<Book> findByCategoryName(String categoryName);

}
