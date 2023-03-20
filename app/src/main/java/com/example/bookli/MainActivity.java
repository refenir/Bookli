package com.example.bookli;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.bookli.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookli.databinding.ActivityMainBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    EditText datePicker;
    EditText timePicker;
    public final String sharedPrefFile = "com.example.android.mainsharedprefs";
    public static final String KEY = "MyKey";
    SharedPreferences mPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        getSupportActionBar().hide();


        // change date
        datePicker = findViewById(R.id.edit_date);
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateCalendar();
            }

            private void updateCalendar() {
                String Format = "dd/MM/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.US);

                datePicker.setText(sdf.format(calendar.getTime()));
            }
        };

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(MainActivity.this, date, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        // change time
//        timePicker = findViewById(R.id.edit_time);
//
//        TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
//                calendar.set(Calendar.HOUR, hour);
//                calendar.set(Calendar.MINUTE, minute);
//
//                updateCalendar();
//            }
//
//            private void updateCalendar() {
//                String Format = "HH.mm";
//                SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.US);
//
//                timePicker.setText(sdf.format(calendar.getTime()));
//            }
//        };
//
//        timePicker.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                new TimePickerDialog(MainActivity.this, time, calendar.get(Calendar.HOUR),
//                        calendar.get(Calendar.MINUTE), false).show();
//            }
//        });

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        // app is already in use, or app is used for the first time
        String timeText = mPreferences.getString(KEY, "");

    }
    // store previous inputs and show after closing and reopening the app (shared preferences)
    @Override
    protected void onPause(){
        super.onPause();
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.apply();
    }
}