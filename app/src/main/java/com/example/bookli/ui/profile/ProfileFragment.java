package com.example.bookli.ui.profile;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bookli.data.BookingDataService;
import com.example.bookli.R;
import com.example.bookli.data.UserModel;
import com.example.bookli.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    private boolean isEditing = false;
    private EditText nameEdit;
    private EditText emailEdit;
    private EditText phoneEdit;
    private EditText studentIdEdit;
    private Button editButton;
    private Button submitButton;

    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;
    int studentId;
    String name;
    String phoneNumber;
    String email;
    BookingDataService bookingDataService;
    public final String sharedPrefFile = "com.example.android.mainsharedpref";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bookingDataService = new BookingDataService(getContext());

        SharedPreferences pref = getActivity().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);


        studentId = -1;
        name = null;
        phoneNumber = null;
        email = null;

        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            studentId = extras.getInt("studentId");
            name = extras.getString("name");
        } else {
            name = pref.getString("name", "");
            studentId = pref.getInt("studentId", 1000000);
            email = pref.getString("email", "");
            phoneNumber = pref.getString("phoneNumber", "");
        }

        nameEdit = binding.nameEdit;
        emailEdit = binding.emailEdit;
        phoneEdit = binding.phoneNumberEdit;
        studentIdEdit = binding.studentIdEdit;
        editButton = binding.editButton;
        submitButton = binding.submitButton;

        if (studentId != -1) {
            studentIdEdit.setText(String.valueOf(studentId));
        }
        if (name != null) {
            nameEdit.setText(name);
        }
        if (email != null) {
            emailEdit.setText(email);
        }
        if (phoneNumber != null) {
            phoneEdit.setText(phoneNumber);
        }

        editButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);

        profileViewModel.getLoginFormState().observe(getViewLifecycleOwner(), new Observer<ProfileFormState>() {
            @Override
            public void onChanged(ProfileFormState profileFormState) {
                if (profileFormState == null) return;
                submitButton.setEnabled(profileFormState.isDataValid());
                if (profileFormState.getNameError() != null) {
                    nameEdit.setError(getActivity().getString(profileFormState.getNameError()));
                }
                if (profileFormState.getEmailError() != null) {
                    emailEdit.setError(getActivity().getString(profileFormState.getEmailError()));
                }
                if (profileFormState.getPhoneNumberError() != null) {
                    phoneEdit.setError(getActivity().getString(profileFormState.getPhoneNumberError()));
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                profileViewModel.profileDataChanged(nameEdit.getText().toString(), emailEdit.getText().toString(),
                        phoneEdit.getText().toString());
            }
        };

        nameEdit.addTextChangedListener(afterTextChangedListener);
        emailEdit.addTextChangedListener(afterTextChangedListener);
        phoneEdit.addTextChangedListener(afterTextChangedListener);

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences pref = getActivity().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor edt = pref.edit();
        edt.putString("name", name);
        edt.putInt("studentId", studentId);
        edt.putString("email", email);
        edt.putString("phoneNumber", phoneNumber);
        Log.d("profile pause", pref.getAll().toString());

        edt.apply();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_button:
                if (!isEditing) {
                    // If we're not currently editing, switch to edit mode
                    isEditing = true;
                    editButton.setVisibility(View.GONE);
                    submitButton.setVisibility(View.VISIBLE);
                    nameEdit.setEnabled(true);
                    emailEdit.setEnabled(true);
                    phoneEdit.setEnabled(true);
                }
                break;

            case R.id.submit_button:
                if (isEditing) {
                    // if we are editing, save
                    isEditing = false;

                    String newName = nameEdit.getText().toString();
                    String newEmail = emailEdit.getText().toString();
                    String newPhone = phoneEdit.getText().toString();
                    int newStudentId = Integer.parseInt(studentIdEdit.getText().toString());

                    // save to backend
                    bookingDataService.updateStudentInfo(newStudentId, newName, newEmail, newPhone, new BookingDataService.UpdateStudentResponseListener() {
                        @Override
                        public void onError(String msg) {
                            Toast.makeText(getContext(), "Update failed", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(UserModel userModel) {
                            Toast.makeText(getContext(), "Update success", Toast.LENGTH_SHORT).show();
                            name = userModel.getName();
                            studentId = userModel.getStudentId();
                            email = userModel.getEmail();
                            phoneNumber = userModel.getPhoneNumber();
                            editButton.setVisibility(View.VISIBLE);
                            submitButton.setVisibility(View.GONE);
                            nameEdit.setEnabled(false);
                            emailEdit.setEnabled(false);
                            phoneEdit.setEnabled(false);
                        }
                    });

                }
                break;

            default:
                // If we're not currently editing, switch to edit mode
                isEditing = true;
                editButton.setVisibility(View.GONE);
                submitButton.setVisibility(View.VISIBLE);
                nameEdit.setEnabled(true);
                emailEdit.setEnabled(true);
                phoneEdit.setEnabled(true);
                studentIdEdit.setEnabled(true);
                break;

        }
    }
}