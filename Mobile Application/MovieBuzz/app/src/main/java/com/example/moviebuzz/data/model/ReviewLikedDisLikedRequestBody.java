package com.example.moviebuzz.data.model;

public class ReviewLikedDisLikedRequestBody {
    private boolean isLiked;
    private boolean isDisLiked;
    private String reviewId;
    private String movieId;
    private String userId;

    public ReviewLikedDisLikedRequestBody(boolean isLiked, boolean isDisLiked, String reviewId, String movieId,String userId) {
        this.isLiked = isLiked;
        this.isDisLiked = isDisLiked;
        this.reviewId = reviewId;
        this.movieId = movieId;
        this.userId = userId;
    }
}
