package com.parkstat.backend.parkstat.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserRegisterDTO {
    @NotNull(message = "Name is required")
    @Size(min = 4, max = 32, message = "The name length must be from 4 to 32 characters")
    private final String name;

    @NotNull(message = "Email is required")
    @Email(message = "Invalid email address")
    private final String email;

    @NotNull(message = "Password is required")
    @Size(min = 8, max = 32, message = "The password length must be from 8 to 32 characters")
    private final String password;

    @JsonCreator
    public UserRegisterDTO(@JsonProperty String name,
                           @JsonProperty String email,
                           @JsonProperty String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
