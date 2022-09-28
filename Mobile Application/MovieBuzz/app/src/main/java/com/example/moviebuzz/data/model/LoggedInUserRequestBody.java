package com.example.moviebuzz.data.model;

public class LoggedInUserRequestBody {
    private String username;
    private String password;

    public LoggedInUserRequestBody(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
