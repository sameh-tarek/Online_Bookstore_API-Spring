package com.sameh.onlinebookstore.mapper;

import com.sameh.onlinebookstore.entity.User;
import com.sameh.onlinebookstore.model.user.UserRequestDTO;

public interface UserMapper {
    UserRequestDTO toDTO(User user);

    User toEntity(UserRequestDTO userRequestDTO);
}
