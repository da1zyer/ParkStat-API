package com.parkstat.backend.parkstat.repositories;

import com.parkstat.backend.parkstat.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
