package com.sameh.onlinebookstore.repository;

import com.sameh.onlinebookstore.entity.Book;
import com.sameh.onlinebookstore.entity.Category;
import com.sameh.onlinebookstore.exception.RecordNotFoundException;
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
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Book book;
    private Category category;

    @AfterEach
    public void tearDown() {
        // Delete specific records created in setUp
        bookRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        category = new Category();
        category.setName("Fiction");
        categoryRepository.save(category);

        book = new Book();
        book.setTitle("The Great Gatsby");
        book.setAuthor("F. Scott Fitzgerald");
        book.setPublishDate(LocalDateTime.of(1925, 4, 10, 0, 0));
        book.setDescription("A classic novel set in the roaring twenties.");
        book.setStockLevel(1);
        book.setAvailable(true);
        book.setCategory(category);
        bookRepository.save(book);
    }

    @DisplayName("Find By Title")
    @Test
    public void testFindByTitle() {
        Book foundBook = bookRepository.findByTitle(book.getTitle())
                .orElseThrow(() -> new RecordNotFoundException("This Book Not Found"));

        assertNotNull(foundBook);
    }

    @DisplayName("Find By Category Id")
    @Test
    public void testFindByCategoryId() {
        List<Book> books = bookRepository.findByCategoryId(category.getId());

        assertEquals(1, books.size(), "The size should be 1");
    }

    @DisplayName("Find By Category Name")
    @Test
    public void testFindByCategoryName(){
        List<Book> books = bookRepository.findByCategoryName(category.getName());
        assertEquals(1, books.size(), "The size should be 1");
    }
}