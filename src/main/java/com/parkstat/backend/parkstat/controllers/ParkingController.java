package com.parkstat.backend.parkstat.controllers;

import com.parkstat.backend.parkstat.dto.CameraDTO;
import com.parkstat.backend.parkstat.dto.CameraUpdateDTO;
import com.parkstat.backend.parkstat.dto.ParkingDTO;
import com.parkstat.backend.parkstat.dto.ParkingUpdateDTO;
import com.parkstat.backend.parkstat.models.Camera;
import com.parkstat.backend.parkstat.models.Parking;
import com.parkstat.backend.parkstat.models.user.User;
import com.parkstat.backend.parkstat.repositories.CameraRepository;
import com.parkstat.backend.parkstat.repositories.ParkingRepository;
import com.parkstat.backend.parkstat.service.EncryptionService;
import com.parkstat.backend.parkstat.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Tag(name = "Parking")
@RestController
@RequestMapping("/park")
@SecurityRequirement(name = "bearerAuth")
public class ParkingController {
    @Autowired
    private ParkingRepository parkingRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private EncryptionService encryptionService;
    @Autowired
    private CameraRepository cameraRepository;

    @Operation(summary = "Create new parking")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Parking.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "The token must be provided",
                    content = @Content
            )
    })
    @PostMapping(path = "", produces = "application/json")
    public Parking create(
            @Schema(hidden = true)
            @RequestHeader(name = "Authorization", required = false) String authHeader,
            @Valid @RequestBody ParkingDTO parkingDTO
    ) {
        User user = userService.getUserFromHeader(authHeader);
        if (parkingDTO.getTakenSpaceCount() > parkingDTO.getSpaceCount()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Taken Spaces greater than Space Count");
        }
        try {
            String accessKey = encryptionService.encryptKey(parkingDTO.getAccessKey());
            Parking parking = new Parking(parkingDTO, user, accessKey);
            parkingRepository.save(parking);
            return parking;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Encryption error");
        }
    }

    @Operation(summary = "Get parking by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully received",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Parking.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "The token must be provided",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Parking does not belong to the user",
                    content = @Content
            )
    })
    @GetMapping(path = "", produces = "application/json")
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

    @Operation(summary = "Change parking parameters")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Parking.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content
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
    @PatchMapping(path = "", produces = "application/json")
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
                String accessKey = parkingUpdateDTO.getAccessKey();
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
                if (accessKey != null) {
                    try {
                        String newKey = encryptionService.encryptKey(accessKey);
                        parking.setAccessKey(newKey);
                    } catch (Exception e) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Encryption error");
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

    @Operation(summary = "Get all user parking")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully received",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Parking.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "The token must be provided",
                    content = @Content
            )
    })
    @GetMapping(path = "/all", produces = "application/json")
    public List<Parking> getAll(
            @Schema(hidden = true)
            @RequestHeader(name = "Authorization", required = false) String authHeader) {
        User user = userService.getUserFromHeader(authHeader);
        return parkingRepository.findByUserId(user.getId());
    }

    @PostMapping(path = "/camera", produces = "application/json")
    public Camera createCamera(
            @Schema(hidden = true)
            @RequestHeader(name = "Authorization", required = false) String authHeader,
            @Valid @RequestBody CameraDTO cameraDTO) {
        Optional<Parking> parkingOptional = parkingRepository.findById(cameraDTO.getParkingId());
        if (parkingOptional.isPresent()) {
            User user = userService.getUserFromHeader(authHeader);
            Parking parking = parkingOptional.get();
            if (user.getId() == parking.getUser().getId()) {
                for (Camera camera : cameraRepository.findCameraByParkingId(parking.getId())) {
                    if (camera.getEvent().equals(cameraDTO.getEvent())) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "A camera with this event already exists");
                    }
                }
                Camera camera = new Camera(cameraDTO);
                camera.setParking(parking);
                cameraRepository.save(camera);
                return camera;
            }
            else {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Parking does not belong to the user");
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking not found");
        }
    }

    @PatchMapping(path = "/camera", produces = "application/json")
    public Camera updateCamera(
            @Schema(hidden = true)
            @RequestHeader(name = "Authorization", required = false) String authHeader,
            @Valid @RequestBody CameraUpdateDTO cameraUpdateDTO) {
        Optional<Parking> parkingOptional = parkingRepository.findById(cameraUpdateDTO.getParkingId());
        if (parkingOptional.isPresent()) {
            User user = userService.getUserFromHeader(authHeader);
            Parking parking = parkingOptional.get();
            if (user.getId() == parking.getUser().getId()) {
                Optional<Camera> optionalCamera = cameraRepository.findById(cameraUpdateDTO.getId());
                if (optionalCamera.isPresent()) {
                    Camera camera = optionalCamera.get();
                    if (camera.getParking().getId() == parking.getId()) {
                        camera.setIp(cameraUpdateDTO.getIp());
                        cameraRepository.save(camera);
                        return camera;
                    }
                    else {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Camera does not belong to the parking");
                    }
                }
                else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Camera not found");
                }
            }
            else {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Parking does not belong to the user");
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking not found");
        }
    }
}
