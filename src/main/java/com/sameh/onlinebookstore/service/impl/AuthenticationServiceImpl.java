package com.sameh.onlinebookstore.service.impl;

import com.sameh.onlinebookstore.exception.ConflictException;
import com.sameh.onlinebookstore.mapper.UserMapper;
import com.sameh.onlinebookstore.model.auth.AuthenticationRequest;
import com.sameh.onlinebookstore.model.auth.AuthenticationResponse;
import com.sameh.onlinebookstore.model.user.UserRequestDTO;
import com.sameh.onlinebookstore.repository.UserRepository;
import com.sameh.onlinebookstore.security.JWTService;
import com.sameh.onlinebookstore.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthenticationResponse register(UserRequestDTO request) {
        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.warn("this user want to register: {}", user);

        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new ConflictException("this User already exist");
        }

        userRepository.save(user);
        log.warn("The user has registered successfully {}", user);
        return authenticate(new AuthenticationRequest(user.getEmail(), request.getPassword()));
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.warn("user wants to login with this credentials {}", request);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Not found User"));

        var jwtToken = jwtService.generateToken(authentication);
        log.warn("The user has login successfully {}", user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .id(user.getId())
                .email(user.getEmail())
                .userName(user.getUserName())
                .build();
    }
}
