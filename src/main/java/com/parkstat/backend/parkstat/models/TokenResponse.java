package com.parkstat.backend.parkstat.models;

public class TokenResponse {
    private String token;

    public TokenResponse(String token) {
        setToken(token);
    }

    public void setToken(String token) {
        this.token = token;
    }
    public String getToken() {
        return token;
    }
}
