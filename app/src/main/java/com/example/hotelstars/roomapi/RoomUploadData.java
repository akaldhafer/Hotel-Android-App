package com.example.hotelstars.roomapi;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class RoomUploadData implements RoomDataPresenter, RoomViewMessage {

    RoomViewMessage roomViewMessage;

    public RoomUploadData(RoomViewMessage roomViewMessage) {
        this.roomViewMessage = roomViewMessage;
    }

    //to upload to the firebase
    @Override
    public void onSuccessUpdate(Activity activity, String id, String title, String description, String isAvailable, String location, String imageUrl, int price) {

        RoomModel roomModel = new RoomModel( id,  title,  description,  isAvailable,  location,  imageUrl,  price);
        FirebaseFirestore.getInstance().collection("RoomData").document(id)
                .set(roomModel, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            roomViewMessage.onUpdateSuccess("Added Successfully");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                roomViewMessage.onUpdateFailure("Something went wrong");
            }
        });
    }

    @Override
    public void onUpdateFailure(String message) {
        roomViewMessage.onUpdateFailure(message);
    }

    @Override
    public void onUpdateSuccess(String message) {
        roomViewMessage.onUpdateSuccess(message);
    }
}
