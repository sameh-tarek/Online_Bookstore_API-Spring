package com.sameh.onlinebookstore.controller;

import com.sameh.onlinebookstore.model.stock.StockUpdateRequest;
import com.sameh.onlinebookstore.model.book.BookAvailabilityRequest;
import com.sameh.onlinebookstore.model.book.BookRequestDTO;
import com.sameh.onlinebookstore.model.borrowingRequest.BorrowingRequestDTO;
import com.sameh.onlinebookstore.service.BookService;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/category/{category}")
    public ResponseEntity<List<BookRequestDTO>> getBooksByCategory(@PathVariable(name = "category") String category){
        if(StringUtils.isNumeric(category)){
            Long categoryId = Long.parseLong(category);
            return new ResponseEntity<>(bookService.getBooksByCategory(categoryId),HttpStatus.OK);
        }else {
            return new ResponseEntity<>(bookService.getBooksByCategory(category),HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookRequestDTO> getBookDetailsById(@PathVariable(name = "id") Long id){
        BookRequestDTO result = bookService.getBookDetailsById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable(name = "id") Long id){
        String result = bookService.deleteBook(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/{id}/borrow")
    public ResponseEntity<String> requestBorrowing(@PathVariable(name = "id") Long bookId){
        Long userId = 1l; // get current user id
        String result =  bookService.requestBorrowing(bookId, userId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/requests")
    public ResponseEntity<List<BorrowingRequestDTO>> getAllBorrowingRequests() {
        List<BorrowingRequestDTO> borrowingRequests = bookService.getAllBorrowingRequests();
        return new ResponseEntity<>(borrowingRequests, HttpStatus.OK);
    }


}
