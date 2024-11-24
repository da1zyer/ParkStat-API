package com.parkstat.backend.parkstat.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.parkstat.backend.parkstat.models.log.CarEvent;
import jakarta.validation.constraints.NotNull;

public class CameraDTO {
    @NotNull(message = "You must provide ip")
    private final String ip;

    @NotNull(message = "You must provide event")
    private final CarEvent event;

    @NotNull(message = "You must provide parking id")
    private final int parkingId;

    @JsonCreator
    public CameraDTO(@JsonProperty String ip,
                     @JsonProperty CarEvent event,
                     @JsonProperty int parkingId) {
        this.ip = ip;
        this.event = event;
        this.parkingId = parkingId;
    }

    public String getIp() {
        return ip;
    }

    public CarEvent getEvent() {
        return event;
    }

    public int getParkingId() {
        return parkingId;
    }
}
