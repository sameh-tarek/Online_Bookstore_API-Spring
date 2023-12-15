package com.sameh.onlinebookstore.mapper.impl;

import com.sameh.onlinebookstore.entity.Book;
import com.sameh.onlinebookstore.entity.BorrowingRequest;
import com.sameh.onlinebookstore.entity.User;
import com.sameh.onlinebookstore.entity.enums.Status;
import com.sameh.onlinebookstore.exception.RecordNotFoundException;
import com.sameh.onlinebookstore.mapper.BorrowingRequestMapper;
import com.sameh.onlinebookstore.model.borrowingRequest.BorrowingRequestDTO;
import com.sameh.onlinebookstore.repository.BookRepository;
import com.sameh.onlinebookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BorrowingRequestMapperImpl implements BorrowingRequestMapper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

   @Override
    public BorrowingRequestDTO toDTO(BorrowingRequest borrowingRequest) {
        BorrowingRequestDTO borrowingRequestDTO = new BorrowingRequestDTO(
                borrowingRequest.getId(),
                borrowingRequest.getUser().getId(),
                borrowingRequest.getBook().getId(),
                borrowingRequest.getUser().getUserName(),
                borrowingRequest.getBook().getTitle(),
                borrowingRequest.getBorrowingStatus().name(),
                borrowingRequest.getBorrowingDate(),
                borrowingRequest.getExpectedReturnDate()
        );
        return borrowingRequestDTO;
    }

    @Override
    public BorrowingRequest toEntity(BorrowingRequestDTO borrowingRequestDTO) {
        BorrowingRequest borrowingRequest = new BorrowingRequest();
        borrowingRequest.setId(borrowingRequestDTO.getId());
        User user = userRepository.findById(borrowingRequest.getId())
                        .orElseThrow( () -> new RecordNotFoundException("The user with id" + borrowingRequestDTO.getUserId() + "doesn't exists"));
        borrowingRequest.setUser(user);
        Book book = bookRepository.findById(borrowingRequestDTO.getBookId())
                        .orElseThrow(() -> new RecordNotFoundException("The book with id" + borrowingRequestDTO.getBookId() + "doesn't exists"));
        borrowingRequest.setBook(book);
        borrowingRequest.setBorrowingStatus(Status.valueOf(borrowingRequestDTO.getBorrowingStatus()));
        borrowingRequest.setBorrowingDate(borrowingRequestDTO.getBorrowingDate());
        borrowingRequest.setExpectedReturnDate(borrowingRequestDTO.getExpectedReturnDate());

        return borrowingRequest;
    }

}
