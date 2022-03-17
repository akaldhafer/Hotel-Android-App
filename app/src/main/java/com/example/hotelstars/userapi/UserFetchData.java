package com.example.hotelstars.userapi;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.hotelstars.encryption.SymmtCrypto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class UserFetchData implements UserFetchDataPresenter{
    private Context context;
    private UserViewFetchMessage userViewFetchMessage;

    public UserFetchData(Context context, UserViewFetchMessage userViewFetchMessage) {
        this.context = context;
        this.userViewFetchMessage = userViewFetchMessage;
    }

    @Override
    public void onSuccessUpdate(Activity activity) {
        FirebaseFirestore.getInstance().collection("UserData")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (int i = 0; i < task.getResult().size(); i++) {
                        String name = task.getResult().getDocuments().get(i).getString("name");
                        String email = task.getResult().getDocuments().get(i).getString("email");
                        String password = task.getResult().getDocuments().get(i).getString("password");
                        SymmtCrypto de = new SymmtCrypto();

                        try {
                            UserModel userModel = new UserModel(de.decrypt(name), email,de.decrypt(password));
                            userViewFetchMessage.onUpdateSuccess(userModel);

                        } catch (Exception e) {
                            e.printStackTrace();
                            userViewFetchMessage.onUpdateFailure(e.getMessage());

                        }

                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                userViewFetchMessage.onUpdateFailure(e.getMessage());

            }
        });
    }


}