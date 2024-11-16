package com.parkstat.backend.parkstat.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class UserLoginDTO {
    @NotNull(message = "Name is required")
    private final String name;

    @NotNull(message = "Password is required")
    private final String password;

    @JsonCreator
    public UserLoginDTO(@JsonProperty String name,
                        @JsonProperty String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
