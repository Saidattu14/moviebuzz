package com.example.moviebuzz.data.model;

import java.util.UUID;

public class MovieReviewsRequestModel {

    UUID requestId;
    String requestType;
    String movieId;

    public MovieReviewsRequestModel(UUID requestId, String requestType, String movieId) {
        this.requestId = requestId;
        this.requestType = requestType;
        this.movieId = movieId;
    }
}
