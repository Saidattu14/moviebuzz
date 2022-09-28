package com.example.moviebuzz.data.model;

import java.util.List;

public class TicketsDetailsModel {

    String Date;
    List<Shows> shows_details;

    public TicketsDetailsModel(String date, List<Shows> shows_details) {
        this.Date = date;
        this.shows_details = shows_details;
    }

    public String getDate() {
        return Date;
    }

    public List<Shows> getShows_details() {
        return shows_details;
    }
}
