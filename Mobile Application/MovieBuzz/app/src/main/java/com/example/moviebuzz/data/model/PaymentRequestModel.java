package com.example.moviebuzz.data.model;

import com.example.moviebuzz.ui.tickets.Seating1;

import java.util.UUID;

public class PaymentRequestModel {

    UUID requestId;
    String requestType;
    String token;
    UUID userId;
    PaymentData paymentData;
    PaymentBookingRequestModel bookingData;

    public PaymentRequestModel(UUID requestId, String requestType, PaymentData paymentData,
                               PaymentBookingRequestModel bookingData,String token,UUID userId) {
        this.requestId = requestId;
        this.requestType = requestType;
        this.paymentData = paymentData;
        this.bookingData = bookingData;
        this.token = token;
        this.userId = userId;
    }
}
