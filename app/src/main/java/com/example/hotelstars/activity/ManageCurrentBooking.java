package com.example.hotelstars.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelstars.R;
import com.example.hotelstars.adapter.ApproveBookingAdapter;
import com.example.hotelstars.adapter.ManageCurrentBookingAdapter;
import com.example.hotelstars.bookingapi.BookingFetchData;
import com.example.hotelstars.bookingapi.BookingModel;
import com.example.hotelstars.bookingapi.BookingViewFetchMessage;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Objects;

public class ManageCurrentBooking extends AppCompatActivity implements BookingViewFetchMessage {
    private RecyclerView ListDataView;
    private ManageCurrentBookingAdapter Adapter;
    ImageView menu;
    TextView title;

    ArrayList<BookingModel> roomModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide (); //This Line hides the action bar
        setContentView(R.layout.admin_list_view);
        title = findViewById(R.id.pageTitle);

        menu = findViewById(R.id.onMenu);
        title.setText("Current Booking List");

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageCurrentBooking.this, AdminPanel.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });


        ListDataView = findViewById(R.id.AdminListView);

        BookingFetchData roomFetchData = new BookingFetchData(this, this);

        RecyclerViewMethod();
        roomFetchData.onSuccessUpdate(this);

    }
    public void RecyclerViewMethod() {

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        ListDataView.setLayoutManager(manager);
        ListDataView.setItemAnimator(new DefaultItemAnimator());
        ListDataView.setHasFixedSize(true);

//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
//        ListDataView.setLayoutManager(mLayoutManager);

        Adapter = new ManageCurrentBookingAdapter(this, roomModelArrayList);
        ListDataView.setAdapter(Adapter);
        ListDataView.invalidate();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onUpdateSuccess(BookingModel message) {
        if(message != null &&message.getStatus().equals("accepted")){
            BookingModel roomModel = new BookingModel(message.getId(),message.getCustomerEmail(),
                    message.getRoomID(), message.getRoomTitle(), message.getStartDate(),
                    message.getEndDate(),message.getStatus(),message.getImageUrl(),
                    message.getBookingDays(),message.getPrice(),message.getTotalPayment());
            roomModelArrayList.add(roomModel);

        }
        Adapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ManageCurrentBooking.this, AdminPanel.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onUpdateFailure(String message) {
        Toast.makeText(ManageCurrentBooking.this, message, Toast.LENGTH_LONG).show();

    }
}
