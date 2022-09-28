package com.example.moviebuzz.data.model;

import java.util.UUID;

public class BookingHistoryRequestModel {

    UUID requestId;
    String requestType;
    String token;
    UUID userId;

    public BookingHistoryRequestModel(UUID requestId, String requestType, String token,
                                      UUID userId) {
        this.requestId = requestId;
        this.requestType = requestType;
        this.token = token;
        this.userId = userId;
    }
}
