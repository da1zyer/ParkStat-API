package com.parkstat.backend.parkstat.models.log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.parkstat.backend.parkstat.models.Car;
import com.parkstat.backend.parkstat.models.Parking;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "event", nullable = false)
    @Enumerated(EnumType.STRING)
    private CarEvent event;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "parkingId", nullable = false)
    @JsonIgnore
    private Parking parking;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "carId", nullable = false)
    private Car car;

    public Log() {

    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setEvent(CarEvent event) {
        this.event = event;
    }
    public CarEvent getEvent() {
        return event;
    }

    public void setParking(Parking parking) {
        this.parking = parking;
    }
    public Parking getParking() {
        return parking;
    }

    public void setCar(Car car) {
        this.car = car;
    }
    public Car getCar() {
        return car;
    }
}
