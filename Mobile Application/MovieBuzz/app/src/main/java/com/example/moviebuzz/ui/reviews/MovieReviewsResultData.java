package com.example.moviebuzz.ui.reviews;

import com.example.moviebuzz.data.model.MovieReviewsModel;
import com.example.moviebuzz.data.model.MovieReviewsResponseBody;

import java.util.List;

public class MovieReviewsResultData {

    String error;
    List<MovieReviewsModel> movieReviewsData;
    String movieId;

    public MovieReviewsResultData() {
    }

    public MovieReviewsResultData(String error, List<MovieReviewsModel> movieReviewsData, String movieId) {
        this.error = error;
        this.movieReviewsData = movieReviewsData;
        this.movieId = movieId;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getError() {
        return error;
    }

    public List<MovieReviewsModel> getMovieReviewsData() {
        return movieReviewsData;
    }
}
