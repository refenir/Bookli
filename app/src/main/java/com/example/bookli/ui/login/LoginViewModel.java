package com.example.bookli.ui.login;

import android.graphics.LinearGradient;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookli.data.LoginRepository;
import com.example.bookli.data.Result;
import com.example.bookli.data.model.LoggedInUser;
import com.example.bookli.R;

import java.util.regex.Pattern;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String name, String studentId) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(name, studentId);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String name, String studentId, String phoneNumber, String email) {
        if (!isUserNameValid(name)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_name, null, null, null));
        } else if (!isStudentIdValid(studentId)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_student_id, null, null));
        } else if (!isPhoneNumberValid(phoneNumber)) {
            loginFormState.setValue(new LoginFormState(null, null, R.string.invalid_phone_number, null));
        } else if (!isEmailValid(email)) {
            loginFormState.setValue(new LoginFormState(null, null, null, R.string.invalid_email));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        else {
            return !username.trim().isEmpty();
        }
    }


    // A placeholder studentId validation check
    private boolean isStudentIdValid(String studentId) {
        if (!studentId.equals("")) {
            return Integer.parseInt(studentId.trim()) > 1000000;
        }
        return false;
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.length() == 8;
    }

    private boolean isEmailValid(String email) {
        return Pattern.compile("^(.+)@(\\S+) $")
                .matcher(email)
                .matches();
    }
}