package com.example.moviebuzz.ui.viewModel;

import com.example.moviebuzz.data.enums.LocationRequestEnum;
import com.example.moviebuzz.data.enums.SearchApiEnum;

import java.util.UUID;

public class MainClass {

    public String jwtToken;
    public SearchApiEnum currentAPI;
    public String generType;
    public String searchText;
    public String userEmail;
    public UUID userId;
    public LocationRequestEnum isLocationAccepted;

    public MainClass() {
    }

    public MainClass(String jwtToken, SearchApiEnum currentAPI, String generType,String searchText,
                     String userEmail, UUID userId,LocationRequestEnum isLocationAccepted) {
        this.jwtToken = jwtToken;
        this.currentAPI = currentAPI;
        this.generType = generType;
        this.searchText = searchText;
        this.userEmail = userEmail;
        this.userId = userId;
        this.isLocationAccepted = isLocationAccepted;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getGenerType() {
        return generType;
    }

    public void setGenerType(String generType) {
        this.generType = generType;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public SearchApiEnum getCurrentAPI() {
        return currentAPI;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }



    public void setCurrentAPI(SearchApiEnum currentAPI) {
        this.currentAPI = currentAPI;
    }

    public LocationRequestEnum getIsLocationAccepted() {
        return isLocationAccepted;
    }

    public void setIsLocationAccepted(LocationRequestEnum isLocationAccepted) {
        this.isLocationAccepted = isLocationAccepted;
    }
}
