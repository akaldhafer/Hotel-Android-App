package com.example.hotelstars.bookingapi;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class BookingFetchData implements BookingFetchDataPresenter {
    private Context context;
    private BookingViewFetchMessage viewFetchMessage;

    public BookingFetchData(Context context, BookingViewFetchMessage viewFetchMessage) {
        this.context = context;
        this.viewFetchMessage = viewFetchMessage;
    }

    @Override
    public void onSuccessUpdate(Activity activity) {
        FirebaseFirestore.getInstance().collection("BookingData")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (int i = 0; i < task.getResult().size(); i++) {
                        String id = task.getResult().getDocuments().get(i).getString("id");
                        String customerEmail = task.getResult().getDocuments().get(i).getString("customerEmail");
                        String roomID = task.getResult().getDocuments().get(i).getString("roomID");
                        String roomTitle = task.getResult().getDocuments().get(i).getString("roomTitle");
                        String startDate = task.getResult().getDocuments().get(i).getString("startDate");
                        String endDate = task.getResult().getDocuments().get(i).getString("endDate");
                        String status = task.getResult().getDocuments().get(i).getString("status");
                        String imageUrl = task.getResult().getDocuments().get(i).getString("imageUrl");

                        int bookingDays =  Integer.parseInt(task.getResult().getDocuments().get(i).get("bookingDays").toString());
                        int price =  Integer.parseInt(task.getResult().getDocuments().get(i).get("price").toString());

                        int totalPayment =  Integer.parseInt(task.getResult().getDocuments().get(i).get("totalPayment").toString());

                        //save them in on object
                        BookingModel bookingModel = new BookingModel(id,  customerEmail,  roomID, roomTitle,
                                startDate,  endDate,  status,imageUrl, bookingDays,  price,  totalPayment);

                        //send object to the activity
                        viewFetchMessage.onUpdateSuccess(bookingModel);


                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                viewFetchMessage.onUpdateFailure(e.getMessage());

            }
        });
    }


}