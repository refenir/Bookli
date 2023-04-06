package com.example.bookli.ui.home;

public class RoomModel {
    int image;
    String roomName;
    String capacity;

    public RoomModel(int image, String roomName, String capacity) {
        this.image = image;
        this.roomName = roomName;
        this.capacity = capacity;
    }

    public int getImage() {
        return image;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getCapacity() {
        return capacity;
    }
}
