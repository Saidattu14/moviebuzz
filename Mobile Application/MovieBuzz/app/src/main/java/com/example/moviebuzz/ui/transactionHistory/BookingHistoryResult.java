package com.example.moviebuzz.ui.transactionHistory;

import com.example.moviebuzz.data.model.BookingHistoryResponseModel;

public class BookingHistoryResult {

    String error;
    BookingHistoryResponseModel bookingHistoryResponseModel;

    public BookingHistoryResult(String error, BookingHistoryResponseModel bookingHistoryResponseModel) {
        this.error = error;
        this.bookingHistoryResponseModel = bookingHistoryResponseModel;
    }

    public String getError() {
        return error;
    }

    public BookingHistoryResponseModel getBookingHistoryResponseModel() {
        return bookingHistoryResponseModel;
    }
}
