package com.sameh.onlinebookstore.service.impl;

import com.sameh.onlinebookstore.entity.Book;
import com.sameh.onlinebookstore.entity.BorrowingRequest;
import com.sameh.onlinebookstore.entity.Category;
import com.sameh.onlinebookstore.entity.User;
import com.sameh.onlinebookstore.entity.enums.Role;
import com.sameh.onlinebookstore.entity.enums.Status;
import com.sameh.onlinebookstore.exception.ConflictException;
import com.sameh.onlinebookstore.exception.NoUpdateFoundException;
import com.sameh.onlinebookstore.exception.RecordNotFoundException;
import com.sameh.onlinebookstore.mapper.BookMapper;
import com.sameh.onlinebookstore.mapper.BorrowingRequestMapper;
import com.sameh.onlinebookstore.model.book.BookAvailabilityRequest;
import com.sameh.onlinebookstore.model.book.BookRequestDTO;
import com.sameh.onlinebookstore.model.borrowingRequest.BorrowingRequestDTO;
import com.sameh.onlinebookstore.model.borrowingRequest.BorrowingRequestWrapperDTO;
import com.sameh.onlinebookstore.model.stock.StockUpdateRequest;
import com.sameh.onlinebookstore.repository.BookRepository;
import com.sameh.onlinebookstore.repository.BorrowingRequestRepository;
import com.sameh.onlinebookstore.repository.CategoryRepository;
import com.sameh.onlinebookstore.repository.UserRepository;
import com.sameh.onlinebookstore.security.user.UserDetailsImpl;
import com.sameh.onlinebookstore.utils.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BorrowingRequestRepository borrowingRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BorrowingRequestMapper borrowingRequestMapper;

    @InjectMocks
    private BookServiceImpl bookService;


    @Test
    public void addNewBookShouldAddNewBookSuccessfully() {
        // Arrange
        var category = new Category(1L, "Fiction", "bla bla");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        var bookRequestDTO = new BookRequestDTO("Sample Title", "Sample Author", "Sample Description", true, 1L);
        var book = new Book(1L, "Sample Title", "Sample Author", LocalDateTime.of(2023, 6, 15, 10, 30), "Sample Description", 10, true, category);
        when(bookMapper.toEntity(bookRequestDTO)).thenReturn(book);

        // Act
        String result = bookService.addNewBook(bookRequestDTO);

        // Assert
        assertThat(result).isEqualTo("The Book Added Successfully");
    }

    @Test
    public void addNewBookShouldThrowConflictExceptionIfBookWithThisTitleIsExist() {
        // Arrange
        var category = new Category(1L, "Fiction", "bla bla");
        var bookRequestDTO = new BookRequestDTO("Sample Title", "Sample Author", "Sample Description", true, 1L);
        var book = new Book(1L, "Sample Title", "Sample Author", LocalDateTime.of(2023, 6, 15, 10, 30), "Sample Description", 10, true, category);
        when(bookRepository.findByTitle("Sample Title")).thenReturn(Optional.of(book));

        // Act & Assert
        assertThatThrownBy(() -> bookService.addNewBook(bookRequestDTO))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Book with Title: Sample Title already exists");
    }

    @Test
    public void addNewBookShouldThrowRecordNotFoundExceptionIfCategoryDoesNotExist() {
        // Arrange
        var bookRequestDTO = new BookRequestDTO("Sample Title", "Sample Author", "Sample Description", true, 1L);

        // Act & Assert
        assertThatThrownBy(() -> bookService.addNewBook(bookRequestDTO))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("Category with ID 1 not found");
    }


    @Test
    public void updateBookShouldUpdateExistingBookSuccessfully() {
        // Arrange
        var category = new Category(1L, "Fiction", "bla bla");
        var book = new Book(1L, "Sample Title", "Sample Author", LocalDateTime.of(2023, 6, 15, 10, 30), "Sample Description", 10, true, category);
        var updatedBook = new Book(1L, "Updated Sample Title", "Sample Author", LocalDateTime.of(2023, 6, 15, 10, 30), "Sample Description", 10, true, category);
        var updatebookRequestDTO = new BookRequestDTO("Updated Sample Title", "Sample Author", "Sample Description", true, 1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toEntity(updatebookRequestDTO)).thenReturn(updatedBook);

        // Act
        String result = bookService.updateBook(1L, updatebookRequestDTO);

        // Assert
        assertThat(result).isEqualTo("The Book Updated Successfully");
    }

    @Test
    public void updateBookShouldThrowRecordNotFoundExceptionIfTheBookDoesNotExist() {
        // Arrange
        var updatebookRequestDTO = new BookRequestDTO("Updated Sample Title", "Sample Author", "Sample Description", true, 1L);

        // Act & Assert
        assertThatThrownBy(() -> bookService.updateBook(1L, updatebookRequestDTO))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("The book with ID 1 does not exist");
    }

    @Test
    public void updateBookShouldThrowNoUpdateFoundExceptionIfNoUpdates() {
        // Arrange
        var category = new Category(1L, "Fiction", "bla bla");
        var book = new Book(1L, "Sample Title", "Sample Author", LocalDateTime.of(2023, 6, 15, 10, 30), "Sample Description", 10, true, category);
        var updatedBookRequestDTO = new BookRequestDTO("Sample Title", "Sample Author", "Sample Description", true, 1L);
        var updateBook = new Book(1L, "Sample Title", "Sample Author", LocalDateTime.of(2023, 6, 15, 10, 30), "Sample Description", 10, true, category);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toEntity(updatedBookRequestDTO)).thenReturn(updateBook);


        // Act & Assert
        assertThatThrownBy(() -> bookService.updateBook(1L, updatedBookRequestDTO))
                .isInstanceOf(NoUpdateFoundException.class)
                .hasMessageContaining("Not found Any update in the book details");
    }

    @Test
    public void setBookAvailabilityShouldSuccess() {
        // Arrange
        var category = new Category(1L, "Fiction", "bla bla");
        var book = new Book(1L, "Sample Title", "Sample Author", LocalDateTime.of(2023, 6, 15, 10, 30), "Sample Description", 10, true, category);
        BookAvailabilityRequest bookAvailabilityRequest = new BookAvailabilityRequest();
        bookAvailabilityRequest.setAvailable(false);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Act
        String result = bookService.setBookAvailability(1L, bookAvailabilityRequest);

        // Assert
        assertThat(result).isEqualTo("The Book Availability is Updated Successfully");
    }

    @Test
    public void setBookAvailabilityShouldThrowRecordNotFoundExceptionIfTheBookDoesNotExist() {
        // Arrange
        BookAvailabilityRequest bookAvailabilityRequest = new BookAvailabilityRequest();
        bookAvailabilityRequest.setAvailable(false);

        // Act & Assert
        assertThatThrownBy(() -> bookService.setBookAvailability(1L, bookAvailabilityRequest))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("The book with ID 1 does not exist");
    }

    @Test
    public void setBookAvailabilityShouldThrowNoUpdateFoundExceptionIfNoUpdates() {
        // Arrange
        var category = new Category(1L, "Fiction", "bla bla");
        var book = new Book(1L, "Sample Title", "Sample Author", LocalDateTime.of(2023, 6, 15, 10, 30), "Sample Description", 10, true, category);
        BookAvailabilityRequest bookAvailabilityRequest = new BookAvailabilityRequest();
        bookAvailabilityRequest.setAvailable(true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));


        // Act & Assert
        assertThatThrownBy(() -> bookService.setBookAvailability(1L, bookAvailabilityRequest))
                .isInstanceOf(NoUpdateFoundException.class)
                .hasMessageContaining("Not found update in book availability");
    }

    @Test
    public void deleteBookShouldDeleteSuccessfully() {
        // Arrange
        var category = new Category(1L, "Fiction", "bla bla");
        var book = new Book(1L, "Sample Title", "Sample Author", LocalDateTime.of(2023, 6, 15, 10, 30), "Sample Description", 10, true, category);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Act
        String result = bookService.deleteBook(1L);

        // Assert
        assertThat(result).isEqualTo("The Book Deleted Successfully");
    }

    @Test
    public void deleteBookShouldThrowRecordNotFoundExceptionIfBookDoesNotExist() {
        // Act & Assert
        assertThatThrownBy(() -> bookService.deleteBook(1L))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("The book with ID 1 does not exist");
    }

    @Test
    public void updateStockShouldSuccess() {
        // Arrange
        var category = new Category(1L, "Fiction", "bla bla");
        var book = new Book(1L, "Sample Title", "Sample Author", LocalDateTime.of(2023, 6, 15, 10, 30), "Sample Description", 10, true, category);
        StockUpdateRequest stockUpdateRequest = new StockUpdateRequest();
        stockUpdateRequest.setStockLevel(20);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Act
        String result = bookService.updateStock(1L, stockUpdateRequest);

        // Assert
        assertThat(result).isEqualTo("Stock Level updated Successfully");
    }

    @Test
    public void updateStockShouldThrowRecordNotFoundExceptionIfBookDoesNotExist() {
        // Arrange
        StockUpdateRequest stockUpdateRequest = new StockUpdateRequest();
        stockUpdateRequest.setStockLevel(20);

        // Act & Assert
        assertThatThrownBy(() -> bookService.updateStock(1L, stockUpdateRequest))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("The book with ID 1 does not exist");
    }

    @Test
    public void updateStockShouldThrowNoUpdateFoundExceptionIfNoUpdates() {
        // Arrange
        var category = new Category(1L, "Fiction", "bla bla");
        var book = new Book(1L, "Sample Title", "Sample Author", LocalDateTime.of(2023, 6, 15, 10, 30), "Sample Description", 10, true, category);
        StockUpdateRequest stockUpdateRequest = new StockUpdateRequest();
        stockUpdateRequest.setStockLevel(10);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Act & Assert
        assertThatThrownBy(() -> bookService.updateStock(1L, stockUpdateRequest))
                .isInstanceOf(NoUpdateFoundException.class)
                .hasMessageContaining("Not found update in book Stock Level");
    }

    @Test
    public void getBooksByCategoryIdShouldReturnBooksDTOs() {
        // Arrange
        var category = new Category(1L, "Fiction", "bla bla");
        var book = new Book(1L, "Sample Title", "Sample Author", LocalDateTime.of(2023, 6, 15, 10, 30), "Sample Description", 10, true, category);
        var bookRequestDTO = new BookRequestDTO("Sample Title", "Sample Author", "Sample Description", true, 1L);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(bookRepository.findByCategoryId(1L)).thenReturn(List.of(book));
        when(bookMapper.toDTO(book)).thenReturn(bookRequestDTO);

        // Act
        List<BookRequestDTO> result = bookService.getBooksByCategory(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(bookRequestDTO);
    }

    @Test
    public void getBooksByCategoryIdShouldThrowRecordNotFoundExceptionIfCategoryDoesNotExist() {
        // Act & Assert
        assertThatThrownBy(() -> bookService.getBooksByCategory(1L))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("The Category with ID 1 does not exist");
    }

    @Test
    public void getBooksByCategoryIdShouldThrowRecordNotFoundExceptionIfNoBooksAtThisCategory() {
        // Arrange
        var category = new Category(1L, "Fiction", "bla bla");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // Act & Assert
        assertThatThrownBy(() -> bookService.getBooksByCategory(1L))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("There are no books in this category");
    }

    @Test
    public void getBooksByCategoryNameShouldReturnBooksDTOs() {
        // Arrange
        var category = new Category(1L, "Fiction", "bla bla");
        var book = new Book(1L, "Sample Title", "Sample Author", LocalDateTime.of(2023, 6, 15, 10, 30), "Sample Description", 10, true, category);
        var bookRequestDTO = new BookRequestDTO("Sample Title", "Sample Author", "Sample Description", true, 1L);
        when(categoryRepository.findByName(category.getName())).thenReturn(Optional.of(category));
        when(bookRepository.findByCategoryName(category.getName())).thenReturn(List.of(book));
        when(bookMapper.toDTO(book)).thenReturn(bookRequestDTO);

        // Act
        List<BookRequestDTO> result = bookService.getBooksByCategory(category.getName());

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(bookRequestDTO);
    }

    @Test
    public void getBooksByCategoryNameShouldThrowRecordNotFoundExceptionIfCategoryDoesNotExist() {
        // Act & Assert
        assertThatThrownBy(() -> bookService.getBooksByCategory("bla"))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("The Category with Name bla does not exist");
    }

    @Test
    public void getBooksByCategoryNameShouldThrowRecordNotFoundExceptionIfNoBooksAtThisCategory() {
        // Arrange
        var category = new Category(1L, "Fiction", "bla bla");
        when(categoryRepository.findByName(category.getName())).thenReturn(Optional.of(category));

        // Act & Assert
        assertThatThrownBy(() -> bookService.getBooksByCategory(category.getName()))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("There are no books in this category");
    }

    @Test
    public void getBookDetailsByIdShouldReturnBookDTO(){
        // Arrange
        var category = new Category(1L, "Fiction", "bla bla");
        var book = new Book(1L, "Sample Title", "Sample Author", LocalDateTime.of(2023, 6, 15, 10, 30), "Sample Description", 10, true, category);
        var bookRequestDTO = new BookRequestDTO("Sample Title", "Sample Author", "Sample Description", true, 1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDTO(book)).thenReturn(bookRequestDTO);

        // Act
        BookRequestDTO bookRequestDTO1 = bookService.getBookDetailsById(1L);

        // Assert
        assertThat(bookRequestDTO1).isNotNull();
        assertThat(bookRequestDTO1).isEqualTo(bookRequestDTO);
    }

    @Test
    public void getBookDetailsByIdShouldThrowRecordNotFoundExceptionIfBookDoesNotExist(){
        // Act & Assert
        assertThatThrownBy(() -> bookService.getBookDetailsById(1L))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("The book with ID 1 does not exist");
    }

    @Test
    public void requestBorrowingShouldSuccess(){
        // Arrange
        User user = new User(1L, "username", "user@example.com", "password", Role.CUSTOMER, true);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        Authentication authentication = new UsernamePasswordAuthenticationToken("user@example.com", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var category = new Category(1L, "Fiction", "bla bla");
        var book = new Book(1L, "Sample Title", "Sample Author", LocalDateTime.of(2023, 6, 15, 10, 30), "Sample Description", 10, true, category);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        String result = bookService.requestBorrowing(1L);

        // Assert
        assertThat(result).isEqualTo("The request you are submitting to borrow the book has been sent to the admin and will be reviewed.");
    }

    @Test
    public void requestBorrowingShouldThrowRecordNotFoundExceptionIfTheBookDoesNotExist(){
        // Arrange
        User user = new User(1L, "username", "user@example.com", "password", Role.CUSTOMER, true);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        Authentication authentication = new UsernamePasswordAuthenticationToken("user@example.com", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Act & Assert
        assertThatThrownBy(() -> bookService.requestBorrowing(1L))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("The book with ID 1 does not exist");
    }

    @Test
    public void requestBorrowingShouldThrowRecordNotFoundExceptionIfBookNotAvailable(){
        // Arrange
        User user = new User(1L, "username", "user@example.com", "password", Role.CUSTOMER, true);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        Authentication authentication = new UsernamePasswordAuthenticationToken("user@example.com", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var category = new Category(1L, "Fiction", "bla bla");
        var book = new Book(1L, "Sample Title", "Sample Author", LocalDateTime.of(2023, 6, 15, 10, 30), "Sample Description", 10, false, category);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Act & Assert
        assertThatThrownBy(() -> bookService.requestBorrowing(1L))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("The book with ID 1 Not available now");
    }

    @Test
    public void requestBorrowingShouldThrowRecordNotFoundExceptionIfBookAvailableAndStockLevelIsZero(){
        // Arrange
        User user = new User(1L, "username", "user@example.com", "password", Role.CUSTOMER, true);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        Authentication authentication = new UsernamePasswordAuthenticationToken("user@example.com", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var category = new Category(1L, "Fiction", "bla bla");
        var book = new Book(1L, "Sample Title", "Sample Author", LocalDateTime.of(2023, 6, 15, 10, 30), "Sample Description", 0, true, category);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Act & Assert
        assertThatThrownBy(() -> bookService.requestBorrowing(1L))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("The book with ID 1 Not available now");
    }

    @Test
    public void requestBorrowingShouldThrowConflictExceptionIfBorrowingRequestIsExistWithThisUser(){
        // Arrange
        User user = new User(1L, "username", "user@example.com", "password", Role.CUSTOMER, true);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        Authentication authentication = new UsernamePasswordAuthenticationToken("user@example.com", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var category = new Category(1L, "Fiction", "bla bla");
        var book = new Book(1L, "Sample Title", "Sample Author", LocalDateTime.of(2023, 6, 15, 10, 30), "Sample Description", 10, true, category);
        BorrowingRequest borrowingRequest = new BorrowingRequest(1L, user, book, Status.pending, null, null);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(borrowingRequestRepository.findByBookId(1L)).thenReturn(Optional.of(borrowingRequest));

        // Act & Assert
        assertThatThrownBy(() -> bookService.requestBorrowing(1L))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("This Borrowing request is already Exist");
    }

    @Test
    public void requestBorrowingShouldSuccessIfBorrowingRequestIsExistWithOtherUser(){
        // Arrange
        User user = new User(1L, "username", "user@example.com", "password", Role.CUSTOMER, true);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        Authentication authentication = new UsernamePasswordAuthenticationToken("user@example.com", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var category = new Category(1L, "Fiction", "bla bla");
        var book = new Book(1L, "Sample Title", "Sample Author", LocalDateTime.of(2023, 6, 15, 10, 30), "Sample Description", 10, true, category);
        User requestUser = new User(3L, "X-username", "X-user@example.com", "password", Role.CUSTOMER, true);
        BorrowingRequest borrowingRequest = new BorrowingRequest(1L, requestUser, book, Status.pending, null, null);
        when(borrowingRequestRepository.findByBookId(1L)).thenReturn(Optional.of(borrowingRequest));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        String result = bookService.requestBorrowing(1L);

        // Assert
        assertThat(result).isEqualTo("The request you are submitting to borrow the book has been sent to the admin and will be reviewed.");
    }

    @Test
    public void getAllBorrowingRequestsShouldReturnBorrowingRequestsDTOs() {
        // Arrange
        User user = new User(1L, "username", "user@example.com", "password", Role.CUSTOMER, true);
        var category = new Category(1L, "Fiction", "bla bla");
        var book = new Book(1L, "Sample Title", "Sample Author", LocalDateTime.of(2023, 6, 15, 10, 30), "Sample Description", 10, true, category);
        BorrowingRequest borrowingRequest = new BorrowingRequest(1L, user, book, Status.pending, null, null);
        BorrowingRequestDTO borrowingRequestDTO = new BorrowingRequestDTO(1L, 1L, 1L, "username", "Sample Title", "pending", null, null);
        List<BorrowingRequest> borrowingRequests = List.of(borrowingRequest);
        when(borrowingRequestRepository.findAll()).thenReturn(borrowingRequests);
        when(borrowingRequestMapper.toDTO(borrowingRequest)).thenReturn(borrowingRequestDTO);

        // Act
        List<BorrowingRequestDTO> result = bookService.getAllBorrowingRequests();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(borrowingRequestDTO);
    }

    @Test
    public void getAllBorrowingRequestsShouldThrowRecordNotFoundExceptionIfNoRequests(){
        // Act & Assert
        assertThatThrownBy(() -> bookService.getAllBorrowingRequests())
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("There are no requests yet");
    }

    @Test
    public void updateBorrowingStatusShouldUpdateStatusToApprovedSuccessfully() {
        // Arrange

        Status newStatus = Status.approved;
        BorrowingRequest borrowingRequest = new BorrowingRequest(1L, null, null, Status.pending, null, null);
        when(borrowingRequestRepository.findById(1L)).thenReturn(Optional.of(borrowingRequest));

        // Act
        String result = bookService.updateBorrowingStatus(1L, newStatus);

        // Assert
        assertThat(result).isEqualTo("The Borrowing status is updated successfully");
        assertThat(borrowingRequest.getBorrowingStatus()).isEqualTo(newStatus);
    }

    @Test
    public void updateBorrowingStatusShouldUpdateStatusToRejectedSuccessfully() {
        // Arrange

        Status newStatus = Status.rejected;
        BorrowingRequest borrowingRequest = new BorrowingRequest(1L, null, null, Status.pending, null, null);
        when(borrowingRequestRepository.findById(1L)).thenReturn(Optional.of(borrowingRequest));

        // Act
        String result = bookService.updateBorrowingStatus(1L, newStatus);

        // Assert
        assertThat(result).isEqualTo("The Borrowing status is updated successfully");
        assertThat(borrowingRequest.getBorrowingStatus()).isEqualTo(newStatus);
    }

    @Test
    public void updateBorrowingStatusShouldThrowRecordNotFoundExceptionIfRequestDoesNotExist() {
        // Arrange
        Long requestId = 1L;
        Status newStatus = Status.approved;
        when(borrowingRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> bookService.updateBorrowingStatus(requestId, newStatus))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("This Borrowing request with id" + requestId + " doesn't exist.");
    }

    @Test
    public void updateBorrowingStatusShouldThrowNoUpdateFoundExceptionIfStatusNotChanged() {
        // Arrange
        Long requestId = 1L;
        Status newStatus = Status.pending;
        BorrowingRequest borrowingRequest = new BorrowingRequest(requestId, null, null, Status.pending, null, null);
        when(borrowingRequestRepository.findById(requestId)).thenReturn(Optional.of(borrowingRequest));

        // Act & Assert
        assertThatThrownBy(() -> bookService.updateBorrowingStatus(requestId, newStatus))
                .isInstanceOf(NoUpdateFoundException.class)
                .hasMessageContaining("There is no update in the status");
    }

    @Test
    public void getCustomerBorrowingRequestsShouldReturnRequests() {
        // Arrange
        User user = new User(1L, "username", "user@example.com", "password", Role.CUSTOMER, true);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        Authentication authentication = new UsernamePasswordAuthenticationToken("user@example.com", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        BorrowingRequest borrowingRequest = new BorrowingRequest();
        BorrowingRequestWrapperDTO borrowingRequestWrapperDTO = new BorrowingRequestWrapperDTO();
        when(borrowingRequestRepository.findByUserId(1L)).thenReturn(List.of(borrowingRequest));
        when(borrowingRequestMapper.toWrapperDTO(borrowingRequest)).thenReturn(borrowingRequestWrapperDTO);

        // Act
        List<BorrowingRequestWrapperDTO> result = bookService.getCustomerBorrowingRequests();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(borrowingRequestWrapperDTO);
    }

    @Test
    public void getCustomerBorrowingRequestsShouldThrowRecordNotFoundExceptionIfNoRequestsFound() {
        // Arrange
        User user = new User(1L, "username", "user@example.com", "password", Role.CUSTOMER, true);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        Authentication authentication = new UsernamePasswordAuthenticationToken("user@example.com", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(borrowingRequestRepository.findByUserId(1L)).thenReturn(List.of());

        // Act & Assert
        assertThatThrownBy(() -> bookService.getCustomerBorrowingRequests())
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("No borrowing requests found for user with ID " + 1L);
    }

}