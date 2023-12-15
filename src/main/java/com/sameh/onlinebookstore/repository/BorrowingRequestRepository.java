package com.sameh.onlinebookstore.repository;

import com.sameh.onlinebookstore.entity.BorrowingRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BorrowingRequestRepository extends
        JpaRepository<BorrowingRequest, Long> {
    Optional<BorrowingRequest> findByBookId(Long bookId);
}
