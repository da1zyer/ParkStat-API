package com.parkstat.backend.parkstat.service;

import com.parkstat.backend.parkstat.models.Camera;
import com.parkstat.backend.parkstat.models.Parking;
import com.parkstat.backend.parkstat.models.user.User;
import com.parkstat.backend.parkstat.repositories.CameraRepository;
import com.parkstat.backend.parkstat.repositories.ParkingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class ParkingService {
    @Autowired
    private ParkingRepository parkingRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CameraRepository cameraRepository;

    public Parking getParking(int id, String authHeader) {
        Parking parking = getParkingById(id);
        User user = userService.getUserFromHeader(authHeader);
        if (user.getId() == parking.getUser().getId()) {
            return parking;
        }
        else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Parking does not belong to the user");
        }
    }

    private Parking getParkingById(int id) {
        Optional<Parking> parkingOptional = parkingRepository.findById(id);
        if (parkingOptional.isPresent()) {
            return parkingOptional.get();
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking not found");
        }
    }

    public Camera getCamera(int id, Parking parking) {
        Camera camera = getCameraById(id);
        if (camera.getParking().getId() == parking.getId()) {
            return camera;
        }
        else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Camera does not belong to the parking");
        }
    }

    private Camera getCameraById(int id) {
        Optional<Camera> optionalCamera = cameraRepository.findById(id);
        if (optionalCamera.isPresent()) {
            return optionalCamera.get();
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Camera not found");
        }
    }
}
