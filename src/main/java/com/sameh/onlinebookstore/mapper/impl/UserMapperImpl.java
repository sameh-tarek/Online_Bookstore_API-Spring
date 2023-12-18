package com.sameh.onlinebookstore.mapper.impl;

import com.sameh.onlinebookstore.entity.User;
import com.sameh.onlinebookstore.mapper.UserMapper;
import com.sameh.onlinebookstore.model.user.UserRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public UserRequestDTO toDTO(User user) {
        UserRequestDTO newUserRequestDTO = new UserRequestDTO();
        newUserRequestDTO.setUserName(user.getUserName());
        newUserRequestDTO.setEmail(user.getEmail());
        newUserRequestDTO.setRole(user.getRole());
        newUserRequestDTO.setEnabled(user.isEnabled());
        newUserRequestDTO.setPassword(user.getPassword());
        return newUserRequestDTO;
    }

    @Override
    public User toEntity(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setUserName(userRequestDTO.getUserName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword());
        user.setRole(userRequestDTO.getRole());
        user.setEnabled(userRequestDTO.isEnabled());
        return user;
    }
}
