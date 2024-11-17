package com.parkstat.backend.parkstat.controllers;

import com.parkstat.backend.parkstat.dto.UserLoginDTO;
import com.parkstat.backend.parkstat.dto.UserRegisterDTO;
import com.parkstat.backend.parkstat.models.TokenResponse;
import com.parkstat.backend.parkstat.models.user.User;
import com.parkstat.backend.parkstat.repositories.UserRepository;
import com.parkstat.backend.parkstat.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public TokenResponse register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        if (userRepository.existsUserByName(userRegisterDTO.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with this name already exists");
        }
        if (userRepository.existsUserByEmail(userRegisterDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with this email already exists");
        }
        String hashedPassword = passwordEncoder.encode(userRegisterDTO.getPassword());
        User user = new User(userRegisterDTO.getName(), userRegisterDTO.getEmail(), hashedPassword);
        userRepository.save(user);
        return authService.auth(userRegisterDTO.getName(), userRegisterDTO.getPassword());
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody UserLoginDTO userLoginDTO) {
        try {
            return authService.auth(userLoginDTO.getName(), userLoginDTO.getPassword());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect name or password");
        }
    }
}
