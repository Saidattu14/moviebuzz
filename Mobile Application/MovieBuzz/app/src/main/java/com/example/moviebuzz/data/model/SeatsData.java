package com.example.moviebuzz.data.model;


public class SeatsData
{
    String status;
    String seat_id;

    public SeatsData(String status, String seat_id) {
        this.status = status;
        this.seat_id = seat_id;
    }

    public String getStatus() {
        return status;
    }

    public String getSeat_id() {
        return seat_id;
    }
}