package com.parkstat.backend.parkstat.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.parkstat.backend.parkstat.dto.ParkingDTO;
import com.parkstat.backend.parkstat.models.log.Log;
import com.parkstat.backend.parkstat.models.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
public class Parking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "spaceCount", nullable = false)
    @NotNull
    private int spaceCount;

    @Column(name = "takenSpaceCount", nullable = false)
    private int takenSpaceCount;

    // This column stores an encrypted key.
    // The key must be provided by the ML model
    // to enable logging functionality.
    @Column(name = "accessKey", nullable = false)
    @NotNull
    @JsonIgnore
    private String accessKey;

    @ManyToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "userId", nullable = false)
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "parking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Car> cars;

    @OneToMany(mappedBy = "parking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Log> logs;

    public Parking() {

    }
    public Parking(ParkingDTO parkingDTO, User user, String accessKey) {
        setName(parkingDTO.getName());
        setSpaceCount(parkingDTO.getSpaceCount());
        setTakenSpaceCount(parkingDTO.getTakenSpaceCount());
        setAccessKey(accessKey);
        setUser(user);
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

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

    public void setTakenSpaceCount(int takenSpaceCount) {
        this.takenSpaceCount = takenSpaceCount;
    }
    public int getTakenSpaceCount() {
        return takenSpaceCount;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
    public String getAccessKey() {
        return accessKey;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public User getUser() {
        return user;
    }
}
