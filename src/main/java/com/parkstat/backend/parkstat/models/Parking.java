package com.parkstat.backend.parkstat.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.parkstat.backend.parkstat.dto.ParkingDTO;
import com.parkstat.backend.parkstat.models.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @ManyToOne()
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "userId", nullable = false)
    @JsonIgnore
    private User user;

    public Parking() {

    }
    public Parking(ParkingDTO parkingDTO, User user) {
        setName(parkingDTO.getName());
        setSpaceCount(parkingDTO.getSpaceCount());
        setTakenSpaceCount(parkingDTO.getTakenSpaceCount());
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

    public void setUser(User user) {
        this.user = user;
    }
    public User getUser() {
        return user;
    }
}
