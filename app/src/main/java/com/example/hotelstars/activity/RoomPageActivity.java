package com.example.hotelstars.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hotelstars.R;
import com.example.hotelstars.adapter.BestOfferAdapter;
import com.example.hotelstars.bookingapi.BookingUploadData;
import com.example.hotelstars.bookingapi.BookingViewMessage;
import com.example.hotelstars.roomapi.RoomFetchData;
import com.example.hotelstars.roomapi.RoomModel;
import com.example.hotelstars.roomapi.RoomViewFetchMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class RoomPageActivity extends AppCompatActivity implements RoomViewFetchMessage, BookingViewMessage {
    private RecyclerView ListDataView;
    private BestOfferAdapter bestOfferAdapter;
    private DatePicker datePicker;
    private Calendar calendar;
    private int difference;
    BookingUploadData uploadData;

    private int year, month, day;
    private int Cyear, Cmonth, Cday;
    String title, description, imageUrl,id;
    int price;
    TextView vTitle, vDesc, vPrice, vStartDate, vNights;
    ImageView imageView;
    ArrayList<RoomModel> roomModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide (); //This Line hides the action bar
        setContentView(R.layout.activity_room_page);
        vTitle = findViewById(R.id.proomTitle);
        vDesc = findViewById(R.id.pDesc);
        vPrice = findViewById(R.id.proomPrice);
        imageView = findViewById(R.id.pRoomImage);
        vStartDate =findViewById(R.id.pstartDate);
        vNights= findViewById(R.id.pNights);

        uploadData = new BookingUploadData(this);
        //get current room
        title = getIntent().getStringExtra("title");
        id = getIntent().getStringExtra("id");
        description = getIntent().getStringExtra("description");
        imageUrl = getIntent().getStringExtra("imageUrl");
        price = getIntent().getIntExtra("price",0);

        //
        calendar = Calendar.getInstance();
        Cyear = calendar.get(Calendar.YEAR);
        Cmonth = calendar.get(Calendar.MONTH);
        Cday = calendar.get(Calendar.DAY_OF_MONTH);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        //showDate(year, month+1, day);

        //handle start date
        vStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(999);
                Toast.makeText(getApplicationContext(), "Enter Start Date", Toast.LENGTH_SHORT).show();
            }
        });
        //set the current room view
        vTitle.setText(title);
        vDesc.setText(description);
        vPrice.setText(price+" RM");
        Picasso.with(this).load(imageUrl).fit().into(imageView);
        //get the similar room
        ListDataView = findViewById(R.id.SimilarListView);

        RoomFetchData roomFetchData = new RoomFetchData(this, this);

        RecyclerViewMethod();
        roomFetchData.onSuccessUpdate(this);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    int currentDate = ((Cyear*12*365)+(Cmonth*30)+Cday);
                    int pickDate = ((arg1*12*365)+(arg2*30)+arg3);
                    difference = pickDate - currentDate;
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        vStartDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    public void RecyclerViewMethod() {

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        ListDataView.setLayoutManager(manager);
        ListDataView.setItemAnimator(new DefaultItemAnimator());
        ListDataView.setHasFixedSize(true);

        bestOfferAdapter = new BestOfferAdapter(this, roomModelArrayList);
        ListDataView.setAdapter(bestOfferAdapter);
        ListDataView.invalidate();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onUpdateSuccess(RoomModel message) {
        if(message != null && !message.getId().equals(id)){
            RoomModel roomModel = new RoomModel(message.getId(),message.getTitle(),message.getDescription(),message.getIsAvailable(),
                    message.getLocation(),message.getImageUrl(),message.getPrice());
            roomModelArrayList.add(roomModel);

        }
        bestOfferAdapter.notifyDataSetChanged();
    }
    public void onViewAllItems(View view) {
        Intent intent = new Intent(RoomPageActivity.this, ViewAllRoom.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void onBookRoom(View view) {
        validate();
    }

    public void onProfilePage(View view) {
        Intent intent = new Intent(RoomPageActivity.this, UserProfile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void validate(){
        String bid, customerEmail,roomID, roomTitle, startDate, endDate,status;
        int bookingDays, totalPayment;
        Timestamp timestamp= new Timestamp(System.currentTimeMillis());

        bid = timestamp.toString();
        customerEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        roomID = id.trim();
        roomTitle = title.trim();
        startDate = vStartDate.getText().toString().trim();
        endDate = "";
        status = "requested";
        String Days = vNights.getText().toString().trim();
        if(TextUtils.isEmpty(Days)){
            vNights.setError("Enter Number of nights");
        }else{
            bookingDays = Integer.parseInt(Days);
            if(!TextUtils.isEmpty(startDate) && bookingDays >0 && difference >=0){
                totalPayment = price*bookingDays;
                uploadData.onSuccessUpdate(this,bid, customerEmail,roomID, roomTitle, startDate,
                        endDate,status,imageUrl,bookingDays, price, totalPayment);
            }
            else{
                if(TextUtils.isEmpty(startDate)){
                    vStartDate.setError("Please Enter Start Date");
                    return;
                }
                if (difference <0){
                    vStartDate.setError("The date cannot be older than the current date!");
                    Toast.makeText(RoomPageActivity.this, "Date cannot be older than the current date!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (bookingDays <=0){
                    vNights.setError("Enter more than zero");
                    Toast.makeText(RoomPageActivity.this, "Enter more than zero!", Toast.LENGTH_SHORT).show();
                }

            }
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RoomPageActivity.this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    public void onUpdateFailure(String message) {
        Toast.makeText(RoomPageActivity.this, message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onUpdateSuccess(String message) {
        Toast.makeText(RoomPageActivity.this, message, Toast.LENGTH_LONG).show();
        vNights.setText(null);
        vStartDate.setText(null);
    }

    public void onHomePageClick(View view) {
        Intent intent = new Intent(RoomPageActivity.this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}