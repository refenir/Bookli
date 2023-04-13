package com.example.bookli.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bookli.BookingDataService;
import com.example.bookli.MainActivity;
import com.example.bookli.R;
import com.example.bookli.UserModel;
import com.example.bookli.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    private boolean isEditing = false;
    private EditText nameEdit;
    private EditText emailEdit;
    private EditText phoneEdit;
    private EditText studentIdEdit;
    private Button editButton;
    private Button submitButton;

    private ProfileViewModel mViewModel;
    private FragmentProfileBinding binding;
    int studentId;
    String name;
    String phoneNumber;
    String email;
    BookingDataService bookingDataService;
    public final String sharedPrefFile = "com.example.android.mainsharedpref";
    SharedPreferences prefs;


    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        bookingDataService = new BookingDataService(getContext());

        studentId = -1;
        name = null;
        phoneNumber = null;
        email = null;

        Bundle extras = requireActivity().getIntent().getExtras();
        if (extras != null) {
            studentId = extras.getInt("studentId");
            name = extras.getString("name");
        }

//        prefs = getActivity().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE);
//        name = prefs.getString("name", name);
//        studentId = prefs.getInt("studentId", studentId);
//        email = prefs.getString("email", email);
//        phoneNumber = prefs.getString("phoneNumber", phoneNumber);

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

        // Set up the edit button click listener
//        editButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (!isEditing) {
//                    // If we're not currently editing, switch to edit mode
//                    isEditing = true;
//                    editButton.setVisibility(View.GONE);
//                    submitButton.setVisibility(View.VISIBLE);
//                    nameEdit.setEnabled(true);
//                    emailEdit.setEnabled(true);
//                    phoneEdit.setEnabled(true);
//                    studentIdEdit.setEnabled(true);
//                } else {
//                    // If we are currently editing, save the changes and switch back to view mode
//                    isEditing = false;
//                    editButton.setText(getString(R.string.edit_button_text));
//                    nameEdit.setEnabled(false);
//                    emailEdit.setEnabled(false);
//                    phoneEdit.setEnabled(false);
//                    studentIdEdit.setEnabled(false);
//
//                    // Save the changes to the user's information
//                    String newName = nameEdit.getText().toString();
//                    String newEmail = emailEdit.getText().toString();
//                    String newPhone = phoneEdit.getText().toString();
//                    String newSchoolId = studentIdEdit.getText().toString();
//
//                    // TODO: Save the changes to the user's information in your app's data store
//
//                    // Show submit button and hide edit button
//                    submitButton.setVisibility(View.VISIBLE);
//                    editButton.setVisibility(View.GONE);
//
//                }
//            }
//        });

//        submitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Hide submit button and show edit button
//                submitButton.setVisibility(View.GONE);
//                editButton.setVisibility(View.VISIBLE);
//            }
//        });

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
//        SharedPreferences.Editor preferencesEditor = prefs.edit();
//        preferencesEditor.putString("name", name);
//        preferencesEditor.putString("email", email);
//        preferencesEditor.putString("phoneNumber", phoneNumber);
//        preferencesEditor.putInt("studentId", studentId);
//        preferencesEditor.apply();
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