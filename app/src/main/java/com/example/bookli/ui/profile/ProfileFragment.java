package com.example.bookli.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.bookli.R;
import com.example.bookli.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private boolean isEditing = false;
    private EditText nameEdit;
    private EditText emailEdit;
    private EditText phoneEdit;
    private EditText schoolIdEdit;
    private Button editButton;
    private Button submitButton;

    private ProfileViewModel mViewModel;
    private FragmentProfileBinding binding;


    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        nameEdit = root.findViewById(R.id.name_edit);
        emailEdit = root.findViewById(R.id.email_edit);
        phoneEdit = root.findViewById(R.id.phone_edit);
        schoolIdEdit = root.findViewById(R.id.school_id_edit);
        editButton = root.findViewById(R.id.edit_button);
        submitButton = root.findViewById(R.id.submit_button);

        // Set up the edit button click listener
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isEditing) {
                    // If we're not currently editing, switch to edit mode
                    isEditing = true;
                    editButton.setText(getString(R.string.save_button_text));
                    nameEdit.setEnabled(true);
                    emailEdit.setEnabled(true);
                    phoneEdit.setEnabled(true);
                    schoolIdEdit.setEnabled(true);
                } else {
                    // If we are currently editing, save the changes and switch back to view mode
                    isEditing = false;
                    editButton.setText(getString(R.string.edit_button_text));
                    nameEdit.setEnabled(false);
                    emailEdit.setEnabled(false);
                    phoneEdit.setEnabled(false);
                    schoolIdEdit.setEnabled(false);

                    // Save the changes to the user's information
                    String newName = nameEdit.getText().toString();
                    String newEmail = emailEdit.getText().toString();
                    String newPhone = phoneEdit.getText().toString();
                    String newSchoolId = schoolIdEdit.getText().toString();

                    // TODO: Save the changes to the user's information in your app's data store

                    // Show submit button and hide edit button
                    submitButton.setVisibility(View.VISIBLE);
                    editButton.setVisibility(View.GONE);

                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hide submit button and show edit button
                submitButton.setVisibility(View.GONE);
                editButton.setVisibility(View.VISIBLE);
            }
        });

        return root;
    }

}