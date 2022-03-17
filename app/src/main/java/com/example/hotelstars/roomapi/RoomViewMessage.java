package com.example.hotelstars.roomapi;

public interface RoomViewMessage {

    void onUpdateFailure(String message);
    void onUpdateSuccess(String message);
}
