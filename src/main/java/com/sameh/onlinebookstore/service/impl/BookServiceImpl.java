package com.sameh.onlinebookstore.service.impl;

import com.sameh.onlinebookstore.entity.Book;
import com.sameh.onlinebookstore.entity.BorrowingRequest;
import com.sameh.onlinebookstore.entity.User;
import com.sameh.onlinebookstore.entity.enums.Status;
import com.sameh.onlinebookstore.exception.ConflictException;
import com.sameh.onlinebookstore.exception.NoUpdateFoundException;
import com.sameh.onlinebookstore.exception.RecordNotFoundException;
import com.sameh.onlinebookstore.mapper.BookMapper;
import com.sameh.onlinebookstore.mapper.BorrowingRequestMapper;
import com.sameh.onlinebookstore.model.borrowingRequest.BorrowingRequestWrapperDTO;
import com.sameh.onlinebookstore.model.stock.StockUpdateRequest;
import com.sameh.onlinebookstore.model.book.BookAvailabilityRequest;
import com.sameh.onlinebookstore.model.book.BookRequestDTO;
import com.sameh.onlinebookstore.model.borrowingRequest.BorrowingRequestDTO;
import com.sameh.onlinebookstore.repository.BookRepository;
import com.sameh.onlinebookstore.repository.BorrowingRequestRepository;
import com.sameh.onlinebookstore.repository.CategoryRepository;
import com.sameh.onlinebookstore.repository.UserRepository;
import com.sameh.onlinebookstore.service.BookService;
import com.sameh.onlinebookstore.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        log.info("Admin want to add this book {}", bookRequestDTO);

        bookRepository.findByTitle(bookRequestDTO.getTitle())
                .ifPresent(existingBook -> {
                    log.error("Book with Title: {} already exists", bookRequestDTO.getTitle() );
                    throw new ConflictException("Book with Title: " + bookRequestDTO.getTitle() + " already exists");
                });

        categoryRepository.findById(bookRequestDTO.getCategoryId())
                .orElseThrow(() -> new RecordNotFoundException("Category with ID " + bookRequestDTO.getCategoryId() + " not found"));

        Book newBook = bookMapper.toEntity(bookRequestDTO);
        bookRepository.save(newBook);
        log.info("This new Book has been added successfully {}", newBook);
        return "The Book Added Successfully";
    }

    @Override
    public String updateBook(Long id, BookRequestDTO bookRequestDTO) {
        log.info("Admin want to update the book with id {}", id);
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("The book with ID "+ id +" does not exist"));
        Book updatedBook = bookMapper.toEntity(bookRequestDTO);
        updatedBook.setId(id);
        updatedBook.setPublishDate(existingBook.getPublishDate());

        if(updatedBook.equals(existingBook)){
            log.error("not found any updates the updateBook: {}, the existingBook:{}", updatedBook,existingBook);
            throw new NoUpdateFoundException("Not found Any update in the book details");
        }

        log.info("the book before update: {}", existingBook);
        BeanUtils.copyProperties(updatedBook, existingBook, "id", "publishDate");
        bookRepository.save(existingBook);
        log.info("The Book Updated Successfully, the book after update {}", existingBook);
        return "The Book Updated Successfully";
    }

    @Override
    public String setBookAvailability(Long id, BookAvailabilityRequest bookAvailabilityRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("The book with ID "+ id +" does not exist"));
        log.info("Admin want to update availability for this book {}", book);

        if(book.isAvailable() == bookAvailabilityRequest.isAvailable()){
            log.error("Not found update in book availability");
            throw new NoUpdateFoundException("Not found update in book availability");
        }

        book.setAvailable(bookAvailabilityRequest.isAvailable());
        bookRepository.save(book);
        log.info("The Book After update Availability {}", book);
        return "The Book Availability is Updated Successfully";
    }

    @Override
    public String deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("The book with ID " + id + " does not exist"));
        log.info("Admin want to delete this book {}", book);
        bookRepository.delete(book);
        log.info("The Book Deleted Successfully");
        return "The Book Deleted Successfully";
    }

    @Override
    public String updateStock(Long id, StockUpdateRequest stockUpdateRequest) {
        Book book = bookRepository.findById(id)
                        .orElseThrow(() -> new RecordNotFoundException("The book with ID " + id + " does not exist"));
        log.info("Admin want to update Stock Level for this Book {}", book);
        if(book.getStockLevel() == stockUpdateRequest.getStockLevel()){
            log.error("Not found update in book Stock Level");
            throw new NoUpdateFoundException("Not found update in book Stock Level");
        }
        book.setStockLevel(stockUpdateRequest.getStockLevel());
        bookRepository.save(book);
        log.info("The Book After update Stock Level {}", book);
        return "Stock Level updated Successfully";
    }

    @Override
    public List<BookRequestDTO> getBooksByCategory(Long categoryId) {
        log.info("Customer want to search by category with id {}", categoryId);

        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RecordNotFoundException("The Category with ID " + categoryId + " does not exist"));

        List<Book> books = bookRepository.findByCategoryId(categoryId);
        if (books.isEmpty()) {
            log.error("There are no books in this category with id {}", categoryId);
            throw new RecordNotFoundException("There are no books in this category");
        }

        List<BookRequestDTO> bookDTOs = books.stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
        log.info("These are the books in this category {}", bookDTOs);
        return bookDTOs;
    }


    @Override
    public List<BookRequestDTO> getBooksByCategory(String categoryName) {
        log.info("Customer want to search by category with Name {}", categoryName);
        categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new RecordNotFoundException("The Category with Name " + categoryName + " does not exist"));

        List<Book> books = bookRepository.findByCategoryName(categoryName);
        if (books.isEmpty()) {
            log.error("There are no books in this category {}", categoryName);
            throw new RecordNotFoundException("There are no books in this category");
        }

        List<BookRequestDTO> bookDTOs = books.stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
        log.info("These are the books in this category {}", bookDTOs);
        return bookDTOs;
    }

    @Override
    public BookRequestDTO getBookDetailsById(Long id) {
        log.info("Customer want to View Book Details with ID {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("The book with ID " + id + " does not exist"));
        BookRequestDTO bookRequestDTO = bookMapper.toDTO(book);
        log.info("Book Details: {}", bookRequestDTO);
        return bookRequestDTO;
    }

    @Override
    public String requestBorrowing(Long bookId) {
        String userEmail = SecurityUtils.getCurrentUserEmail();
        Long userId = getUserId(userEmail);
        log.info("customer with id {}, request Borrowing book with id {}", userId, bookId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RecordNotFoundException("The book with ID " + bookId + " does not exist"));

        if(!book.isAvailable() || book.getStockLevel()==0){
            log.error("The book with ID {} Not available now", bookId);
            throw new RecordNotFoundException("The book with ID " + bookId + " Not available now");
        }

        borrowingRequestRepository.findByBookId(bookId)
                .ifPresent(existingBorrowingRequest -> {
                    if (existingBorrowingRequest.getUser().getId().equals(userId)) {
                        log.error("This Borrowing request is already exist {}", existingBorrowingRequest);
                        throw new ConflictException("This Borrowing request is already Exist");
                    }
                });

        User user = userRepository.findById(userId)
                .orElse(null);

        BorrowingRequest newBorrowingRequest = new BorrowingRequest();
        newBorrowingRequest.setBook(book);
        newBorrowingRequest.setUser(user);
        borrowingRequestRepository.save(newBorrowingRequest);

        log.info("The request you are submitting to borrow the book whose ID is {} has been sent to the admin and will be reviewed, and this is the request {}", bookId, newBorrowingRequest);
        return "The request you are submitting to borrow the book has been sent to the admin and will be reviewed.";
    }

    public Long getUserId(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        Long userId = user.getId();
        log.info("authenticated user id {}", userId);
        return userId;
    }

    @Override
    public List<BorrowingRequestDTO> getAllBorrowingRequests() {
        log.info("Admin want to view all requests");
        List<BorrowingRequest> borrowingRequests = borrowingRequestRepository.findAll();
        if(borrowingRequests.isEmpty()){
            log.error("There are no requests yet");
            throw new RecordNotFoundException("There are no requests yet");
        }
        List<BorrowingRequestDTO> borrowingRequestDTOS = borrowingRequests.stream()
                        .map(borrowingRequestMapper::toDTO)
                        .collect(Collectors.toList());
        log.info("All requests: {}", borrowingRequestDTOS);
        return borrowingRequestDTOS;
    }

    @Override
    public String updateBorrowingStatus(Long requestId, Status newStatus) {
        log.info("Admin want to update Borrowing Request status Status");
        BorrowingRequest borrowingRequest = borrowingRequestRepository.findById(requestId)
                .orElseThrow(() -> new RecordNotFoundException("This Borrowing request with id" + requestId + " doesn't exist."));
        log.info("The old status is {}, The new status is {}",borrowingRequest.getBorrowingStatus(),newStatus);
        if(borrowingRequest.getBorrowingStatus() == newStatus){
            log.error("There is no update in the status");
            throw new NoUpdateFoundException("There is no update in the status");
        }
        if(newStatus.equals(Status.approved)){
            LocalDateTime borrowingDate = LocalDateTime.now().plusDays(1);
            LocalDateTime expectedReturnDate = LocalDateTime.now().plusDays(12);
            borrowingRequest.setBorrowingDate(borrowingDate);
            borrowingRequest.setExpectedReturnDate(expectedReturnDate);
            log.info("This Borrowing request is approved {}", borrowingRequest);
            log.info("The Borrowing Date is {}, The Expected Return Date is {}", borrowingDate, expectedReturnDate);
        }
        borrowingRequest.setBorrowingStatus(newStatus);
        borrowingRequestRepository.save(borrowingRequest);
        log.info("The Request after update is {}", borrowingRequest);
        return "The Borrowing status is updated successfully";
    }

    @Override
    public List<BorrowingRequestWrapperDTO> getCustomerBorrowingRequests() {
        String userEmail = SecurityUtils.getCurrentUserEmail();
        Long userId = getUserId(userEmail);
        log.info("customer with id {} want to view his Borrowing requests", userId);
        List<BorrowingRequest> borrowingRequests = borrowingRequestRepository.findByUserId(userId);
        if (borrowingRequests.isEmpty()) {
            log.error("No borrowing requests found for user with ID {}", userId);
            throw new RecordNotFoundException("No borrowing requests found for user with ID " + userId);
        }
        List<BorrowingRequestWrapperDTO> customerBorrowingRequests = borrowingRequests.stream()
                .map(borrowingRequestMapper::toWrapperDTO)
                .collect(Collectors.toList());
        log.info("There are the borrowing requests {}", customerBorrowingRequests);
        return customerBorrowingRequests;
    }
}
