package com.example.bookli.ui.booking_confirmation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.bookli.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BookingConfirmationActivity extends AppCompatActivity {

    String roomName;
    TextView roomTextView;
    ArrayList<String> selectedTimes = new ArrayList<>();
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
            selectedTimes = extras.getStringArrayList("selectedTimes");
            selectedDate = extras.getString("selectedDate");
        }

        roomTextView = findViewById(R.id.textView);
        roomTextView.setText("Room: " + roomName );

        timesTextView = findViewById(R.id.textView7);
        timesTextView.setText("Time: " + selectedTimes);

        dateTextView = findViewById(R.id.textView6);
        dateTextView.setText("Date: " + selectedDate);
    }
}