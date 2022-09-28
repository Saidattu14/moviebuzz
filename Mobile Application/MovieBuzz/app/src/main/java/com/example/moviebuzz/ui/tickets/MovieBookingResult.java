package com.example.moviebuzz.ui.tickets;

import com.example.moviebuzz.data.model.BookingResponseModel;

public class MovieBookingResult {

    BookingResponseModel bookingResponseModel;
    boolean backPressed;
    String error;

    public MovieBookingResult(BookingResponseModel bookingResponseModel, String error,
                              boolean backPressed  ) {
        this.bookingResponseModel = bookingResponseModel;
        this.error = error;
        this.backPressed = backPressed;
    }

    public void setBackPressed(boolean backPressed) {
        this.backPressed = backPressed;
    }

    public boolean isBackPressed() {
        return backPressed;
    }

    public BookingResponseModel getBookingResponseModel() {
        return this.bookingResponseModel;
    }

    public String getError() {
        return error;
    }
}
