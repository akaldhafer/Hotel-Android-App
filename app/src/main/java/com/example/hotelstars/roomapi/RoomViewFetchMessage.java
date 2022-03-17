package com.example.hotelstars.roomapi;

public interface RoomViewFetchMessage {
    void onUpdateSuccess(RoomModel message);
    void onUpdateFailure(String message);

}
