package com.example.moviebuzz.data.model;

import com.example.moviebuzz.ui.tickets.Seating1;

import java.util.List;
import java.util.UUID;

public class BookingResponseModel {
    UUID requestId;
    String requestType;
    String cityName;
    String movieName;
    String theater_id;
    String show_id;
    List<Seating1> seating;
    String state;
    String countryName;
    String theater_name;
    String date;
    String payment_id;
    String status;
    String bookingId;
    String movieId;
    String moviePoster;

    public BookingResponseModel() {
    }

    public BookingResponseModel(UUID requestId, String requestType, String cityName,
                                String movieName, String theater_id, String show_id,
                                List<Seating1> seating, String state, String countryName,
                                String theater_name, String date, String payment_id,
                                String status, String bookingId,String movieId, String moviePoster) {
        this.requestId = requestId;
        this.requestType = requestType;
        this.cityName = cityName;
        this.movieName = movieName;
        this.theater_id = theater_id;
        this.show_id = show_id;
        this.seating = seating;
        this.state = state;
        this.countryName = countryName;
        this.theater_name = theater_name;
        this.date = date;
        this.payment_id = payment_id;
        this.status = status;
        this.bookingId = bookingId;
        this.movieId = movieId;
        this.moviePoster = moviePoster;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getCityName() {
        return cityName;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getTheater_id() {
        return theater_id;
    }

    public String getShow_id() {
        return show_id;
    }

    public List<Seating1> getSeating() {
        return seating;
    }

    public String getState() {
        return state;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getTheater_name() {
        return theater_name;
    }

    public String getDate() {
        return date;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public String getStatus() {
        return status;
    }

    public String getBookingId() {
        return bookingId;
    }
}
