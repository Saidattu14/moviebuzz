package com.example.moviebuzz.data.model;

import java.util.UUID;

public class PaymentResponseModel {

    UUID requestId;
    String requestType;
    String paymentStatus;

    public PaymentResponseModel(UUID requestId, String requestType, String paymentStatus) {
        this.requestId = requestId;
        this.requestType = requestType;
        this.paymentStatus = paymentStatus;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }
}
