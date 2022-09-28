package com.example.moviebuzz.ui.tickets;

import java.util.List;

public class Seating1 {

   String row_id;
   List<String> seat_numbers;

    public Seating1(String row_id, List<String> seat_numbers) {
        this.row_id = row_id;
        this.seat_numbers = seat_numbers;
    }

    public String getRow_id() {
        return row_id;
    }

    public void setRow_id(String row_id) {
        this.row_id = row_id;
    }

    public List<String> getSeat_numbers() {
        return seat_numbers;
    }

    public void setSeat_numbers(List<String> seat_numbers) {
        this.seat_numbers = seat_numbers;
    }
}
