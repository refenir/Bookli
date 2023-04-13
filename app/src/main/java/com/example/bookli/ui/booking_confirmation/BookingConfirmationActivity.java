package com.example.bookli.ui.booking_confirmation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookli.BookingDataService;
import com.example.bookli.MainActivity;
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
    ImageView imageView;
    int roomImage;
    int bookingId;
    Button delete;
    Button share;
    BookingDataService bookingDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookingDataService = new BookingDataService(this);
        setContentView(R.layout.activity_booking_confirmation);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            roomName = extras.getString("selectedRoom");
            selectedTimes = extras.getStringArray("selectedTimes");
            selectedDate = extras.getString("selectedDate");
            roomImage = extras.getInt("image");
            bookingId = extras.getInt("bookingId");
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
        String startTime = selectedTimes[0];
        String endTime = selectedTimes[selectedTimes.length - 1];
        int endTimeInt = Integer.parseInt(endTime) + 100;
        if (endTimeInt < 1000) {
            endTime = "0" + endTimeInt;
        } else {
            endTime = String.valueOf(endTimeInt);
        }
        String duration = startTime + " - " + endTime;
        timesTextView.setText("Time: " + duration);

        dateTextView = findViewById(R.id.textView6);
        dateTextView.setText("Date: " + selectedDate);

        imageView = findViewById(R.id.imageView);
        imageView.setImageResource(roomImage);

        delete = findViewById(R.id.delete_button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookingDataService.deleteBooking(bookingId, new BookingDataService.DeleteBookingResponseListener() {
                    @Override
                    public void onError(String msg) {
                        Toast.makeText(BookingConfirmationActivity.this, "delete failed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse() {
                        Toast.makeText(BookingConfirmationActivity.this, "delete successful", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });

        share = findViewById(R.id.share_button1);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String durationLook = roomTextView.getText().toString().substring(6) + " from "
                        + timesTextView.getText().toString().substring(6);
                String shareBody = "Hi! We have an upcoming library booking at " + durationLook;
                intent.putExtra(Intent.EXTRA_SUBJECT, shareBody);
                startActivity(Intent.createChooser(intent, "Share using"));
            }
        });
    }
}