package com.sameh.onlinebookstore.service.impl;

import com.sameh.onlinebookstore.entity.Book;
import com.sameh.onlinebookstore.entity.Category;
import com.sameh.onlinebookstore.exception.ConflictException;
import com.sameh.onlinebookstore.exception.RecordNotFoundException;
import com.sameh.onlinebookstore.mapper.BookMapper;
import com.sameh.onlinebookstore.model.book.BookRequestDTO;
import com.sameh.onlinebookstore.repository.BookRepository;
import com.sameh.onlinebookstore.repository.CategoryRepository;
import com.sameh.onlinebookstore.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public String addNewBook(BookRequestDTO bookRequestDTO) {
        log.warn("Admin want to add this book {}", bookRequestDTO);

        bookRepository.findByTitle(bookRequestDTO.getTitle())
                .ifPresent(existingBook -> {
                    throw new ConflictException("Book with Title: " + bookRequestDTO.getTitle() + " already exists");
                });

        Category bookCategory = categoryRepository.findById(bookRequestDTO.getCategoryId())
                .orElseThrow(() -> new RecordNotFoundException("Category with ID " + bookRequestDTO.getCategoryId() + " not found"));

        Book newBook = bookMapper.toEntity(bookRequestDTO);
        bookRepository.save(newBook);
        log.warn("This new Book has been added successfully {}", newBook);
        return "Success";
    }
}
