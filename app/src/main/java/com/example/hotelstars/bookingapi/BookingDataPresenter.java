package com.example.hotelstars.bookingapi;

import android.app.Activity;

public interface BookingDataPresenter {
    void onSuccessUpdate(Activity activity, String id, String customerEmail, String roomID,
                         String roomTitle, String startDate, String endDate, String status, String imageUrl,
                         int bookingDays, int price, int totalPayment);

}
