package com.example.moviebuzz.ui.register;

import androidx.annotation.Nullable;

import com.example.moviebuzz.data.model.RegisterResponse;

public class RegisterResult {

    @Nullable
    private String error;

    @Nullable
    private RegisterResponse registerResponse;

    public RegisterResult(@Nullable String error, @Nullable RegisterResponse registerResponse) {
        this.error = error;
        this.registerResponse = registerResponse;
    }

    @Nullable
    public String getError() {
        return error;
    }

    @Nullable
    public RegisterResponse getRegisterResponse() {
        return registerResponse;
    }
}
