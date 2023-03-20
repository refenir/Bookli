package com.example.bookli;

public class RoomModel {
    int image;
    String roomName;

    public RoomModel(int image, String roomName) {
        this.image = image;
        this.roomName = roomName;
    }

    public int getImage() {
        return image;
    }

    public String getRoomName() {
        return roomName;
    }
}
