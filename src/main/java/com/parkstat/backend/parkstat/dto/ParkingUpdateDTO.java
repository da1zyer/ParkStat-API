package com.parkstat.backend.parkstat.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ParkingUpdateDTO{
    @NotNull
    private final Integer id;

    private final String name;

    @Min(value = 1, message = "Space Count must be greater than 0")
    private final Integer spaceCount;

    @Min(value = 0, message = "Taken Spaces < 0")
    private final Integer takenSpaceCount;

    @NotNull(message = "You must provide access key")
    private final String accessKey;

    @JsonCreator
    public ParkingUpdateDTO(@JsonProperty Integer id,
                            @JsonProperty String name,
                            @JsonProperty Integer spaceCount,
                            @JsonProperty Integer takenSpaceCount,
                            @JsonProperty String accessKey) {
        this.id = id;
        this.name = name;
        this.spaceCount = spaceCount;
        this.takenSpaceCount = takenSpaceCount;
        this.accessKey = accessKey;
    }

    public Integer getId() {
        return id;
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

    public String getAccessKey() {
        return accessKey;
    }
}
