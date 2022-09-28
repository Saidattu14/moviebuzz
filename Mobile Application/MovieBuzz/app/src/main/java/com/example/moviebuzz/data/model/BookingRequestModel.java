package com.example.moviebuzz.data.model;

import com.example.moviebuzz.ui.tickets.Seating;
import com.example.moviebuzz.ui.tickets.Seating1;

import java.util.List;
import java.util.UUID;

public class BookingRequestModel {

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
    String bookingId;
    String movieId;
    String moviePoster;
    String token;
    UUID userId;

    public BookingRequestModel() {
    }

    public BookingRequestModel(UUID requestId,
                               String requestType,
                               String cityName, String movieName, String theater_id,
                               String show_id, List<Seating1> seating, String state,
                               String countryName, String theater_name, String date,
                               String bookingId,String movieId,String moviePoster,
                               String token, UUID userId) {
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
        this.bookingId = bookingId;
        this.movieId = movieId;
        this.moviePoster = moviePoster;
        this.token = token;
        this.userId = userId;
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

    public String getBookingId() {
        return bookingId;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public String getToken() {
        return token;
    }

    public UUID getUserId() {
        return userId;
    }
}
