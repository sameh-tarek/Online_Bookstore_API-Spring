package com.sameh.onlinebookstore.service;

import com.sameh.onlinebookstore.model.auth.AuthenticationRequest;
import com.sameh.onlinebookstore.model.auth.AuthenticationResponse;
import com.sameh.onlinebookstore.model.user.UserRequestDTO;

public interface AuthenticationService {
    AuthenticationResponse register(UserRequestDTO request);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
