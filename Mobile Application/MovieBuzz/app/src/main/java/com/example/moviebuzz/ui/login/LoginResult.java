package com.example.moviebuzz.ui.login;

import androidx.annotation.Nullable;

import com.example.moviebuzz.data.model.LoginResponse;

/**
 * Authentication result : success (user details) or error message.
 */
class LoginResult {

    @Nullable
    private String error;
    @Nullable
    private LoginResponse loginResponse;

    public LoginResult(@Nullable String error, @Nullable LoginResponse loginResponse) {
        this.error = error;
        this.loginResponse = loginResponse;
    }

    @Nullable
    public LoginResponse getLoginResponse() {
        return loginResponse;
    }

    @Nullable
    String getError() {
        return error;
    }
}