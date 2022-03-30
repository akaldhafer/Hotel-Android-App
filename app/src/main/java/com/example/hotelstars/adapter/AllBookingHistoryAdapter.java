package com.example.hotelstars.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelstars.R;
import com.example.hotelstars.bookingapi.BookingModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.ArrayList;

public class AllBookingHistoryAdapter extends RecyclerView.Adapter<AllBookingHistoryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<BookingModel> arrayList = new ArrayList<>();

    public AllBookingHistoryAdapter(Context context, ArrayList<BookingModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public AllBookingHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_all_booking_history, parent, false);
        AllBookingHistoryAdapter.ViewHolder holder = new AllBookingHistoryAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AllBookingHistoryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String id, CustomerEmail,roomID, roomTitle, startDate, endDate,status, imageUrl;
        int bookingDays, price, totalPayment;

        roomTitle = arrayList.get(holder.getAdapterPosition()).getRoomTitle();
        id = arrayList.get(holder.getAdapterPosition()).getId();
        CustomerEmail = arrayList.get(holder.getAdapterPosition()).getCustomerEmail();
        startDate =arrayList.get(holder.getAdapterPosition()).getStartDate();
        status = arrayList.get(holder.getAdapterPosition()).getStatus();
        imageUrl = arrayList.get(holder.getAdapterPosition()).getImageUrl();
        price = arrayList.get(holder.getAdapterPosition()).getPrice();
        totalPayment = arrayList.get(holder.getAdapterPosition()).getTotalPayment();
        bookingDays = arrayList.get(holder.getAdapterPosition()).getBookingDays();
        endDate = arrayList.get(holder.getAdapterPosition()).getEndDate();


        //set view
        holder.edendDate.setText(endDate);
        holder.edMail.setText(CustomerEmail);
        holder.edTitle.setText(roomTitle);
        holder.edStatus.setText("Status: "+status);
        holder.edStartDate.setText(new StringBuilder().append("Start Date: ").append(startDate).toString());
        holder.edNights.setText(new StringBuilder().append("Nights: ").append(String.valueOf(bookingDays)).toString());
        holder.edPrice.setText(new StringBuilder().append("Price: ").append(String.valueOf(price)).append(" RM").toString());
        holder.edTotal.setText(new StringBuilder().append("Total: ").append(String.valueOf(totalPayment)).append(" RM").toString());
        //set the image
        Picasso.with(this.context).load(imageUrl).fit().into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView edTitle, edPrice, edTotal, edStatus, edStartDate, edNights,edMail, edendDate;
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edTitle = itemView.findViewById(R.id.hscardTitle);
            edPrice = itemView.findViewById(R.id.hscardPrice);
            imageView = itemView.findViewById(R.id.hscardImage);
            edNights = itemView.findViewById(R.id.hscardDays);
            edStartDate = itemView.findViewById(R.id.hscardStartDate);
            edStatus = itemView.findViewById(R.id.hscardStatus);
            edTotal = itemView.findViewById(R.id.hscardTotalPrice);
            edendDate = itemView.findViewById(R.id.hsendDate);
            edMail = itemView.findViewById(R.id.hscardEmail);


        }
    }
}
