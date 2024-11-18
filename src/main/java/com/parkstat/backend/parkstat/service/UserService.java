package com.parkstat.backend.parkstat.service;

import com.parkstat.backend.parkstat.jwt.JwtCore;
import com.parkstat.backend.parkstat.models.user.User;
import com.parkstat.backend.parkstat.models.user.UserDetailsImpl;
import com.parkstat.backend.parkstat.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtCore jwtCore;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByName(username);
        if (user.isPresent()) {
            return new UserDetailsImpl(user.get());
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    public User getUserFromHeader(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            String name = jwtCore.getNameFromJwt(token);
            Optional<User> userOptional = userRepository.findUserByName(name);
            if (userOptional.isPresent()) {
                return userOptional.get();
            }
            else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token provided, but user not found");
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The token must be provided");
        }
    }
}
