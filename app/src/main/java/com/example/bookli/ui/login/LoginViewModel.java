package com.example.bookli.ui.login;

import android.graphics.LinearGradient;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookli.R;

import java.util.regex.Pattern;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();

    LoginViewModel() {

    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }



    public void loginDataChanged(String name, String studentId) {
        if (!isUserNameValid(name)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_name, null));
        } else if (!isStudentIdValid(studentId)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_student_id));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        else {
            return !username.trim().isEmpty();
        }
    }


    // studentId validation check
    private boolean isStudentIdValid(String studentId) {
        if (!studentId.equals("")) {
            return Integer.parseInt(studentId.trim()) > 1000000 && Integer.parseInt(studentId.trim()) < 2000000;
        }
        return false;
    }

}