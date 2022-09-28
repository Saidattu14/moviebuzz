package com.example.moviebuzz.data;

import com.example.moviebuzz.data.model.MovieReviewsModel;
import com.example.moviebuzz.data.model.MovieReviewsResponseBody;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface MovieRepository {

    @GET("/api/v1/get_reviews/{movieId}/{request_id}")
    Call<MovieReviewsResponseBody> movieReviews(
            @Path(value="movieId", encoded=true) String movieId,
            @Path(value="request_id", encoded=true) String request_id);
}
