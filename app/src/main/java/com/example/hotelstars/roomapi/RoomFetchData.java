package com.example.hotelstars.roomapi;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.hotelstars.encryption.SymmtCrypto;
import com.example.hotelstars.userapi.UserModel;
import com.example.hotelstars.userapi.UserViewFetchMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class RoomFetchData implements RoomFetchDataPresenter {
    private Context context;
    private RoomViewFetchMessage roomViewFetchMessage;

    public RoomFetchData(Context context, RoomViewFetchMessage roomViewFetchMessage) {
        this.context = context;
        this.roomViewFetchMessage = roomViewFetchMessage;
    }

    @Override
    public void onSuccessUpdate(Activity activity) {
        FirebaseFirestore.getInstance().collection("RoomData")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (int i = 0; i < task.getResult().size(); i++) {
                        String id = task.getResult().getDocuments().get(i).getString("id");
                        String title = task.getResult().getDocuments().get(i).getString("title");
                        String description = task.getResult().getDocuments().get(i).getString("description");
                        String isAvailable = task.getResult().getDocuments().get(i).getString("isAvailable");
                        String location = task.getResult().getDocuments().get(i).getString("location");
                        String imageUrl = task.getResult().getDocuments().get(i).getString("imageUrl");
                        int price =  Integer.parseInt(task.getResult().getDocuments().get(i).get("price").toString());

                        //save them in on object
                        RoomModel roomModel = new RoomModel(id,  title,  description,  isAvailable,  location,  imageUrl,  price);

                        //send object to the activity
                        roomViewFetchMessage.onUpdateSuccess(roomModel);


                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                roomViewFetchMessage.onUpdateFailure(e.getMessage());

            }
        });
    }


}