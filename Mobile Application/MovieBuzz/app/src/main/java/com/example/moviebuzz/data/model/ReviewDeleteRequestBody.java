package com.example.moviebuzz.data.model;

public class ReviewDeleteRequestBody {
    private String reviewId;
    private String movieId;

    public ReviewDeleteRequestBody(String reviewId, String movieId) {
        this.reviewId = reviewId;
        this.movieId = movieId;
    }
}
