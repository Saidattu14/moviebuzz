package com.example.moviebuzz.data.model;

import java.util.UUID;

public class TheaterRequestModel {

    UUID requestId;
    String requestType;
    String cityName;
    String movieName;
    String token;
    UUID userId;
    public TheaterRequestModel() {
    }

    public TheaterRequestModel(UUID requestId, String requestType, String cityName, String movieName,String token, UUID userId) {
        this.requestId = requestId;
        this.requestType = requestType;
        this.cityName = cityName;
        this.movieName = movieName;
        this.token = token;
        this.userId = userId;
    }
}
