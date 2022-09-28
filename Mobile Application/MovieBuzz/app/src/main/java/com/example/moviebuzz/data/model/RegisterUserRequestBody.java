package com.example.moviebuzz.data.model;

public class RegisterUserRequestBody {

    private String username;
    private String password;
    private String confirmPassword;

    public RegisterUserRequestBody(String username, String password, String confirmPassword) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
