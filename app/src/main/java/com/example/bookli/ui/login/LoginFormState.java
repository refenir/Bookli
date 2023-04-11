package com.example.bookli.ui.login;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class LoginFormState {
    @Nullable
    private Integer nameError;
    @Nullable
    private Integer studentIdError;
    @Nullable
    private Integer phoneNumberError;
    @Nullable
    private Integer emailError;
    private boolean isDataValid;


    LoginFormState(@Nullable Integer nameError, @Nullable Integer studentIdError, @Nullable Integer phoneNumberError, @Nullable Integer emailError) {
        this.nameError = nameError;
        this.studentIdError = studentIdError;
        this.phoneNumberError = phoneNumberError;
        this.emailError = emailError;
        this.isDataValid = false;
    }

    LoginFormState(boolean isDataValid) {
        this.nameError = null;
        this.studentIdError = null;
        this.phoneNumberError = null;
        this.emailError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer geNameError() {
        return nameError;
    }

    @Nullable
    Integer getStudentIdError() {
        return studentIdError;
    }

    @Nullable
    public Integer getPhoneNumberError() {
        return phoneNumberError;
    }

    @Nullable
    public Integer getEmailError() {
        return emailError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}