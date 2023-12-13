package com.sameh.onlinebookstore.mapper;

import com.sameh.onlinebookstore.entity.Book;
import com.sameh.onlinebookstore.model.book.BookRequestDTO;

public interface BookMapper {

    public BookRequestDTO toDTO(Book book);

    public Book toEntity(BookRequestDTO bookRequestDTO);
}
