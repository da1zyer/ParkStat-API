package com.parkstat.backend.parkstat.controllers;

import com.parkstat.backend.parkstat.models.Parking;
import com.parkstat.backend.parkstat.models.TimeStatResponse;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
    @GetMapping(path = "/logs", produces = "application/json")
    public List<Log> getLogs(
            @Schema(hidden = true)
            @RequestHeader(name = "Authorization", required = false) String authHeader,
            @RequestParam(required = true) int parkingId,
            @RequestParam(required = false) CarEvent event,
            @RequestParam(required = false) String plate,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to) {
        Parking parking = parkingService.getParking(parkingId, authHeader);
        List<Log> logs = new ArrayList<>();
        if (event != null) {
            if (plate != null) {
                logs = logRepository.findByParkingIdAndEventAndCar_Plate(parkingId, event, plate);
            }
            logs = logRepository.findByParkingIdAndEvent(parkingId, event);
        }
        else {
            if (plate != null) {
                logs = logRepository.findByParkingIdAndCar_Plate(parkingId, plate);
            }
            logs = logRepository.findByParkingId(parkingId);
        }
        if (from != null) {
            for (Log log : logs) {
                if (log.getTimestamp().isBefore(from)) {
                    logs.remove(log);
                }
            }
        }
        if (to != null) {
            for (Log log : logs) {
                if (log.getTimestamp().isAfter(to)) {
                    logs.remove(log);
                }
            }
        }
        return logs;
    }

    @Operation(summary = "Get stats for the day")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully received",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TimeStatResponse.class)
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
    @GetMapping(path = "dayStats", produces = "application/json")
    public List<TimeStatResponse> getDayStats(
            @Schema(hidden = true)
            @RequestHeader(name = "Authorization", required = false) String authHeader,
            @RequestParam(required = true) int parkingId,
            @RequestParam(required = true) CarEvent event,
            @RequestParam(required = false) LocalDate date) {
        Parking parking = parkingService.getParking(parkingId, authHeader);
        List<Log> logs = logRepository.findByParkingIdAndEvent(parkingId, event);
        if (date == null) {
            date = LocalDate.now();
        }
        LocalDateTime from = LocalDateTime.of(date, LocalTime.MIN);
        List<TimeStatResponse> response = new ArrayList<>();
        for (Log log : logs) {
            if (log.getTimestamp().isBefore(from)) {
                logs.remove(log);
            }
        }
        for (int i = 0; i < 24; i++) {
            TimeStatResponse timeStatResponse = new TimeStatResponse(from, 0);
            for (Log log : logs) {
                if (log.getTimestamp().isAfter(from) && log.getTimestamp().isBefore(from.plusHours(1))) {
                    timeStatResponse.setCount(timeStatResponse.getCount() + 1);
                }
            }
            response.add(timeStatResponse);
            from = from.plusHours(1);
        }
        return response;
    }
}
