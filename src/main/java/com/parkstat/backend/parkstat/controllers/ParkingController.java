package com.parkstat.backend.parkstat.controllers;

import com.parkstat.backend.parkstat.models.Parking;
import com.parkstat.backend.parkstat.models.User;
import com.parkstat.backend.parkstat.repositories.ParkingRepository;
import com.parkstat.backend.parkstat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController("/park")
public class ParkingController {
    @Autowired
    private ParkingRepository parkingRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(String name, int userId) {
        User user = userRepository.getById(userId);
        Parking parking = new Parking(name, user);
        parkingRepository.save(parking);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public List<Parking> getAll(int id) {
        return parkingRepository.findByUserId(id);
    }
}
