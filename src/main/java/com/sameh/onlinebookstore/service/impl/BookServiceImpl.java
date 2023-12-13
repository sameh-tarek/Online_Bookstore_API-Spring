package com.sameh.onlinebookstore.service.impl;

import com.sameh.onlinebookstore.entity.Book;
import com.sameh.onlinebookstore.exception.ConflictException;
import com.sameh.onlinebookstore.exception.NoUpdateFoundException;
import com.sameh.onlinebookstore.exception.RecordNotFoundException;
import com.sameh.onlinebookstore.mapper.BookMapper;
import com.sameh.onlinebookstore.model.book.BookAvailabilityRequest;
import com.sameh.onlinebookstore.model.book.BookRequestDTO;
import com.sameh.onlinebookstore.repository.BookRepository;
import com.sameh.onlinebookstore.repository.CategoryRepository;
import com.sameh.onlinebookstore.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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
}
