package com.parkstat.backend.parkstat.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class ParkingDeleteDTO {
    @NotNull
    private final int id;

    @JsonCreator
    public ParkingDeleteDTO(@JsonProperty int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
