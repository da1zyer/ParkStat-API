package com.parkstat.backend.parkstat.models;

import java.time.LocalDateTime;

public class TimeStatResponse {
    private LocalDateTime timestamp;
    private int count;

    public TimeStatResponse(LocalDateTime timestamp, int count) {
        setTimestamp(timestamp);
        setCount(count);
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setCount(int count) {
        this.count = count;
    }
    public int getCount() {
        return count;
    }
}
