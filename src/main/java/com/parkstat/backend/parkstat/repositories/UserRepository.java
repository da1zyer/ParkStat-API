package com.parkstat.backend.parkstat.repositories;

import com.parkstat.backend.parkstat.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByName(String name);
    boolean existsUserByEmail(String email);
}
