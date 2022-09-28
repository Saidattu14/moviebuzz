package com.example.moviebuzz.data;

import com.example.moviebuzz.data.model.AddReviewRequestBody;
import com.example.moviebuzz.data.model.LoggedInUserRequestBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AddReviewRepository {

    @POST("/api/v1/add_review")
    Call<String> addReviewApiCall(@Header("authorization") String token, @Body AddReviewRequestBody addReviewRequestBody);
}
