package com.parkstat.backend.parkstat.dto;

public class ParkingUpdateDTO{
    private String name;
    private Integer spaceCount;
    private Integer takenSpaceCount;

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
}
