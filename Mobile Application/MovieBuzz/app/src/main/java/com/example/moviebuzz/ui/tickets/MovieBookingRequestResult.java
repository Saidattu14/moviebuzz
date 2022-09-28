package com.example.moviebuzz.ui.tickets;

import com.example.moviebuzz.data.model.BookingRequestModel;

public class MovieBookingRequestResult {
    BookingRequestModel bookingRequestModel;
    String error;

    public MovieBookingRequestResult(BookingRequestModel bookingRequestModel, String error) {
        this.bookingRequestModel = bookingRequestModel;
        this.error = error;
    }

    public BookingRequestModel getBookingRequestModel() {
        return bookingRequestModel;
    }

    public String getError() {
        return error;
    }
}
