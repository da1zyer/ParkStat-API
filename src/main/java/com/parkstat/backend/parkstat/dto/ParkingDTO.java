package com.parkstat.backend.parkstat.dto;

import jakarta.validation.constraints.NotNull;

public class ParkingDTO {
    private String name = "Парковка";

    @NotNull(message = "You must provide Space Count")
    private Integer spaceCount;
    private Integer takenSpaceCount = 0;

    @NotNull(message = "You must provide User Id")
    private int userId;

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setSpaceCount(Integer spaceCount) {
        this.spaceCount = spaceCount;
    }
    public Integer getSpaceCount() {
        return spaceCount;
    }

    public void setTakenSpaceCount(Integer takenSpaceCount) {
        this.takenSpaceCount = takenSpaceCount;
    }
    public Integer getTakenSpaceCount() {
        return takenSpaceCount;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getUserId() {
        return userId;
    }
}
