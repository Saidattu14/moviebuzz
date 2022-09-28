package com.example.moviebuzz.data.model;

import com.example.moviebuzz.ui.tickets.Seating1;

import java.util.List;
import java.util.UUID;

public class MovieBookingHistoryDetails {

    UUID bookingid;
    List<Seating1> seating;
    String show_id;
    String countryname;
    String cityname;
    String moviename;
    String date;
    String movieid;
    String theatername;
    UUID paymentid;
    String movieposter;
    String paymentstatus;

    public MovieBookingHistoryDetails(UUID bookingid, List<Seating1> seating,
                                      String show_id, String countryname, String cityname,
                                      String moviename, String date, String movieid,
                                      String theatername, UUID paymentid,String movieposter,
                                      String paymentstatus) {
        this.bookingid = bookingid;
        this.seating = seating;
        this.show_id = show_id;
        this.countryname = countryname;
        this.cityname = cityname;
        this.moviename = moviename;
        this.date = date;
        this.movieid = movieid;
        this.theatername = theatername;
        this.paymentid = paymentid;
        this.movieposter = movieposter;
        this.paymentstatus = paymentstatus;
    }

    public String getPaymentStatus() {
        return paymentstatus;
    }

    public String getMovieposter() {
        return movieposter;
    }

    public UUID getBookingid() {
        return bookingid;
    }

    public List<Seating1> getSeating() {
        return seating;
    }

    public String getShow_id() {
        return show_id;
    }

    public String getCountryname() {
        return countryname;
    }

    public String getCityname() {
        return cityname;
    }

    public String getMoviename() {
        return moviename;
    }

    public String getDate() {
        return date;
    }

    public String getMovieid() {
        return movieid;
    }

    public String getTheatername() {
        return theatername;
    }

    public UUID getPaymentid() {
        return paymentid;
    }
}
