package com.example.moviebuzz.data.model;

import java.util.List;
import java.util.UUID;

public class MovieReviewsResponseBody {

    UUID requestId;
    List<MovieReviewsModel> reviews;
    String movieId;
    public MovieReviewsResponseBody(UUID requestId, List<MovieReviewsModel> reviews,String movieId) {

        this.requestId = requestId;
        this.reviews = reviews;
        this.movieId = movieId;
    }

    public String getMovieId() {
        return movieId;
    }

    public List<MovieReviewsModel> getMovieReviewsModelList() {
        return this.reviews;
    }

    public UUID getRequestId() {
        return requestId;
    }
}
