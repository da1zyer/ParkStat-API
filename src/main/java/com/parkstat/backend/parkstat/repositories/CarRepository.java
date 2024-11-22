package com.parkstat.backend.parkstat.repositories;

import com.parkstat.backend.parkstat.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
    Car findCarByPlate(String plate);
    boolean existsCarByPlate(String plate);
    List<Car> findCarByParkingId(int parkingId);
}
