package com.example.moviebuzz.data.model;
import java.util.List;

public class Seats
{
    String row_id;
    List<SeatsData> seats;

    public Seats(String row_id, List<SeatsData> seats) {
        this.row_id = row_id;
        this.seats = seats;
    }

    public String getRow_id() {
        return row_id;
    }

    public List<SeatsData> getSeats() {
        return seats;
    }

}
