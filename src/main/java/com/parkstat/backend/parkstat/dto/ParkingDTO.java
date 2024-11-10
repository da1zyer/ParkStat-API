package com.parkstat.backend.parkstat.dto;

public class ParkingDTO {
    private String name;
    private int spaceCount;
    private int userId;

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setSpaceCount(int spaceCount) {
        this.spaceCount = spaceCount;
    }
    public int getSpaceCount() {
        return spaceCount;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getUserId() {
        return userId;
    }
}
