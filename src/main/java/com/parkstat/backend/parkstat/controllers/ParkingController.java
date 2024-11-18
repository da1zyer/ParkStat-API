package com.parkstat.backend.parkstat.controllers;

import com.parkstat.backend.parkstat.dto.ParkingDTO;
import com.parkstat.backend.parkstat.dto.ParkingUpdateDTO;
import com.parkstat.backend.parkstat.models.Parking;
import com.parkstat.backend.parkstat.models.user.User;
import com.parkstat.backend.parkstat.repositories.ParkingRepository;
import com.parkstat.backend.parkstat.service.UserService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/park")
@SecurityRequirement(name = "bearerAuth")
public class ParkingController {
    @Autowired
    private ParkingRepository parkingRepository;
    @Autowired
    private UserService userService;

    @PostMapping
    public Parking create(
            @Schema(hidden = true)
            @RequestHeader(name = "Authorization", required = false) String authHeader,
            @Valid @RequestBody ParkingDTO parkingDTO
    ) {
        User user = userService.getUserFromHeader(authHeader);
        if (parkingDTO.getTakenSpaceCount() > parkingDTO.getSpaceCount()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Taken Spaces greater than Space Count");
        }
        Parking parking = new Parking(parkingDTO, user);
        parkingRepository.save(parking);
        return parking;
    }

    @GetMapping
    public Parking get(
            @Schema(hidden = true)
            @RequestHeader(name = "Authorization", required = false) String authHeader,
            @RequestParam int id
    ) {
        User user = userService.getUserFromHeader(authHeader);
        Optional<Parking> parkingOptional = parkingRepository.findById(id);
        if (parkingOptional.isPresent()) {
            Parking parking = parkingOptional.get();
            if (parking.getUser().getId() == user.getId()) {
                return parking;
            }
            else {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Parking does not belong to the user");
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking not found");
        }
    }

    @PatchMapping
    public Parking update(
            @Schema(hidden = true)
            @RequestHeader(name = "Authorization", required = false) String authHeader,
            @Valid @RequestBody ParkingUpdateDTO parkingUpdateDTO) {
        Optional<Parking> parkingOptional = parkingRepository.findById(parkingUpdateDTO.getId());
        if (parkingOptional.isPresent()) {
            User user = userService.getUserFromHeader(authHeader);
            Parking parking = parkingOptional.get();
            if (user.getId() == parking.getUser().getId()) {
                String name = parkingUpdateDTO.getName();
                Integer spaceCount = parkingUpdateDTO.getSpaceCount();
                Integer takenSpaceCount = parkingUpdateDTO.getTakenSpaceCount();
                if (name != null) {
                    parking.setName(name);
                }
                if (spaceCount != null) {
                    parking.setSpaceCount(spaceCount);
                }
                if (takenSpaceCount != null) {
                    if (takenSpaceCount > parking.getSpaceCount()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Taken Spaces greater than Space Count");
                    }
                    else {
                        parking.setTakenSpaceCount(takenSpaceCount);
                    }
                }
                parkingRepository.save(parking);
                return parking;
            }
            else {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Parking does not belong to the user");
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking not found");
        }
    }

    @GetMapping("/all")
    public List<Parking> getAll(
            @Schema(hidden = true)
            @RequestHeader(name = "Authorization", required = false) String authHeader) {
        User user = userService.getUserFromHeader(authHeader);
        return parkingRepository.findByUserId(user.getId());
    }
}
