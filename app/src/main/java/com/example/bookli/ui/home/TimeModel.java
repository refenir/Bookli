package com.example.bookli.ui.home;

public class TimeModel {
    private String time;
    private boolean available;

    public TimeModel(String time) {
        this.time = time;
        this.available = false;
    }

    public TimeModel(String time, boolean available) {
        this.time = time;
        this.available = available;
    }

    public String getTime() {
        return time;
    }
    public boolean getAvailability() { return available; }

}
