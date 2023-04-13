package com.example.bookli.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookli.R;


import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ProfileViewModel extends ViewModel {
    private MutableLiveData<ProfileFormState> profileFormState = new MutableLiveData<>();

    LiveData<ProfileFormState> getLoginFormState() {
        return profileFormState;
    }

    public void profileDataChanged(String name, String email, String phoneNumber) {
        if (!isUserNameValid(name)) {
            profileFormState.setValue(new ProfileFormState(R.string.invalid_name, null, null));
        } else if (!isEmailValid(email)) {
            profileFormState.setValue(new ProfileFormState(null, R.string.invalid_email, null));
        } else if (!isPhoneNumValid(phoneNumber)){
            profileFormState.setValue(new ProfileFormState(null, null, R.string.invalid_phone_number));
        } else {
            profileFormState.setValue(new ProfileFormState(true));
        }
    }

    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        else {
            return !username.trim().isEmpty();
        }
    }

    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        else {
            String regex = "^(.+)@(.+)$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
    }

    private boolean isPhoneNumValid(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        }
        else {
            return (phoneNumber.length() == 8 && (phoneNumber.charAt(0) == '8' || phoneNumber.charAt(0) == '9'));
        }
    }
}