package com.sameh.onlinebookstore.repository;

import com.sameh.onlinebookstore.entity.BorrowingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface BorrowingRequestRepository extends
        JpaRepository<BorrowingRequest, Long> {
    Optional<BorrowingRequest> findByBookId(Long bookId);

    List<BorrowingRequest> findByUserId(Long userId);
}
