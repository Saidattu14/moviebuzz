package com.example.moviebuzz.ui.search;

import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import com.example.moviebuzz.data.model.SearchMoviesResponse;

public class SearchResult {

    @Nullable
    private Integer error;

    @Nullable
    private List<SearchMoviesResponse> searchMoviesResponseList;



    public SearchResult(@Nullable Integer error,@Nullable List<SearchMoviesResponse> searchMoviesResponseList) {
        this.error = error;
        this.searchMoviesResponseList = searchMoviesResponseList;
    }

    @Nullable
    public List<SearchMoviesResponse> getSearchMoviesResponseList() {
        return searchMoviesResponseList;
    }

    @Nullable
    public Integer getError() {
        return error;
    }

}
