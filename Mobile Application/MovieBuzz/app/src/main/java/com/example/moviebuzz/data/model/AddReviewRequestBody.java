package com.example.moviebuzz.data.model;

public class AddReviewRequestBody {

    String movieId;
    String review;
    float rating;

    public AddReviewRequestBody(String movieId, String review,float rating) {
        this.movieId = movieId;
        this.review = review;
        this.rating = rating;
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

}
