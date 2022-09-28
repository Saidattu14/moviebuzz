package com.example.moviebuzz.ui.payment;

import com.example.moviebuzz.data.model.PaymentResponseModel;

public class PaymentResponeResult {
    String error;
    PaymentResponseModel paymentResponseModel;

    public PaymentResponeResult() {
    }

    public PaymentResponeResult(String error, PaymentResponseModel paymentResponseModel) {
        this.error = error;
        this.paymentResponseModel = paymentResponseModel;
    }

    public String getError() {
        return error;
    }

    public void setPaymentResponseModel(PaymentResponseModel paymentResponseModel) {
        this.paymentResponseModel = paymentResponseModel;
    }

    public PaymentResponseModel getPaymentResponseModel() {
        return paymentResponseModel;
    }
}
