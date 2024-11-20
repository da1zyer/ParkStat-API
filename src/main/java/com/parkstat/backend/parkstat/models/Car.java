package com.parkstat.backend.parkstat.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.parkstat.backend.parkstat.models.log.Log;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "plate", nullable = false, unique = true)
    @NotNull
    private String plate;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "parkingId", nullable = true)
    @JsonIgnore
    private Parking parking;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Log> logs;

    public Car() {

    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }
    public String getPlate() {
        return plate;
    }

    public void setParking(Parking parking) {
        this.parking = parking;
    }
    public Parking getParking() {
        return parking;
    }
}
