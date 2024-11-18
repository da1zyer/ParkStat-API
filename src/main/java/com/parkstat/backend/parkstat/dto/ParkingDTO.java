package com.parkstat.backend.parkstat.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ParkingDTO {
    private final String name;

    @NotNull(message = "You must provide Space Count")
    @Min(value = 1, message = "Space Count must be greater than 0")
    private final Integer spaceCount;

    @Min(value = 0, message = "Taken Spaces < 0")
    private final Integer takenSpaceCount;

    @JsonCreator
    public ParkingDTO(@JsonProperty String name,
                      @JsonProperty Integer spaceCount,
                      @JsonProperty Integer takenSpaceCount) {
        if (name == null) this.name = "Парковка";
        else this.name = name;

        this.spaceCount = spaceCount;

        if (takenSpaceCount == null) this.takenSpaceCount = 0;
        else this.takenSpaceCount = takenSpaceCount;
    }

    public String getName() {
        return name;
    }

    public Integer getSpaceCount() {
        return spaceCount;
    }

    public Integer getTakenSpaceCount() {
        return takenSpaceCount;
    }
}
