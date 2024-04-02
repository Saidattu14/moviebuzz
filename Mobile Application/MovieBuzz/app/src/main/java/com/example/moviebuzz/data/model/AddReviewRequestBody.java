package com.example.moviebuzz.data.model;

public class AddReviewRequestBody {

    String movieId;
    String review;
    float rating;
    String reviewer_name;

    public AddReviewRequestBody(String movieId, String review,float rating,  String reviewer_name) {
        this.movieId = movieId;
        this.review = review;
        this.rating = rating;
        this.reviewer_name = reviewer_name;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getReview() {
        return review;
    }

    public Float getRating() {
        return rating;
    }

    public String getReviewer_name() {
        return reviewer_name;
    }
}
