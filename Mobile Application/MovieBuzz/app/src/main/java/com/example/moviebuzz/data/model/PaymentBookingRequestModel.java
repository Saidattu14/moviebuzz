package com.example.moviebuzz.data.model;

import com.example.moviebuzz.ui.tickets.Seating1;

import java.util.List;
import java.util.UUID;

public class PaymentBookingRequestModel {

    String cityName;
    String movieName;
    String theater_id;
    String show_id;
    List<Seating1> seating;
    String state;
    String countryName;
    String theater_name;
    String date;
    String bookingId;
    String movieId;
    String moviePoster;

    public PaymentBookingRequestModel(String cityName, String movieName, String theater_id,
                                      String show_id, List<Seating1> seating, String state,
                                      String countryName, String theater_name, String date,
                                      String bookingId, String movieId, String moviePoster) {
        this.cityName = cityName;
        this.movieName = movieName;
        this.theater_id = theater_id;
        this.show_id = show_id;
        this.seating = seating;
        this.state = state;
        this.countryName = countryName;
        this.theater_name = theater_name;
        this.date = date;
        this.bookingId = bookingId;
        this.movieId = movieId;
        this.moviePoster = moviePoster;
    }

    public String getCityName() {
        return cityName;
    }
}
