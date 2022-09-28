package com.example.moviebuzz.data.model;

import java.util.UUID;

public class LoginResponse {

    private String jwtToken;
    private String userEmail;
    private UUID userId;


    public LoginResponse(String jwtToken, String userEmail,UUID userId) {
        this.jwtToken = jwtToken;
        this.userEmail = userEmail;
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
