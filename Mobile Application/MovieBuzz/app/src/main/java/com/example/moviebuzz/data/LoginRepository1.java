package com.example.moviebuzz.data;

import com.example.moviebuzz.data.model.LoggedInUserRequestBody;
import com.example.moviebuzz.data.model.LoginResponse;
import com.example.moviebuzz.data.model.RegisterUser;
import com.example.moviebuzz.data.model.RegisterUserRequestBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginRepository1 {
    @POST("/api/v1/login_user")
    Call<LoginResponse> loginUser(@Body LoggedInUserRequestBody loggedInUserRequestBody);
}
