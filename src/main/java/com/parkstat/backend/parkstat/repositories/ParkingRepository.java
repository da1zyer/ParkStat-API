package com.parkstat.backend.parkstat.repositories;

import com.parkstat.backend.parkstat.models.Parking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingRepository extends JpaRepository<Parking, Integer> {
}
