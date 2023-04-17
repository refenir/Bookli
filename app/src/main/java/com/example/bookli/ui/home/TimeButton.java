package com.example.bookli.ui.home;

import android.widget.Button;

public class TimeButton {
    private Button timeButton;
    private int position;


    public TimeButton(Button timeButton, int position) {
        this.timeButton = timeButton;
        this.position = position;
    }

    public Button getTimeButton() {
        return timeButton;
    }
    public int getPosition() { return position; }
}
