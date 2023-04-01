package com.example.bookli.ui.booking_confirmation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.bookli.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class BookingConfirmationActivity extends AppCompatActivity {

    String roomName;
    TextView roomTextView;
    String[] selectedTimes = new String[14];
    String selectedDate;
    TextView timesTextView;
    TextView dateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmation);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            roomName = extras.getString("selectedRoom");
            selectedTimes = extras.getStringArray("selectedTimes");
            selectedDate = extras.getString("selectedDate");
        }

        Arrays.sort(selectedTimes, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.valueOf(o1).compareTo(Integer.valueOf(o2));
            }
        });

        roomTextView = findViewById(R.id.textView);
        roomTextView.setText("Room: " + roomName );

        timesTextView = findViewById(R.id.textView7);
        timesTextView.setText("Time: " + Arrays.toString(selectedTimes)
                .replace("[", "")
                .replace("]", ""));

        dateTextView = findViewById(R.id.textView6);
        dateTextView.setText("Date: " + selectedDate);
    }
}