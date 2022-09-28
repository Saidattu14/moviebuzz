package com.example.moviebuzz.data.model;

public class PaymentData {

    String cardHolderName;
    String cardNumber;
    String expiryDate;
    String postalCode;
    String cvv;
    String paymentId;

    public PaymentData(String cardHolderName, String cardNumber, String expiryDate,
                       String postalCode, String cvv, String paymentId) {
        this.cardHolderName = cardHolderName;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.postalCode = postalCode;
        this.cvv = cvv;
        this.paymentId = paymentId;
    }
}
