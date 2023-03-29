package com.example.bookli;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bookli.databinding.ActivityMainBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.sidesheet.SideSheetDialog;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    EditText timePicker;
    public final String sharedPrefFile = "com.example.android.mainsharedprefs";
    public static final String KEY = "MyKey";
    SharedPreferences mPreferences;
    BottomSheetDialog bottomSheetDialog;

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

        bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_content);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }
}