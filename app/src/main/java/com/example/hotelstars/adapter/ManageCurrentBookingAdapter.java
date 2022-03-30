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

public class ManageCurrentBookingAdapter extends RecyclerView.Adapter<ManageCurrentBookingAdapter.ViewHolder> {
    private Context context;
    private ArrayList<BookingModel> arrayList = new ArrayList<>();

    public ManageCurrentBookingAdapter(Context context, ArrayList<BookingModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ManageCurrentBookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_manage_current_booking, parent, false);
        ManageCurrentBookingAdapter.ViewHolder holder = new ManageCurrentBookingAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ManageCurrentBookingAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
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
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        endDate = timestamp.toString();

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

        //approve
        holder.checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                DocumentReference record = firebaseFirestore.collection("BookingData").document(id);
                record.update("status", "checkedOut", "endDate", endDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(view.getContext(), "Booking Checked out", Toast.LENGTH_LONG).show();
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
        private Button checkout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edTitle = itemView.findViewById(R.id.mcardTitle);
            edPrice = itemView.findViewById(R.id.mcardPrice);
            imageView = itemView.findViewById(R.id.mcardImage);
            edNights = itemView.findViewById(R.id.mcardDays);
            edStartDate = itemView.findViewById(R.id.mcardStartDate);
            edStatus = itemView.findViewById(R.id.mcardStatus);
            edTotal = itemView.findViewById(R.id.mcardTotalPrice);
            edMail = itemView.findViewById(R.id.mcardEmail);
            checkout = itemView.findViewById(R.id.mcardCheckout);

        }
    }
}
