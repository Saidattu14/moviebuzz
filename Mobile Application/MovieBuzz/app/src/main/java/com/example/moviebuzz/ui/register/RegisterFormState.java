package com.example.moviebuzz.ui.register;

import androidx.annotation.Nullable;

public class RegisterFormState {

    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    private boolean isDataValid;
    @Nullable
    private Integer passwordUnMatchError;

    public RegisterFormState() {
    }

    public RegisterFormState setUsernameError(@Nullable Integer usernameError) {
        this.usernameError = usernameError;
        return null;
    }

    public void setPasswordError(@Nullable Integer passwordError) {
        this.passwordError = passwordError;
    }

    public void setDataValid(boolean dataValid) {
        isDataValid = dataValid;
    }

    public void setPasswordUnMatchError(@Nullable Integer passwordUnMatchError) {
        this.passwordUnMatchError = passwordUnMatchError;
    }

    @Nullable
    public void clearErrors()
    {
        this.passwordError = null;
        this.usernameError = null;
        this.passwordUnMatchError = null;
        this.isDataValid = true;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }

    @Nullable
    Integer getPasswordUnMatchError() {
        return passwordUnMatchError;
    }
}
