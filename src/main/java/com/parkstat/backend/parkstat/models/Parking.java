package com.parkstat.backend.parkstat.models;

import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
public class Parking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private static int idCounter = 1;
    @Column(name = "name", nullable = false)
    private String name;
    // Remove when has ParkingDAO, it is just for test
    public static ArrayList<Parking> list = new ArrayList<>();

    public Parking() {
        setId(idCounter++);
        setName("default");
    }
    public Parking(String name) {
        setId(idCounter++);
        setName(name);
        list.add(this);
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
}
