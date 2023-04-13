package com.example.bookli.ui.calendar;

public class EventModel {
    //    private String location;
//    private String startTime;
//    private String endTime;
    private String desc;
    private int bookingId;
    public EventModel(String descNew, int bookingId){
        this.desc = descNew;
        this.bookingId = bookingId;
    }

    public String getDesc() {
        return desc;
    }

    public int getBookingId() {
        return bookingId;
    }

}
