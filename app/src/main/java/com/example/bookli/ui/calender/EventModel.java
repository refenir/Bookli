package com.example.bookli.ui.calender;

public class EventModel {
    //    private String location;
//    private String startTime;
//    private String endTime;
    private String desc;
    public EventModel(String descNew){
        this.desc = descNew;
    }

    public String getDesc() {
        return desc;
    }


//    public EventModel(String locationNew, String timeStart, String timeEnd){
//        this.location = locationNew;
//        this.startTime = timeStart;
//        this.endTime = timeEnd;

//    public String getLocation(){
//        return location;
//    }

//    public String getStartTime() {
//        String editedStart = this.startTime.charAt(0) + this.startTime.charAt(1)+":00";
//        return startTime;
//    }

//    public String getEndTime() {
//        String editedEnd = this.endTime.charAt(0) + this.endTime.charAt(1)+":00";
//        return endTime;
//    }


}
