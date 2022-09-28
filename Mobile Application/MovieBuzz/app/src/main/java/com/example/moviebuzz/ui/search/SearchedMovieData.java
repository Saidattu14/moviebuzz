package com.example.moviebuzz.ui.search;

import androidx.annotation.Nullable;

import com.example.moviebuzz.data.model.SearchMoviesResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SearchedMovieData {

    @Nullable
    private SearchMoviesResponse currentSearchedMovieData;

    @Nullable
    private Stack<SearchMoviesResponse> clickedMoviesList = new Stack<>();

    @Nullable
    private Integer error;



    @Nullable
    public Integer getError() {
        return error;
    }

    public SearchedMovieData() {
    }

    public SearchedMovieData(@Nullable SearchMoviesResponse currentSearchedMovieData,
                             @Nullable Stack<SearchMoviesResponse> clickedMoviesList,
                             @Nullable Integer error) {
        this.currentSearchedMovieData = currentSearchedMovieData;
        this.clickedMoviesList = clickedMoviesList;
        this.error = error;
    }

    public void setCurrentSearchedMovieData(@Nullable SearchMoviesResponse currentSearchedMovieData) {
        this.currentSearchedMovieData = currentSearchedMovieData;
    }

    @Nullable
    public SearchMoviesResponse getCurrentSearchedMovieData() {
        return currentSearchedMovieData;
    }

    @Nullable
    public Stack<SearchMoviesResponse> getClickedMoviesList() {
        return clickedMoviesList;
    }

    public void addClickedData(SearchMoviesResponse searchMoviesResponse)
    {
        this.clickedMoviesList.add(searchMoviesResponse);
    }
}
