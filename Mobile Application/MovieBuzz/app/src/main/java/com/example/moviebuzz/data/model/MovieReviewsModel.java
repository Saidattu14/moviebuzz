package com.example.moviebuzz.data.model;

public class MovieReviewsModel {

    String review_id;
    String review;
    String username;
    float rating;
    String _id;

    public MovieReviewsModel(String review, String username,float rating,String review_id,String _id) {
        this.review = review;
        this.username = username;
        this.rating = rating;
        this.review_id = review_id;
        this._id = _id;
    }

    public String get_id() {
        return this._id;
    }

    public String getReview_id() {
        return this.review_id;
    }

    public String getReview() {
        return this.review;
    }

    public float getRating() {
        return this.rating;
    }

    public String getUsername() {
        return this.username;
    }
}
