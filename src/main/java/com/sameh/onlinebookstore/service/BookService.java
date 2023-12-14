package com.sameh.onlinebookstore.service;

import com.sameh.onlinebookstore.model.Stock.StockUpdateRequest;
import com.sameh.onlinebookstore.model.book.BookAvailabilityRequest;
import com.sameh.onlinebookstore.model.book.BookRequestDTO;

public interface BookService {

    String addNewBook(BookRequestDTO bookRequestDTO);

    String updateBook(Long id, BookRequestDTO bookRequestDTO);

    String setBookAvailability(Long id, BookAvailabilityRequest bookAvailabilityRequest);

    String deleteBook(Long id);

    String updateStock(Long id, StockUpdateRequest stockUpdateRequest);
}
