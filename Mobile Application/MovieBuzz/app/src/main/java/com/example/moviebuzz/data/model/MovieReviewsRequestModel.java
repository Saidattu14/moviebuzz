package com.example.moviebuzz.data.model;

public class MovieReviewsRequestModel {

    String requestId;
    String requestType;
    String movieId;

    public MovieReviewsRequestModel(String requestId, String requestType, String movieId) {
        this.requestId = requestId;
        this.requestType = requestType;
        this.movieId = movieId;
    }
}
