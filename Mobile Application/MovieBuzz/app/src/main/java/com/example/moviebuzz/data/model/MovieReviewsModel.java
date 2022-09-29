package com.example.moviebuzz.data.model;

public class MovieReviewsModel {

    String reviewId;
    String review;
    String username;
    float rating;

    public MovieReviewsModel(String reviewId, String review, String username, float rating) {
        this.reviewId = reviewId;
        this.review = review;
        this.username = username;
        this.rating = rating;
    }

    public String getReviewId() {
        return reviewId;
    }

    public String getReview() {
        return review;
    }

    public String getUsername() {
        return username;
    }

    public float getRating() {
        return rating;
    }
}
