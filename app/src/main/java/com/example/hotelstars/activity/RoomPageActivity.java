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
import com.example.hotelstars.adapter.BestOfferAdapter;
import com.example.hotelstars.roomapi.RoomFetchData;
import com.example.hotelstars.roomapi.RoomModel;
import com.example.hotelstars.roomapi.RoomViewFetchMessage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class RoomPageActivity extends AppCompatActivity implements RoomViewFetchMessage {
    private RecyclerView ListDataView;
    private BestOfferAdapter bestOfferAdapter;
    String title, description, imageUrl,id;
    int price;
    TextView vTitle, vDesc, vPrice;
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

        //get current room
        title = getIntent().getStringExtra("title");
        id = getIntent().getStringExtra("id");
        description = getIntent().getStringExtra("description");
        imageUrl = getIntent().getStringExtra("imageUrl");
        price = getIntent().getIntExtra("price",0);

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
    }

    public void onBookRoom(View view) {
    }

    public void onProfilePage(View view) {
        Intent intent = new Intent(RoomPageActivity.this, UserProfile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }


    @Override
    public void onUpdateFailure(String message) {
        Toast.makeText(RoomPageActivity.this, message, Toast.LENGTH_LONG).show();

    }

    public void onHomePageClick(View view) {
        Intent intent = new Intent(RoomPageActivity.this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}