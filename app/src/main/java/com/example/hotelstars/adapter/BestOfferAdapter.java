package com.example.hotelstars.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelstars.R;
import com.example.hotelstars.activity.RoomPageActivity;
import com.example.hotelstars.activity.UserProfile;
import com.example.hotelstars.roomapi.RoomModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BestOfferAdapter  extends RecyclerView.Adapter<BestOfferAdapter.ViewHolder> {
    private Context context;
    private ArrayList<RoomModel> arrayList = new ArrayList<>();

    public BestOfferAdapter(Context context, ArrayList<RoomModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public BestOfferAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_card, parent, false);
        BestOfferAdapter.ViewHolder holder = new BestOfferAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BestOfferAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String title, description, location,imageUrl;
        int price;

        title = arrayList.get(holder.getAdapterPosition()).getTitle();
        description =arrayList.get(holder.getAdapterPosition()).getDescription();
        location = arrayList.get(holder.getAdapterPosition()).getLocation();
        imageUrl = arrayList.get(position).getImageUrl();
        price = arrayList.get(holder.getAdapterPosition()).getPrice();

        holder.edTitle.setText(title);
        holder.edPrice.setText(String.valueOf(price)+" RM");
        //set the image
        Picasso.with(this.context).load(imageUrl).fit().into(holder.imageView);
        String id= arrayList.get(holder.getAdapterPosition()).getId();
        //delete
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo open product page
                System.out.println("you clicked the view !"+id);
                Intent intent = new Intent(view.getContext(), RoomPageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", id);
                intent.putExtra("title", title);
                intent.putExtra("description", description);
                intent.putExtra("price", price);
                intent.putExtra("imageUrl",imageUrl);
                view.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView edTitle, edPrice;
        private ImageView imageView;
        private CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edTitle = itemView.findViewById(R.id.roomTitle);
            edPrice = itemView.findViewById(R.id.roomPrice);
            cardView = itemView.findViewById(R.id.roomCardView);
            imageView = itemView.findViewById(R.id.roomImage);
        }
    }
}
