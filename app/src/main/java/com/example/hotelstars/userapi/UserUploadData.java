package com.example.hotelstars.userapi;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class UserUploadData implements UserDataPresenter, UserViewMessage {

    UserViewMessage userViewMessage;

    public UserUploadData(UserViewMessage userViewMessage) {
        this.userViewMessage = userViewMessage;
    }

    //to upload to the firebase
    @Override
    public void onSuccessUpdate(Activity activity, String name,
                                String email, String password) {
        UserModel userModel = new UserModel(name, email, password);
        FirebaseFirestore.getInstance().collection("UserData").document(email)
                .set(userModel, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful()){
                           userViewMessage.onUpdateSuccess("Registered Successfully");
                       }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                userViewMessage.onUpdateFailure("Something went wrong");
            }
        });
    }

    @Override
    public void onUpdateFailure(String message) {
       userViewMessage.onUpdateFailure(message);
    }

    @Override
    public void onUpdateSuccess(String message) {
        userViewMessage.onUpdateSuccess(message);
    }
}
