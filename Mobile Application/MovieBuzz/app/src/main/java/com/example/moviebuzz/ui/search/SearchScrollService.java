package com.example.moviebuzz.ui.search;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviebuzz.adapters.SearchDataMoviesAdapter;
import com.example.moviebuzz.data.enums.SearchApiEnum;
import com.example.moviebuzz.databinding.FragmentSearchBinding;

public class SearchScrollService {

    SearchViewModel searchViewModel;
    SearchFragment searchFragment;
    FragmentSearchBinding binding;
    SearchApiCallService searchApiCallService;

    public SearchScrollService(SearchViewModel searchViewModel, SearchFragment searchFragment, FragmentSearchBinding binding,
                               SearchApiCallService searchApiCallService) {
        this.searchViewModel = searchViewModel;
        this.searchFragment = searchFragment;
        this.binding = binding;
        this.searchApiCallService = searchApiCallService;
    }

    public RecyclerView.OnScrollListener onScrollListenerFunction()
    {
        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                int count = 0;
                int scrollPosition = searchFragment.getScrollPosition();
                SearchApiEnum searchApiEnumValue = searchFragment.getSearchApiEnumValue();
                boolean previousSearchResponse = searchFragment.isSearchPrevResultsResponse();
                //System.out.println(scrollPosition);
                SearchDataMoviesAdapter searchDataMoviesAdapter = (SearchDataMoviesAdapter) binding.moviesList.getAdapter();
                if(searchDataMoviesAdapter != null && previousSearchResponse) {
                    count = searchDataMoviesAdapter.getItemCount();
                    if (position > scrollPosition - 5) {
                        searchFragment.incrementScrollCount();
                        if (searchApiEnumValue == SearchApiEnum.Location_Based_Search) {
                            searchApiCallService.searchWithLocationBased();
                        } else if (searchApiEnumValue == SearchApiEnum.Genre_Movies_Search) {
                            searchApiCallService.searchWithOutLocationBased();
                        } else if (searchApiEnumValue == SearchApiEnum.Other) {
                            searchApiCallService.searchWildCard();
                        } else if(searchApiEnumValue == SearchApiEnum.Popular_Movies_Search) {

                            searchApiCallService.searchWithOutLocationBased();
                        } else if(searchApiEnumValue == SearchApiEnum.Rated_Movies_Search) {
                            searchApiCallService.searchWithOutLocationBased();
                        } else if(searchApiEnumValue == SearchApiEnum.Country_Based_Search) {
                            searchApiCallService.searchWithOutLocationBased();
                        } else {
                            System.out.println(searchApiEnumValue);
                        }
                    }
                }
            }
        };
        return onScrollListener;
    }
}
