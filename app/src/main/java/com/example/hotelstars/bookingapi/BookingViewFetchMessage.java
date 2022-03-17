package com.example.hotelstars.bookingapi;

public interface BookingViewFetchMessage {
    void onUpdateSuccess(BookingModel message);
    void onUpdateFailure(String message);
}
