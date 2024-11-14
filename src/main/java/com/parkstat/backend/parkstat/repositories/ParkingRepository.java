package com.parkstat.backend.parkstat.repositories;

import com.parkstat.backend.parkstat.models.Parking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingRepository extends JpaRepository<Parking, Integer> {
    List<Parking> findByUserId(int userId);
}
