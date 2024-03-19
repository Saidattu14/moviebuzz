package com.example.moviebuzz.data.model;

public class MovieReviewsModel {

    String reviewId;
    String reviewer_name;
    String rating_value;
    Integer likesCount;
    Integer dislikesCount;
    String reviewer_url;
    String review_data;
    String full_review;
    String short_review;

    public String getReviewId() {
        return reviewId;
    }

    public String getReviewer_name() {
        return reviewer_name;
    }

    public String getRating_value() {
        return rating_value;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public Integer getDislikesCount() {
        return dislikesCount;
    }

    public String getReviewer_url() {
        return reviewer_url;
    }

    public String getReview_data() {
        return review_data;
    }

    public String getFull_review() {
        return full_review;
    }

    public String getShort_review() {
        return short_review;
    }

    public MovieReviewsModel(String reviewId, String reviewer_name, String rating_value,
                             String full_review, String  short_review, Integer likesCount, Integer dislikesCount,
                             String review_data, String reviewer_url) {
        this.reviewId = reviewId;
        this.reviewer_name = reviewer_name;
        this.rating_value = rating_value;
        this.dislikesCount = dislikesCount;
        this.likesCount = likesCount;
        this.review_data = review_data;
        this.full_review = full_review;
        this.short_review = short_review;
        this.reviewer_url = reviewer_url;
    }

}
