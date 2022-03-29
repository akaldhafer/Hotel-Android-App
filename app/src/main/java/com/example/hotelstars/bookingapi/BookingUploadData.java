package com.example.hotelstars.bookingapi;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class BookingUploadData implements BookingDataPresenter, BookingViewMessage {

    BookingViewMessage bookingViewMessage;

    public BookingUploadData(BookingViewMessage bookingViewMessage) {
        this.bookingViewMessage = bookingViewMessage;
    }

    //to upload to the firebase
    @Override
    public void onSuccessUpdate(Activity activity, String id, String customerEmail, String roomID,
                                String roomTitle, String startDate, String endDate, String status, String imageUrl,
                                int bookingDays, int price, int totalPayment) {

        BookingModel bookingModel = new BookingModel( id,  customerEmail,  roomID,
                 roomTitle,  startDate,  endDate,  status, imageUrl,bookingDays,  price,  totalPayment);

        FirebaseFirestore.getInstance().collection("BookingData").document(id)
                .set(bookingModel, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            bookingViewMessage.onUpdateSuccess("Booked Successfully, check your booking request for update");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                bookingViewMessage.onUpdateFailure("Something went wrong");
            }
        });
    }

    @Override
    public void onUpdateFailure(String message) {
        bookingViewMessage.onUpdateFailure(message);
    }

    @Override
    public void onUpdateSuccess(String message) {
        bookingViewMessage.onUpdateSuccess(message);
    }
}
