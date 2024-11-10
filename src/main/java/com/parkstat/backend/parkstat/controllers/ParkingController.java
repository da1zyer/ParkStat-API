package com.parkstat.backend.parkstat.controllers;

import com.parkstat.backend.parkstat.dto.ParkingDTO;
import com.parkstat.backend.parkstat.models.Parking;
import com.parkstat.backend.parkstat.models.User;
import com.parkstat.backend.parkstat.repositories.ParkingRepository;
import com.parkstat.backend.parkstat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController("/park")
public class ParkingController {
    @Autowired
    private ParkingRepository parkingRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public Parking create(@RequestBody ParkingDTO parkingDTO) {
        Optional<User> userOptional = userRepository.findById(parkingDTO.getUserId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (parkingDTO.getSpaceCount() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Space Count must be greater than 0");
            }
            else {
                Parking parking = new Parking(parkingDTO.getName(), parkingDTO.getSpaceCount(), user);
                parkingRepository.save(parking);
                return parking;
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public List<Parking> getAll(int id) {
        return parkingRepository.findByUserId(id);
    }
}
