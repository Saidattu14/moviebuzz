package com.example.moviebuzz.data.model;

import java.util.List;
import java.util.UUID;

public class BookingHistoryResponseModel {
    UUID requestId;
    String requestType;
    List<MovieBookingHistoryDetails> movieBookingHistoryDetails;

    public BookingHistoryResponseModel(UUID requestId,
                                       String requestType,
                                       List<MovieBookingHistoryDetails> movieBookingHistoryDetails) {
        this.requestId = requestId;
        this.requestType = requestType;
        this.movieBookingHistoryDetails = movieBookingHistoryDetails;
    }

    public UUID getRequestId() {
        return requestId;
    }



    public String getRequestType() {
        return requestType;
    }



    public List<MovieBookingHistoryDetails> getMovieBookingHistoryDetails() {
        return movieBookingHistoryDetails;
    }
}
