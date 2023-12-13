package com.sameh.onlinebookstore.repository;

import com.sameh.onlinebookstore.entity.BorrowingRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowingRequestRepository extends
        JpaRepository<BorrowingRequest, Long> {
}
