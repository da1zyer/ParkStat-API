package com.parkstat.backend.parkstat.controllers;

import com.parkstat.backend.parkstat.models.Parking;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController("/park")
public class ParkingController {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(String name) {
        Parking parking = new Parking(name);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public Parking get(int id) {
        for (Parking parking: Parking.list) {
            if (parking.getId() == id) {
                return parking;
            }
        }
        return null;
    }
}
