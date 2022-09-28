package com.example.moviebuzz.data;

import com.example.moviebuzz.data.model.RegisterResponse;
import com.example.moviebuzz.data.model.RegisterUserRequestBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegisterRepository {

    @POST("/api/v1/register_user")
    Call<RegisterResponse> registerUser(@Body RegisterUserRequestBody registerUserRequestBody);
}
