package com.parkstat.backend.parkstat.controllers;

import com.parkstat.backend.parkstat.dto.ParkingDTO;
import com.parkstat.backend.parkstat.dto.ParkingUpdateDTO;
import com.parkstat.backend.parkstat.models.Parking;
import com.parkstat.backend.parkstat.models.user.User;
import com.parkstat.backend.parkstat.repositories.ParkingRepository;
import com.parkstat.backend.parkstat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
            if (parkingDTO.getSpaceCount() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Space Count must be greater than 0");
            }
            else {
                if (parkingDTO.getTakenSpaceCount() < 0 || parkingDTO.getTakenSpaceCount() > parkingDTO.getSpaceCount()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Taken Spaces < 0 or greater than Space Count");
                }
                else {
                    User user = userOptional.get();
                    Parking parking = new Parking(parkingDTO, user);
                    parkingRepository.save(parking);
                    return parking;
                }
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @GetMapping
    public Parking get(@RequestParam int id) {
        Optional<Parking> parkingOptional = parkingRepository.findById(id);
        if (parkingOptional.isPresent()) {
            return parkingOptional.get();
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking not found");
        }
    }

    @PatchMapping
    public Parking update(@RequestParam int id, @RequestBody ParkingUpdateDTO parkingUpdateDTO) {
        Optional<Parking> parkingOptional = parkingRepository.findById(id);
        if (parkingOptional.isPresent()) {
            Parking parking = parkingOptional.get();
            String name = parkingUpdateDTO.getName();
            Integer spaceCount = parkingUpdateDTO.getSpaceCount();
            Integer takenSpaceCount = parkingUpdateDTO.getTakenSpaceCount();
            if (name != null) {
                if (name.isEmpty()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name can not be empty");
                }
                else {
                    parking.setName(name);
                }
            }
            if (spaceCount != null) {
                if (spaceCount <= 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Space Count must be greater than 0");
                }
                else {
                    parking.setSpaceCount(spaceCount);
                }
            }
            if (takenSpaceCount != null) {
                if (takenSpaceCount > parking.getSpaceCount() || takenSpaceCount < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Taken Spaces < 0 or greater than Space Count");
                }
                else {
                    parking.setTakenSpaceCount(takenSpaceCount);
                }
            }
            parkingRepository.save(parking);
            return parking;
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking not found");
        }
    }

    @GetMapping("/all")
    public List<Parking> getAll(@RequestParam int userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return parkingRepository.findByUserId(userId);
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }
}
