package com.sameh.onlinebookstore.model.borrowingRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowingRequestWrapperDTO {
    private String bookTitle;
    private String borrowingStatus;
    private LocalDateTime borrowingDate;
    private LocalDateTime expectedReturnDate;
}
