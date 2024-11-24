package com.parkstat.backend.parkstat.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.parkstat.backend.parkstat.dto.CameraDTO;
import com.parkstat.backend.parkstat.models.log.CarEvent;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Camera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ip", nullable = false, unique = true)
    @NotNull
    private String ip;

    @Column(name = "event", nullable = false)
    @Enumerated(EnumType.STRING)
    private CarEvent event;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "parkingId", nullable = true)
    @JsonIgnore
    private Parking parking;

    public Camera() {

    }
    public Camera(CameraDTO cameraDTO) {
        setIp(cameraDTO.getIp());
        setEvent(cameraDTO.getEvent());
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getIp() {
        return ip;
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
}
