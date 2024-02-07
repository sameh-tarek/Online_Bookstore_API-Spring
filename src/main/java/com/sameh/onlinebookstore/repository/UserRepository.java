package com.sameh.onlinebookstore.repository;

import com.sameh.onlinebookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends
        JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
