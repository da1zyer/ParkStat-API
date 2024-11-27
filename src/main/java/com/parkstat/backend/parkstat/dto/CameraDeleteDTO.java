package com.parkstat.backend.parkstat.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class CameraDeleteDTO {
    @NotNull
    private final int id;

    @NotNull
    private final int parkingId;

    @JsonCreator
    public CameraDeleteDTO(@JsonProperty int id,
                           @JsonProperty int parkingId) {
        this.id = id;
        this.parkingId = parkingId;
    }

    public int getId() {
        return id;
    }

    public int getParkingId() {
        return parkingId;
    }
}
