package com.sameh.onlinebookstore.service;

import com.sameh.onlinebookstore.entity.enums.Status;
import com.sameh.onlinebookstore.model.borrowingRequest.BorrowingRequestWrapperDTO;
import com.sameh.onlinebookstore.model.stock.StockUpdateRequest;
import com.sameh.onlinebookstore.model.book.BookAvailabilityRequest;
import com.sameh.onlinebookstore.model.book.BookRequestDTO;
import com.sameh.onlinebookstore.model.borrowingRequest.BorrowingRequestDTO;

import java.util.List;

public interface BookService {

    String addNewBook(BookRequestDTO bookRequestDTO);

    String updateBook(Long id, BookRequestDTO bookRequestDTO);

    String setBookAvailability(Long id, BookAvailabilityRequest bookAvailabilityRequest);

    String deleteBook(Long id);

    String updateStock(Long id, StockUpdateRequest stockUpdateRequest);

    List<BookRequestDTO> getBooksByCategory(Long categoryId);

    List<BookRequestDTO> getBooksByCategory(String categoryName);

    BookRequestDTO getBookDetailsById(Long id);

    String requestBorrowing(Long bookId, Long userId);

    List<BorrowingRequestDTO> getAllBorrowingRequests();

    String updateBorrowingStatus(Long requestId, Status newStatus, Long userId);

    List<BorrowingRequestWrapperDTO> getCustomerBorrowingRequests(Long userId);
}
