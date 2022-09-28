package com.example.moviebuzz.data.model;

import java.util.List;
import java.util.UUID;

public class MovieReviewsResponseBody {

    String request_id;
    List<MovieReviewsModel> reviews;
    String movieId;
    public MovieReviewsResponseBody(String request_id, List<MovieReviewsModel> reviews,String movieId) {

        this.request_id = request_id;
        this.reviews = reviews;
        this.movieId = movieId;
    }

    public String getMovieId() {
        return movieId;
    }

    public List<MovieReviewsModel> getMovieReviewsModelList() {
        return this.reviews;
    }

    public String getRequest_id() {
        return this.request_id;
    }
}
