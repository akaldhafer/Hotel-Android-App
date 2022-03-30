package com.example.hotelstars.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelstars.R;
import com.example.hotelstars.bookingapi.BookingModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CurrentBookingAdapter extends RecyclerView.Adapter<CurrentBookingAdapter.ViewHolder> {
    private Context context;
    private ArrayList<BookingModel> arrayList = new ArrayList<>();

    public CurrentBookingAdapter(Context context, ArrayList<BookingModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CurrentBookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_current_booking, parent, false);
        CurrentBookingAdapter.ViewHolder holder = new CurrentBookingAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentBookingAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String id, CustomerEmail,roomID, roomTitle, startDate, endDate,status, imageUrl;
        int bookingDays, price, totalPayment;

        roomTitle = arrayList.get(holder.getAdapterPosition()).getRoomTitle();
        id = arrayList.get(holder.getAdapterPosition()).getId();
        startDate =arrayList.get(holder.getAdapterPosition()).getStartDate();
        status = arrayList.get(holder.getAdapterPosition()).getStatus();
        imageUrl = arrayList.get(holder.getAdapterPosition()).getImageUrl();
        price = arrayList.get(holder.getAdapterPosition()).getPrice();
        endDate = arrayList.get(holder.getAdapterPosition()).getEndDate();
        totalPayment = arrayList.get(holder.getAdapterPosition()).getTotalPayment();
        bookingDays = arrayList.get(holder.getAdapterPosition()).getBookingDays();

        //set view
        holder.edTitle.setText(roomTitle);
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
        private TextView edTitle, edPrice, edTotal, edStartDate, edNights;
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edTitle = itemView.findViewById(R.id.ccardTitle);
            edPrice = itemView.findViewById(R.id.ccardPrice);
            imageView = itemView.findViewById(R.id.ccardImage);
            edNights = itemView.findViewById(R.id.ccardDays);
            edStartDate = itemView.findViewById(R.id.ccardStartDate);
            edTotal = itemView.findViewById(R.id.ccardTotalPrice);

        }
    }
}