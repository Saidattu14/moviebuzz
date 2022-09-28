package com.example.moviebuzz.ui.movieAvalablity;

import com.example.moviebuzz.data.model.Seats;

import java.util.List;

public class MovieTicketsResult {

    public List<Seats> seats;
    public String error;

    public MovieTicketsResult(List<Seats> seats, String error) {
        this.seats = seats;
        this.error = error;
    }

    public List<Seats> getSeats() {
        return seats;
    }

    public void setSeats(List<Seats> seats) {
        this.seats = seats;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
