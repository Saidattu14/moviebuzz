package com.example.moviebuzz.ui.movieAvalablity;

public class MovieAvailablityTimeDate {

    String date;
    String time;
    String error;

    public MovieAvailablityTimeDate() {
    }

    public MovieAvailablityTimeDate(String date, String time, String error) {
        this.date = date;
        this.time = time;
        this.error = error;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
