package com.example.moviebuzz.ui.search;

import androidx.annotation.Nullable;

import com.example.moviebuzz.data.model.SearchMoviesResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PreviousSearchResult {


    @Nullable
    private Stack<List<SearchMoviesResponse>> previousSearchMoviesResponseList = new Stack<>();

    @Nullable
    private Stack<SearchMoviesResponse> previousSearchMoviesResponse = new Stack<>();

    public PreviousSearchResult() {
    }

    @Nullable
    public Stack<List<SearchMoviesResponse>> getPreviousSearchMoviesResponseList() {
        return previousSearchMoviesResponseList;
    }

    public void addPreviousSearchMoviesResponseList(@Nullable List<SearchMoviesResponse> searchMoviesResponseList) {
        this.previousSearchMoviesResponseList.add(searchMoviesResponseList);
    }

    @Nullable
    public Stack<SearchMoviesResponse> getPreviousSearchMoviesResponse() {
        return previousSearchMoviesResponse;
    }

    public void addPreviousSearchMoviesResponse(@Nullable SearchMoviesResponse searchMoviesResponse) {
        this.previousSearchMoviesResponse.add(searchMoviesResponse);
    }

}
