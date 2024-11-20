package com.parkstat.backend.parkstat.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.parkstat.backend.parkstat.models.log.CarEvent;
import jakarta.validation.constraints.NotNull;

public class LogDTO {
    @NotNull(message = "You must provide event")
    private final CarEvent event;

    @NotNull(message = "You must provide car plate")
    private final String plate;

    @NotNull(message = "You must provide parking id")
    private final int parkingId;

    @NotNull(message = "You must provide access key")
    private final String accessKey;

    @JsonCreator
    public LogDTO(@JsonProperty CarEvent event,
                  @JsonProperty String plate,
                  @JsonProperty int parkingId,
                  @JsonProperty String accessKey) {
        this.event = event;
        this.plate = plate;
        this.parkingId = parkingId;
        this.accessKey = accessKey;
    }

    public CarEvent getEvent() {
        return event;
    }

    public String getPlate() {
        return plate;
    }

    public int getParkingId() {
        return parkingId;
    }

    public String getAccessKey() {
        return accessKey;
    }
}
