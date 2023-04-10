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
    private boolean isDataValid;


    LoginFormState(@Nullable Integer nameError, @Nullable Integer studentIdError) {
        this.nameError = nameError;
        this.studentIdError = studentIdError;
        this.isDataValid = false;
    }

    LoginFormState(boolean isDataValid) {
        this.nameError = null;
        this.studentIdError = null;
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

    boolean isDataValid() {
        return isDataValid;
    }
}