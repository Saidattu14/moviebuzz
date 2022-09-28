package com.example.moviebuzz.ui.addReview;

public class AddReviewResult {

    public String reviewResponse;
    public String error;

    public AddReviewResult(String reviewResponse, String error) {
        this.reviewResponse = reviewResponse;
        this.error = error;
    }

    public String getReviewResponse() {
        return reviewResponse;
    }

    public String getError() {
        return error;
    }
}
