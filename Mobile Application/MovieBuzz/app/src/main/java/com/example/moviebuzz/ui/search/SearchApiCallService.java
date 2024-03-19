package com.example.moviebuzz.ui.search;

import com.example.moviebuzz.adapters.SearchDataMoviesAdapter;
import com.example.moviebuzz.data.enums.SearchApiEnum;
import com.example.moviebuzz.databinding.FragmentSearchBinding;

public class SearchApiCallService {

    SearchViewModel searchViewModel;
    SearchFragment searchFragment;
    FragmentSearchBinding binding;
    double latitude;
    double longitude;
    SearchApiEnum searchApiEnumValue;
    String genreType;
    String jwtToken;
    String country;
    int scrollPosition;
    String searchText;

    public SearchApiCallService(SearchViewModel searchViewModel, SearchFragment searchFragment) {
        this.searchViewModel = searchViewModel;
        this.searchFragment = searchFragment;
    }

    public void setUpdatedValues()
    {
        latitude = searchFragment.getLatitude();
        longitude = searchFragment.getLongitude();
        searchApiEnumValue = searchFragment.getSearchApiEnumValue();
        genreType = searchFragment.getGenerType();
        jwtToken = searchFragment.getToken();
        scrollPosition = searchFragment.getScrollPosition();
        searchText = searchFragment.getSearchText();
        country = searchFragment.getCountry();
    }
    public void searchWithLocationBased()
    {
        setUpdatedValues();
        searchViewModel.moviesSearchAPIRequest(jwtToken,searchApiEnumValue,country,latitude,longitude,null,scrollPosition);
    }


    public void searchWithOutLocationBased()
    {
        setUpdatedValues();
        if(searchApiEnumValue != null) {
            if(searchApiEnumValue == SearchApiEnum.Country_Based_Search) {
                searchViewModel.moviesSearchAPIRequest(jwtToken,searchApiEnumValue,searchFragment.getSearchText(),latitude,longitude,genreType,scrollPosition);
            } else{
                searchViewModel.moviesSearchAPIRequest(jwtToken,searchApiEnumValue,country,latitude,longitude,genreType,scrollPosition);
            }
        } else{
            searchViewModel.moviesSearchAPIRequest(jwtToken,null,country,latitude,longitude,genreType,scrollPosition);
        }


    }

    public void searchWildCard()
    {
        setUpdatedValues();
        searchViewModel.wildCardSearchApi(jwtToken,searchText,scrollPosition);
    }
}
