package com.sameh.onlinebookstore.mapper.impl;

import com.sameh.onlinebookstore.entity.Book;
import com.sameh.onlinebookstore.mapper.BookMapper;
import com.sameh.onlinebookstore.model.book.BookRequestDTO;
import com.sameh.onlinebookstore.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Component
public class BookMapperImpl implements BookMapper {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public BookRequestDTO toDTO(Book book) {
        BookRequestDTO bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setAuthor(book.getAuthor());
        bookRequestDTO.setAvailable(book.isAvailable());
        bookRequestDTO.setTitle(book.getTitle());
        bookRequestDTO.setDescription(book.getDescription());
        bookRequestDTO.setCategoryId(book.getCategory().getId());

        return bookRequestDTO;
    }

    @Override
    public Book toEntity(BookRequestDTO bookRequestDTO) {
        Book book = new Book();
        book.setAuthor(bookRequestDTO.getAuthor());
        book.setAvailable(bookRequestDTO.isAvailable());
        book.setPublishDate(LocalDateTime.now());
        book.setTitle(bookRequestDTO.getTitle());
        book.setDescription(bookRequestDTO.getDescription());
        var category = categoryRepository.findById(bookRequestDTO.getCategoryId()).orElse(null);
        book.setCategory(category);

        return book;
    }
}
