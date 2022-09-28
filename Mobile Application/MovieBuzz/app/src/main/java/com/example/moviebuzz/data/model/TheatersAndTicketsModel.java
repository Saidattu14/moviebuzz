package com.example.moviebuzz.data.model;

import java.util.List;

public class TheatersAndTicketsModel {
    String id;
    String country;
    String state;
    String city;
    String theater_id;
    String theater_name;
    String movie_id;
    String movie_name;
    List<TicketsDetailsModel> available_booking_data;

    public TheatersAndTicketsModel(String id, String country,
                                   String state, String city,
                                   String theater_id,
                                   String theater_name, String movie_id,
                                   String movie_name,
                                   List<TicketsDetailsModel> available_booking_data)
    {
        this.id = id;
        this.country = country;
        this.state = state;
        this.city = city;
        this.theater_id = theater_id;
        this.theater_name = theater_name;
        this.movie_id = movie_id;
        this.movie_name = movie_name;
        this.available_booking_data = available_booking_data;
    }

    public String getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getTheater_id() {
        return theater_id;
    }

    public String getTheater_name() {
        return theater_name;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public String getMovie_name() {
        return movie_name;
    }

    public List<TicketsDetailsModel> getAvailable_booking_data() {
        return available_booking_data;
    }
}
