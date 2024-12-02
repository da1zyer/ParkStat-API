package com.parkstat.backend.parkstat.controllers;

import com.parkstat.backend.parkstat.models.Parking;
import com.parkstat.backend.parkstat.models.log.CarEvent;
import com.parkstat.backend.parkstat.models.log.Log;
import com.parkstat.backend.parkstat.repositories.LogRepository;
import com.parkstat.backend.parkstat.service.ParkingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Statistics")
@RestController
@RequestMapping("/stat")
@SecurityRequirement(name = "bearerAuth")
public class StatController {
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private ParkingService parkingService;

    @Operation(summary = "Get logs")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully received",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Log.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "The token must be provided",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Parking not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Parking does not belong to the user",
                    content = @Content
            )
    })
    @GetMapping(path = "", produces = "application/json")
    public List<Log> get(
            @Schema(hidden = true)
            @RequestHeader(name = "Authorization", required = false) String authHeader,
            @RequestParam(required = true) int parkingId,
            @RequestParam(required = false) CarEvent event,
            @RequestParam(required = false) String plate) {
        Parking parking = parkingService.getParking(parkingId, authHeader);
        if (event != null) {
            if (plate != null) {
                return logRepository.findByParkingIdAndEventAndCar_Plate(parkingId, event, plate);
            }
            return logRepository.findByParkingIdAndEvent(parkingId, event);
        }
        else {
            if (plate != null) {
                return logRepository.findByParkingIdAndCar_Plate(parkingId, plate);
            }
            return logRepository.findByParkingId(parkingId);
        }
    }
}
