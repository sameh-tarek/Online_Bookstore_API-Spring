package com.sameh.onlinebookstore.mapper;

import com.sameh.onlinebookstore.entity.BorrowingRequest;
import com.sameh.onlinebookstore.model.borrowingRequest.BorrowingRequestDTO;
import com.sameh.onlinebookstore.model.borrowingRequest.BorrowingRequestWrapperDTO;

public interface BorrowingRequestMapper {
    BorrowingRequestDTO toDTO(BorrowingRequest borrowingRequest);

    BorrowingRequest toEntity(BorrowingRequestDTO borrowingRequestDTO);

    BorrowingRequestWrapperDTO toWrapperDTO(BorrowingRequest borrowingRequest);
}
