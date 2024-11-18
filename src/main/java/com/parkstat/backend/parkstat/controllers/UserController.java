package com.parkstat.backend.parkstat.controllers;

import com.parkstat.backend.parkstat.dto.UserLoginDTO;
import com.parkstat.backend.parkstat.dto.UserRegisterDTO;
import com.parkstat.backend.parkstat.models.TokenResponse;
import com.parkstat.backend.parkstat.models.user.User;
import com.parkstat.backend.parkstat.repositories.UserRepository;
import com.parkstat.backend.parkstat.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "Auth")
@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthService authService;

    @Operation(summary = "Register a user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully registered",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "An account with this name or email already exists",
                    content = @Content
            )
    })
    @PostMapping(path = "/register", produces = "application/json")
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

    @Operation(summary = "Log in a user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully logged in",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Incorrect name or password",
                    content = @Content
            )
    })
    @PostMapping(path = "/login", produces = "application/json")
    public TokenResponse login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
            return authService.auth(userLoginDTO.getName(), userLoginDTO.getPassword());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect name or password");
        }
    }
}
