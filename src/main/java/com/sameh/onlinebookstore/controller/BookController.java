package com.sameh.onlinebookstore.controller;

import com.sameh.onlinebookstore.model.Stock.StockUpdateRequest;
import com.sameh.onlinebookstore.model.book.BookAvailabilityRequest;
import com.sameh.onlinebookstore.model.book.BookRequestDTO;
import com.sameh.onlinebookstore.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/add")
    public ResponseEntity<String> addNewBook(@Valid @RequestBody BookRequestDTO bookRequestDTO) {
        String result = bookService.addNewBook(bookRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateBook(@PathVariable(name = "id") Long id
            , @RequestBody BookRequestDTO bookRequestDTO){
        String result = bookService.updateBook(id, bookRequestDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<String> setBookAvailability(@PathVariable(name = "id") Long id,@Valid @RequestBody BookAvailabilityRequest bookAvailabilityRequest){
        String result = bookService.setBookAvailability(id, bookAvailabilityRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<String> updateStock(@PathVariable(name = "id") Long id, @Valid @RequestBody StockUpdateRequest stockUpdateRequest){
        String result = bookService.updateStock(id, stockUpdateRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable(name = "id") Long id){
        String result = bookService.deleteBook(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
