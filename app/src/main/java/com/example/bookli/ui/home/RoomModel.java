package com.example.bookli.ui.home;

public class RoomModel {
    int image;
    String roomName;
    String capacity;
    Boolean available;

    public RoomModel(int image, String roomName, String capacity, Boolean available) {
        this.image = image;
        this.roomName = roomName;
        this.capacity = capacity;
        this.available = available;
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

    public Boolean getAvailable() {
        return available;
    }
}
