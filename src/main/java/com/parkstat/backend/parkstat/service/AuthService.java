package com.parkstat.backend.parkstat.service;

import com.parkstat.backend.parkstat.jwt.JwtCore;
import com.parkstat.backend.parkstat.models.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtCore jwtCore;

    public TokenResponse auth(String name, String password) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(name, password)
            );
        }
        catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "bla bla bla...");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtCore.generateToken(authentication);
        return new TokenResponse(jwt);
    }
}
