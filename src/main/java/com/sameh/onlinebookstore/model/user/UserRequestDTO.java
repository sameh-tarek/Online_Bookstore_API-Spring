package com.sameh.onlinebookstore.model.user;

import com.sameh.onlinebookstore.entity.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    private String userName;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String email;

    private String password;

    private boolean isEnabled = true;
}
