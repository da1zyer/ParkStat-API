package com.parkstat.backend.parkstat.controllers;

import com.parkstat.backend.parkstat.dto.LogDTO;
import com.parkstat.backend.parkstat.models.Car;
import com.parkstat.backend.parkstat.models.Parking;
import com.parkstat.backend.parkstat.models.log.CarEvent;
import com.parkstat.backend.parkstat.models.log.Log;
import com.parkstat.backend.parkstat.repositories.CarRepository;
import com.parkstat.backend.parkstat.repositories.LogRepository;
import com.parkstat.backend.parkstat.repositories.ParkingRepository;
import com.parkstat.backend.parkstat.service.EncryptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Tag(name = "Logging")
@RestController
@RequestMapping("/log")
public class LogController {
    @Autowired
    ParkingRepository parkingRepository;
    @Autowired
    CarRepository carRepository;
    @Autowired
    LogRepository logRepository;
    @Autowired
    EncryptionService encryptionService;

    @Operation(summary = "Create log", description = "Must be called from ML model")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Log.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Access denied",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Parking not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "The car is not parked here or parked somewhere else",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Decryption error",
                    content = @Content
            )
    })
    @PostMapping(path = "", produces = "application/json")
    public Log create(@Valid @RequestBody LogDTO logDTO) {
        Optional<Parking> parkingOptional = parkingRepository.findById(logDTO.getParkingId());
        if (parkingOptional.isPresent()) {
            Parking parking = parkingOptional.get();
            String accessKey;
            try {
                accessKey = encryptionService.decryptKey(parking.getAccessKey());
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Decryption error");
            }
            if (logDTO.getAccessKey().equals(accessKey)) {
                Car car = new Car();
                if (carRepository.existsCarByPlate(logDTO.getPlate())) {
                    car = carRepository.findCarByPlate(logDTO.getPlate());
                }
                else {
                    car.setPlate(logDTO.getPlate());
                }
                int changeTakenSpaceCount = 0;
                if (logDTO.getEvent().equals(CarEvent.ENTRY)) {
                    if (car.getParking() != null) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "The car is parked somewhere else");
                    }
                    car.setParking(parking);
                    changeTakenSpaceCount++;
                }
                else if (logDTO.getEvent().equals(CarEvent.EXIT)) {
                    boolean flag = false;
                    for (Car parkedCar : carRepository.findCarByParkingId(parking.getId())) {
                        if (parkedCar.getId() == car.getId()) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        car.setParking(null);
                        changeTakenSpaceCount--;
                    }
                    else {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "The car is not parked here");
                    }
                }
                parking.setTakenSpaceCount(parking.getTakenSpaceCount() + changeTakenSpaceCount);
                carRepository.save(car);
                Log log = new Log();
                log.setCar(car);
                log.setParking(parking);
                log.setEvent(logDTO.getEvent());
                log.setTimestamp(LocalDateTime.now());
                logRepository.save(log);
                return log;
            }
            else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access denied");
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking not found");
        }
    }
}
