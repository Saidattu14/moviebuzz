package com.example.moviebuzz.data.model;

import java.util.List;

public class Shows {
    String show_id;
    String timings;
    List<Seats> Available_seats;

    public Shows(String show_id, String timings, List<Seats> available_seats) {
        this.show_id = show_id;
        this.timings = timings;
        Available_seats = available_seats;
    }

    public String getShow_id() {
        return show_id;
    }

    public String getTimings() {
        return timings;
    }

    public List<Seats> getAvailable_seats() {
        return Available_seats;
    }
}