package com.example.bookli.ui.profile;
import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class ProfileFormState {
    @Nullable
    private Integer nameError;
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer phoneNumberError;
    private boolean isDataValid;


    ProfileFormState(@Nullable Integer nameError, @Nullable Integer emailError, @Nullable Integer phoneNumberError) {
        this.nameError = nameError;
        this.emailError = emailError;
        this.phoneNumberError = phoneNumberError;
        this.isDataValid = false;
    }

    ProfileFormState(boolean isDataValid) {
        this.nameError = null;
        this.emailError = null;
        this.phoneNumberError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getNameError() {
        return nameError;
    }

    @Nullable
    public Integer getEmailError() {
        return emailError;
    }

    @Nullable
    public Integer getPhoneNumberError() {
        return phoneNumberError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}