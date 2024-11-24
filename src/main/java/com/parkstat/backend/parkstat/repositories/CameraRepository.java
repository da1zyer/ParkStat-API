package com.parkstat.backend.parkstat.repositories;

import com.parkstat.backend.parkstat.models.Camera;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CameraRepository extends JpaRepository<Camera, Integer> {
    List<Camera> findCameraByParkingId(int parkingId);
}
