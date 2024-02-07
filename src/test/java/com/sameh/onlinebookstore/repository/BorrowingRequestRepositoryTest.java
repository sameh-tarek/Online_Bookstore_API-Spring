package com.sameh.onlinebookstore.repository;

import com.sameh.onlinebookstore.entity.Book;
import com.sameh.onlinebookstore.entity.BorrowingRequest;
import com.sameh.onlinebookstore.entity.User;
import com.sameh.onlinebookstore.entity.enums.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BorrowingRequestRepositoryTest {

    @Autowired
    private BorrowingRequestRepository borrowingRequestRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Book book;
    private BorrowingRequest borrowingRequest;

    @AfterEach
    public void tearDown(){
        borrowingRequestRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();
    }

    @BeforeEach
    public void setUp(){
        user = new User();
        user.setUserName("john_doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("securePassword");
        user.setEnabled(true);
        userRepository.save(user);

        book = new Book();
        book.setTitle("The Great Gatsby");
        book.setAuthor("F. Scott Fitzgerald");
        bookRepository.save(book);

        borrowingRequest = new BorrowingRequest();
        borrowingRequest.setUser(user);
        borrowingRequest.setBook(book);
        borrowingRequest.setBorrowingStatus(Status.pending);
        borrowingRequest.setBorrowingDate(LocalDateTime.now());
        borrowingRequest.setExpectedReturnDate(LocalDateTime.now().plusDays(14)); // Assuming a 14-day borrowing period
        borrowingRequestRepository.save(borrowingRequest);
    }


    @DisplayName("Test Find By Book Id")
    @Test
    public void testFindByBookId(){
        BorrowingRequest borrowingRequest = borrowingRequestRepository.findByBookId(book.getId())
                .orElse(null);

        assertNotNull(borrowingRequest);
    }

    @DisplayName("Test Find By User Id")
    @Test
    public void testFindByUserId(){
        List<BorrowingRequest> borrowingRequests = borrowingRequestRepository.findByUserId(user.getId());

        assertEquals(1, borrowingRequests.size(), "size should be equals");
    }

}