package com.example.moviebuzz.data.model;

public class ReviewLikedRequestBody {
    private boolean isLiked;
    private boolean isDisLiked;
    private String reviewId;

    public ReviewLikedRequestBody(boolean isLiked, boolean isDisLiked, String reviewId) {
        this.isLiked = isLiked;
        this.isDisLiked = isDisLiked;
        this.reviewId = reviewId;
    }
}
