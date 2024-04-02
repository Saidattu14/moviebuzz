package com.example.moviebuzz.data;

import com.example.moviebuzz.data.model.AddReviewRequestBody;
import com.example.moviebuzz.data.model.ReviewDeleteRequestBody;
import com.example.moviebuzz.data.model.ReviewLikedDisLikedRequestBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;

public interface ReviewRepository {

    @POST("/api/v1/add_review")
    Call<String> addReviewApiCall(@Header("authorization") String token, @Body AddReviewRequestBody addReviewRequestBody);

    @PATCH("/api/v1/like_dislike_review")
    Call<String> likedOrDisLikedReviewApiCall(@Header("authorization") String token, @Body ReviewLikedDisLikedRequestBody reviewLikedDisLikedRequestBody);

    @PATCH("/api/v1/delete_review")
    Call<String> deleteReviewApiCall(@Header("authorization") String token, @Body ReviewDeleteRequestBody reviewDeleteRequestBody);
}
