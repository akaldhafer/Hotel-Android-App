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

import java.util.ArrayList;

public class ApproveBookingAdapter  extends RecyclerView.Adapter<ApproveBookingAdapter.ViewHolder> {
    private Context context;
    private ArrayList<BookingModel> arrayList = new ArrayList<>();

    public ApproveBookingAdapter(Context context, ArrayList<BookingModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ApproveBookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_approve_booking, parent, false);
        ApproveBookingAdapter.ViewHolder holder = new ApproveBookingAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ApproveBookingAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String id, CustomerEmail,roomID, roomTitle, startDate, endDate,status, imageUrl;
        int bookingDays, price, totalPayment;

        roomTitle = arrayList.get(holder.getAdapterPosition()).getRoomTitle();
        id = arrayList.get(holder.getAdapterPosition()).getId();
        CustomerEmail = arrayList.get(holder.getAdapterPosition()).getCustomerEmail();
        startDate =arrayList.get(holder.getAdapterPosition()).getStartDate();
        status = arrayList.get(holder.getAdapterPosition()).getStatus();
        imageUrl = arrayList.get(holder.getAdapterPosition()).getImageUrl();
        price = arrayList.get(holder.getAdapterPosition()).getPrice();
        endDate = arrayList.get(holder.getAdapterPosition()).getEndDate();
        totalPayment = arrayList.get(holder.getAdapterPosition()).getTotalPayment();
        bookingDays = arrayList.get(holder.getAdapterPosition()).getBookingDays();

        //set view
        holder.edMail.setText(CustomerEmail);
        holder.edTitle.setText(roomTitle);
        holder.edStatus.setText("Status: "+status);
        holder.edStartDate.setText(new StringBuilder().append("Start Date: ").append(startDate).toString());
        holder.edNights.setText(new StringBuilder().append("Nights: ").append(String.valueOf(bookingDays)).toString());
        holder.edPrice.setText(new StringBuilder().append("Price: ").append(String.valueOf(price)).append(" RM").toString());
        holder.edTotal.setText(new StringBuilder().append("Total: ").append(String.valueOf(totalPayment)).append(" RM").toString());
        //set the image
        Picasso.with(this.context).load(imageUrl).fit().into(holder.imageView);
        //cancel booking
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                DocumentReference record = firebaseFirestore.collection("BookingData").document(id);
                record.delete().addOnSuccessListener(new OnSuccessListener< Void >() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(view.getContext(), "Booking Canceled", Toast.LENGTH_LONG).show();
                        //delete from the ui
                        arrayList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        notifyItemRangeChanged(holder.getAdapterPosition(), arrayList.size());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(view.getContext(), e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        //approve
        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                DocumentReference record = firebaseFirestore.collection("BookingData").document(id);
                record.update("status", "accepted").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(view.getContext(), "Booking Approved", Toast.LENGTH_LONG).show();
                        //delete from the ui
                        arrayList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        notifyItemRangeChanged(holder.getAdapterPosition(), arrayList.size());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(view.getContext(), e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView edTitle, edPrice, edTotal, edStatus, edStartDate, edNights,edMail;
        private ImageView imageView;
        private Button cancel, approve;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edTitle = itemView.findViewById(R.id.apcardTitle);
            edPrice = itemView.findViewById(R.id.apcardPrice);
            imageView = itemView.findViewById(R.id.apcardImage);
            edNights = itemView.findViewById(R.id.apcardDays);
            edStartDate = itemView.findViewById(R.id.apcardStartDate);
            edStatus = itemView.findViewById(R.id.apcardStatus);
            edTotal = itemView.findViewById(R.id.apcardTotalPrice);
            edMail = itemView.findViewById(R.id.apcardEmail);
            cancel = itemView.findViewById(R.id.apcardCancel);
            approve = itemView.findViewById(R.id.apcardApprove);

        }
    }
}
