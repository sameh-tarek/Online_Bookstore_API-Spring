package com.sameh.onlinebookstore.repository;

import com.sameh.onlinebookstore.entity.User;
import com.sameh.onlinebookstore.entity.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User newUser;

    @BeforeEach
    public void setup(){
        newUser = new User(
                "exampleUser",
                "example@email.com",
                "examplePassword"
        );
        userRepository.save(newUser);
    }

    @AfterEach
    public void tearDown(){
        userRepository.deleteAll();
    }

    @DisplayName("Find By Email")
    @Test
    public void testFindByEmail(){
        User user = userRepository.findByEmail(newUser.getEmail())
                .orElse(null);
        assertNotNull(user);
    }

}