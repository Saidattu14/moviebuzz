package com.example.moviebuzz.ui.payment;

import androidx.annotation.Nullable;

public class PaymentFormState {
    @Nullable
    Integer cardNumberError;
    @Nullable
    Integer cvvError;
    @Nullable
    Integer expiryDateError;
    @Nullable
    Integer postalCodeError;
    @Nullable
    Integer cardNameError;
    final boolean isDataValid;

    public PaymentFormState(Integer cardNumberError, Integer cvvError, Integer expiryDateError, Integer postalCodeError, Integer cardNameError) {
        this.cardNumberError = cardNumberError;
        this.cvvError = cvvError;
        this.expiryDateError = expiryDateError;
        this.postalCodeError = postalCodeError;
        this.cardNameError = cardNameError;
        this.isDataValid = false;
    }

    public PaymentFormState(boolean isDataValid)
    {
        this.cardNameError = null;
        this.cvvError = null;
        this.expiryDateError = null;
        this.postalCodeError = null;
        this.cardNumberError =null;
        this.isDataValid = true;
    }

    public Integer getCardNumberError() {
        return cardNumberError;
    }

    public Integer getCvvError() {
        return cvvError;
    }

    public Integer getExpiryDateError() {
        return expiryDateError;
    }

    public Integer getPostalCodeError() {
        return postalCodeError;
    }

    public Integer getCardNameError() {
        return cardNameError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}
