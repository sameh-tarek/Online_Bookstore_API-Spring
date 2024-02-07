package com.sameh.onlinebookstore.service.impl;

import com.sameh.onlinebookstore.entity.Book;
import com.sameh.onlinebookstore.entity.Category;
import com.sameh.onlinebookstore.exception.ConflictException;
import com.sameh.onlinebookstore.exception.NoUpdateFoundException;
import com.sameh.onlinebookstore.exception.RecordNotFoundException;
import com.sameh.onlinebookstore.mapper.BookMapper;
import com.sameh.onlinebookstore.mapper.BorrowingRequestMapper;
import com.sameh.onlinebookstore.model.book.BookAvailabilityRequest;
import com.sameh.onlinebookstore.model.book.BookRequestDTO;
import com.sameh.onlinebookstore.model.stock.StockUpdateRequest;
import com.sameh.onlinebookstore.repository.BookRepository;
import com.sameh.onlinebookstore.repository.BorrowingRequestRepository;
import com.sameh.onlinebookstore.repository.CategoryRepository;
import com.sameh.onlinebookstore.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
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
    private CategoryRepository categoryRepository ;
    @Mock
    private BorrowingRequestRepository borrowingRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BorrowingRequestMapper borrowingRequestMapper;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    public void addNewBookShouldAddNewBookSuccessfully(){
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
    public void addNewBookShouldThrowConflictExceptionIfBookWithThisTitleIsExist(){
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
    public void updateBookShouldUpdateExistingBookSuccessfully(){
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
    public void updateBookShouldThrowRecordNotFoundExceptionIfTheBookDoesNotExist(){
        // Arrange
        var updatebookRequestDTO = new BookRequestDTO("Updated Sample Title", "Sample Author", "Sample Description", true, 1L);

        // Act & Assert
        assertThatThrownBy(() -> bookService.updateBook(1L, updatebookRequestDTO))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("The book with ID 1 does not exist");
    }

    @Test
    public void updateBookShouldThrowNoUpdateFoundExceptionIfNoUpdates(){
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
    public void setBookAvailabilityShouldSuccess(){
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
    public void setBookAvailabilityShouldThrowRecordNotFoundExceptionIfTheBookDoesNotExist(){
        // Arrange
        BookAvailabilityRequest bookAvailabilityRequest = new BookAvailabilityRequest();
        bookAvailabilityRequest.setAvailable(false);

        // Act & Assert
        assertThatThrownBy(() -> bookService.setBookAvailability(1L, bookAvailabilityRequest))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("The book with ID 1 does not exist");
    }

    @Test
    public void setBookAvailabilityShouldThrowNoUpdateFoundExceptionIfNoUpdates(){
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
    public void deleteBookShouldDeleteSuccessfully(){
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
    public void deleteBookShouldThrowRecordNotFoundExceptionIfBookDoesNotExist(){
        // Act & Assert
        assertThatThrownBy(() -> bookService.deleteBook(1L))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("The book with ID 1 does not exist");
    }

    @Test
    public void updateStockShouldSuccess(){
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
    public void updateStockShouldThrowRecordNotFoundExceptionIfBookDoesNotExist(){
        // Arrange
        StockUpdateRequest stockUpdateRequest = new StockUpdateRequest();
        stockUpdateRequest.setStockLevel(20);

        // Act & Assert
        assertThatThrownBy(() -> bookService.updateStock(1L, stockUpdateRequest))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("The book with ID 1 does not exist");
    }

    @Test
    public void updateStockShouldThrowNoUpdateFoundExceptionIfNoUpdates(){
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


}