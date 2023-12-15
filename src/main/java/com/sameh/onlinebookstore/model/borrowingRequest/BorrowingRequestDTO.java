package com.sameh.onlinebookstore.model.borrowingRequest;

import com.sameh.onlinebookstore.entity.Book;
import com.sameh.onlinebookstore.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowingRequestDTO {
    private Long id;
    private Long userId;
    private Long bookId;
    private String userName;
    private String bookTitle;
    private String borrowingStatus;
    private LocalDateTime borrowingDate;
    private LocalDateTime expectedReturnDate;
}