package com.sameh.onlinebookstore.service.impl;

import com.sameh.onlinebookstore.entity.Book;
import com.sameh.onlinebookstore.entity.BorrowingRequest;
import com.sameh.onlinebookstore.entity.User;
import com.sameh.onlinebookstore.exception.ConflictException;
import com.sameh.onlinebookstore.exception.NoUpdateFoundException;
import com.sameh.onlinebookstore.exception.RecordNotFoundException;
import com.sameh.onlinebookstore.mapper.BookMapper;
import com.sameh.onlinebookstore.mapper.BorrowingRequestMapper;
import com.sameh.onlinebookstore.model.stock.StockUpdateRequest;
import com.sameh.onlinebookstore.model.book.BookAvailabilityRequest;
import com.sameh.onlinebookstore.model.book.BookRequestDTO;
import com.sameh.onlinebookstore.model.borrowingRequest.BorrowingRequestDTO;
import com.sameh.onlinebookstore.repository.BookRepository;
import com.sameh.onlinebookstore.repository.BorrowingRequestRepository;
import com.sameh.onlinebookstore.repository.CategoryRepository;
import com.sameh.onlinebookstore.repository.UserRepository;
import com.sameh.onlinebookstore.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BorrowingRequestRepository borrowingRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BorrowingRequestMapper borrowingRequestMapper;

    @Override
    public String addNewBook(BookRequestDTO bookRequestDTO) {
        log.warn("Admin want to add this book {}", bookRequestDTO);

        bookRepository.findByTitle(bookRequestDTO.getTitle())
                .ifPresent(existingBook -> {
                    throw new ConflictException("Book with Title: " + bookRequestDTO.getTitle() + " already exists");
                });

        categoryRepository.findById(bookRequestDTO.getCategoryId())
                .orElseThrow(() -> new RecordNotFoundException("Category with ID " + bookRequestDTO.getCategoryId() + " not found"));

        Book newBook = bookMapper.toEntity(bookRequestDTO);
        bookRepository.save(newBook);
        log.warn("This new Book has been added successfully {}", newBook);
        return "The Book Added Successfully";
    }

    @Override
    public String updateBook(Long id, BookRequestDTO bookRequestDTO) {
        log.warn("Admin want to update the book with id {}", id);
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("The book with ID "+ id +"does not exist"));
        Book updatedBook = bookMapper.toEntity(bookRequestDTO);
        updatedBook.setId(id);
        updatedBook.setPublishDate(existingBook.getPublishDate());

        if(updatedBook.equals(existingBook)){
            log.error("not found any updates the updateBook: {}, the existingBook:{}", updatedBook,existingBook);
            throw new NoUpdateFoundException("Not found Any update in the book details");
        }

        log.warn("the book before update: {}", existingBook);
        BeanUtils.copyProperties(updatedBook, existingBook, "id", "publishDate");
        bookRepository.save(existingBook);
        log.warn("The Book Updated Successfully, the book after update {}", existingBook);
        return "The Book Updated Successfully";
    }

    @Override
    public String setBookAvailability(Long id, BookAvailabilityRequest bookAvailabilityRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("The book with ID "+ id +"does not exist"));
        log.warn("Admin want to update availability for this book {}", book);

        if(book.isAvailable() == bookAvailabilityRequest.isAvailable()){
            throw new NoUpdateFoundException("Not found update in book availability");
        }

        book.setAvailable(bookAvailabilityRequest.isAvailable());
        bookRepository.save(book);
        log.warn("The Book After update Availability {}", book);
        return "The Book Availability is Updated Successfully";
    }

    @Override
    public String deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("The book with ID " + id + " does not exist"));
        log.warn("Admin want to delete this book {}", book);
        bookRepository.delete(book);
        log.warn("The Book Deleted Successfully");
        return "The Book Deleted Successfully";
    }

    @Override
    public String updateStock(Long id, StockUpdateRequest stockUpdateRequest) {
        Book book = bookRepository.findById(id)
                        .orElseThrow(() -> new RecordNotFoundException("The book with ID " + id + " does not exist"));
        log.warn("Admin want to update Stock Level for this Book {}", book);
        if(book.getStockLevel() == stockUpdateRequest.getStockLevel()){
            throw new NoUpdateFoundException("Not found update in book Stock Level");
        }
        book.setStockLevel(stockUpdateRequest.getStockLevel());
        bookRepository.save(book);
        log.warn("The Book After update Stock Level {}", book);
        return "Stock Level updated Successfully";
    }

    @Override
    public List<BookRequestDTO> getBooksByCategory(Long categoryId) {
        log.warn("Customer want to search by category with id {}", categoryId);

        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RecordNotFoundException("The Category with ID " + categoryId + " does not exist"));

        List<Book> books = bookRepository.findByCategoryId(categoryId);
        if (books.isEmpty()) {
            throw new RecordNotFoundException("There are no books in this category");
        }

        List<BookRequestDTO> bookDTOs = books.stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
        log.warn("These are the books in this category {}", bookDTOs);
        return bookDTOs;
    }


    @Override
    public List<BookRequestDTO> getBooksByCategory(String categoryName) {
        log.warn("Customer want to search by category with Name {}", categoryName);
        categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new RecordNotFoundException("The Category with Name " + categoryName + " does not exist"));

        List<Book> books = bookRepository.findByCategoryName(categoryName);
        if (books.isEmpty()) {
            throw new RecordNotFoundException("There are no books in this category");
        }

        List<BookRequestDTO> bookDTOs = books.stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
        log.warn("These are the books in this category {}", bookDTOs);
        return bookDTOs;
    }

    @Override
    public BookRequestDTO getBookDetailsById(Long id) {
        log.warn("Customer want to View Book Details with ID {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("The book with ID " + id + " does not exist"));
        BookRequestDTO bookRequestDTO = bookMapper.toDTO(book);
        log.warn("Book Details: {}", bookRequestDTO);
        return bookRequestDTO;
    }

    @Override
    public String requestBorrowing(Long bookId, Long userId) {
        log.warn("customer with id {}, request Borrowing book with id {}", userId, bookId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RecordNotFoundException("The book with ID " + bookId + " does not exist"));

        if(!book.isAvailable() || book.getStockLevel()==0){
            throw new RecordNotFoundException("The book with ID " + bookId + " Not available now");
        }

        borrowingRequestRepository.findByBookId(bookId)
                .ifPresent(existingBorrowingRequest -> {
                    if (existingBorrowingRequest.getUser().getId().equals(userId)) {
                        log.warn("This Borrowing request is already exist {}", existingBorrowingRequest);
                        throw new ConflictException("This Borrowing request is already Exist");
                    }
                });

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException( "This user with id " + userId + "doesn't exist"));

        BorrowingRequest newBorrowingRequest = new BorrowingRequest();
        newBorrowingRequest.setBook(book);
        newBorrowingRequest.setUser(user);
        borrowingRequestRepository.save(newBorrowingRequest);

        log.warn("The request you are submitting to borrow the book whose ID is {} has been sent to the admin and will be reviewed, and this is the request {}", bookId, newBorrowingRequest);
        return "The request you are submitting to borrow the book has been sent to the admin and will be reviewed.";
    }

    @Override
    public List<BorrowingRequestDTO> getAllBorrowingRequests() {
        log.warn("Admin want to view all requests");
        List<BorrowingRequest> borrowingRequests = borrowingRequestRepository.findAll();
        if(borrowingRequests.isEmpty()){
            log.error("There are no requests yet");
            throw new RecordNotFoundException("There are no requests yet");
        }
        List<BorrowingRequestDTO> borrowingRequestDTOS = borrowingRequests.stream()
                        .map(borrowingRequestMapper::toDTO)
                        .collect(Collectors.toList());
        log.warn("All requests: {}", borrowingRequestDTOS);
        return borrowingRequestDTOS;
    }
}
