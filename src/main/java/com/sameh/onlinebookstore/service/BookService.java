package com.sameh.onlinebookstore.service;

import com.sameh.onlinebookstore.model.book.BookRequestDTO;

public interface BookService {

    String addNewBook(BookRequestDTO bookRequestDTO);

}
