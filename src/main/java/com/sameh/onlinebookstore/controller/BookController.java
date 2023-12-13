package com.sameh.onlinebookstore.controller;

import com.sameh.onlinebookstore.model.book.BookRequestDTO;
import com.sameh.onlinebookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/add")
    public ResponseEntity<String> addNewBook(@RequestBody BookRequestDTO bookRequestDTO) {
        String result = bookService.addNewBook(bookRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
