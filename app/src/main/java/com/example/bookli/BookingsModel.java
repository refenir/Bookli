package com.example.bookli;

public class BookingsModel {
    String date;
    String startTime;
    String endTime;
    int studentId;
    int roomId;
    int bookingId;

    public BookingsModel(){
    }
    public BookingsModel(String date, String startTime, String endTime, int studentId, int roomId) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.studentId = studentId;
        this.roomId = roomId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int booking) {this.bookingId = bookingId;}
}
