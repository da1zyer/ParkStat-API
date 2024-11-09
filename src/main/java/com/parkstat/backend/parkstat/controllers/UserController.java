package com.parkstat.backend.parkstat.controllers;

import com.parkstat.backend.parkstat.models.User;
import com.parkstat.backend.parkstat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController("/auth")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public void register(String name, String email, String password) {
        userRepository.save(new User(name, email, password));
    }
}
