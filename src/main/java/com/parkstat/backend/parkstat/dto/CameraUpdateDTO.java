package com.parkstat.backend.parkstat.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class CameraUpdateDTO {
    @NotNull
    private final int id;

    @NotNull
    private final int parkingId;

    @NotNull
    private final String ip;

    @JsonCreator
    public CameraUpdateDTO(@JsonProperty int id,
                           @JsonProperty int parkingId,
                           @JsonProperty String ip) {
        this.id = id;
        this.parkingId = parkingId;
        this.ip = ip;
    }

    public int getId() {
        return id;
    }

    public int getParkingId() {
        return parkingId;
    }

    public String getIp() {
        return ip;
    }
}
