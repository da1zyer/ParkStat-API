package com.parkstat.backend.parkstat.repositories;

import com.parkstat.backend.parkstat.models.log.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log, Integer> {
}
