package com.parkstat.backend.parkstat.repositories;

import com.parkstat.backend.parkstat.models.log.CarEvent;
import com.parkstat.backend.parkstat.models.log.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Integer> {
    List<Log> findByParkingId(int parkingId);
    List<Log> findByParkingIdAndEvent(int parkingId, CarEvent event);
    List<Log> findByParkingIdAndCar_Plate(int parkingId, String plate);
    List<Log> findByParkingIdAndEventAndCar_Plate(int parkingId, CarEvent event, String plate);
}
