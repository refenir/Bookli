package com.example.bookli.ui.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.bookli.data.BookingDataService;
import com.example.bookli.MainActivity;
import com.example.bookli.R;
import com.example.bookli.data.UserModel;
import com.example.bookli.databinding.ActivityLoginBinding;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    public final String sharedPrefFile = "com.example.android.mainsharedpref";

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    BookingDataService bookingDataService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bookingDataService = new BookingDataService(this);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final TextInputEditText nameEditText = binding.getRoot().findViewById(R.id.name);
        final TextInputEditText studentIdEditText = binding.getRoot().findViewById(R.id.student_id);
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;

        // check if data is valid
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getNameError() != null) {
                    nameEditText.setError(getString(loginFormState.getNameError()));
                }
                if (loginFormState.getStudentIdError() != null) {
                    studentIdEditText.setError(getString(loginFormState.getStudentIdError()));
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(nameEditText.getText().toString(),
                        studentIdEditText.getText().toString());
            }
        };
        nameEditText.addTextChangedListener(afterTextChangedListener);
        studentIdEditText.addTextChangedListener(afterTextChangedListener);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // store that user has logged in, they don't have to login again
                SharedPreferences pref = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
                SharedPreferences.Editor edt = pref.edit();
                edt.putBoolean("activity_executed", true);
                edt.apply();

                loadingProgressBar.setVisibility(View.VISIBLE);
                bookingDataService.postStudentInfo(Integer.parseInt(studentIdEditText.getText().toString()),
                        nameEditText.getText().toString(),
                        new BookingDataService.PostStudentInfoResponseListener() {
                            @Override
                            public void onError(String msg) {

                            }

                            @Override
                            public void onResponse(UserModel userModel) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("studentId", userModel.getStudentId());
                                intent.putExtra("name", userModel.getName());
                                intent.putExtra("phoneNumber", userModel.getPhoneNumber());
                                intent.putExtra("email", userModel.getEmail());
                                startActivity(intent);
                                finish();
                            }
                        });
            }
        });

    }
}