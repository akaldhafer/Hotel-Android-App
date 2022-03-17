package com.example.hotelstars.bookingapi;

public interface BookingViewMessage {
    void onUpdateFailure(String message);
    void onUpdateSuccess(String message);
}
