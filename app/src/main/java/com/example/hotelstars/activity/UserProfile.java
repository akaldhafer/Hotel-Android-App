package com.example.hotelstars.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelstars.R;
import com.example.hotelstars.auth.LoginPage;
import com.example.hotelstars.encryption.SymmtCrypto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class UserProfile extends AppCompatActivity {

    TextView vEmail;
    EditText edName, edPassword;
    String email, name, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide(); //This Line hides the action bar
        setContentView(R.layout.activity_user_profile);
        vEmail  =findViewById(R.id.profileEmail);
        edName  =findViewById(R.id.profile_Name);
        edPassword  =findViewById(R.id.profile_Password);

        //fetch current user information
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        email = Objects.requireNonNull(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail()).trim();
        vEmail.setText(email);
        //fetch user name
        FirebaseFirestore.getInstance().collection("UserData").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    SymmtCrypto crypto = new SymmtCrypto();
                    try {
                        name =crypto.decrypt(task.getResult().getString("name"));
                        password = crypto.decrypt(task.getResult().getString("password"));

                        edName.setText(name);
                        edPassword.setText(password);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserProfile.this, "Check your internet Connection !", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onMenuClick(View view) {
        Intent intent = new Intent(UserProfile.this, UserMenu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void validation(){
        String Name = edName.getText().toString().trim();
        String Password=  edPassword.getText().toString().trim();

        if(!TextUtils.isEmpty(Name)
                && !TextUtils.isEmpty(Password) && Password.length() >5){
            //send info
            updateUserInfo(Name,Password);

        }else{
            if(TextUtils.isEmpty(Name)){
                edName.setError("Name is required");
                return;
            }
            if(TextUtils.isEmpty(Password)){
                edPassword.setError("Password is required and must be more than 6c");
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void updateUserInfo(String Name, String Password){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference record = firebaseFirestore.collection("UserData").document(email);
        SymmtCrypto d = new SymmtCrypto();

        try {
            String updatedName = d.encrypt(Name);
            String updatedPassword = d.encrypt(Password);
            record.update("name",updatedName, "password", updatedPassword)
                    .addOnSuccessListener(new OnSuccessListener< Void >() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(UserProfile.this, "Profile updated !", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserProfile.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickUpdateButton(View view) {
        validation();
    }

    public void onClickLogOutButton(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(UserProfile.this, LoginPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UserProfile.this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void onHomePage(View view) {
        Intent intent = new Intent(UserProfile.this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}