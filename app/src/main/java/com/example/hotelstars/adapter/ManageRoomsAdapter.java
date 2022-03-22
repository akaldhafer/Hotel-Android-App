package com.example.hotelstars.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelstars.R;
import com.example.hotelstars.activity.AddRoomActivity;
import com.example.hotelstars.roomapi.RoomModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ManageRoomsAdapter extends RecyclerView.Adapter<ManageRoomsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<RoomModel> arrayList = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;

    public ManageRoomsAdapter(Context context, ArrayList<RoomModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ManageRoomsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_room_card, parent, false);
        ManageRoomsAdapter.ViewHolder holder = new ManageRoomsAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ManageRoomsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String title, description, location,imageUrl;
        int price;

        title = arrayList.get(holder.getAdapterPosition()).getTitle();
        description =arrayList.get(holder.getAdapterPosition()).getDescription();
        location = arrayList.get(holder.getAdapterPosition()).getLocation();
        imageUrl = arrayList.get(position).getImageUrl();
        price = arrayList.get(holder.getAdapterPosition()).getPrice();

        holder.edTitle.setText(title);
        holder.edDesc.setText(description);
        holder.edLocation.setText(location);
        holder.edPrice.setText(String.valueOf(price));
        //set the image
        Picasso.with(this.context).load(imageUrl).fit().into(holder.imageView);
        String id= arrayList.get(holder.getAdapterPosition()).getId();
        //delete
        holder.delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                firebaseFirestore = FirebaseFirestore.getInstance();
                DocumentReference record = firebaseFirestore.collection("RoomData").document(id);
                record.delete().addOnSuccessListener(new OnSuccessListener< Void >() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(v.getContext(), "Room Deleted", Toast.LENGTH_LONG).show();
                        //delete from the ui
                        arrayList.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        notifyItemRangeChanged(holder.getAdapterPosition(), arrayList.size());

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(v.getContext(), e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        //update
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = holder.edTitle.getText().toString();
                String description =holder.edDesc.getText().toString();
                String location = holder.edLocation.getText().toString();
                int price = Integer.parseInt(holder.edPrice.getText().toString());

                if(!TextUtils.isEmpty(title)
                        && !TextUtils.isEmpty(description)
                        && !TextUtils.isEmpty(location)){

                    firebaseFirestore = FirebaseFirestore.getInstance();
                    DocumentReference record = firebaseFirestore.collection("RoomData").document(id);
                    record.update("title",title, "description",description,
                            "location",location, "price", price).addOnSuccessListener(new OnSuccessListener< Void >() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(view.getContext(), "Room Updated", Toast.LENGTH_LONG).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(view.getContext(), e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    if(TextUtils.isEmpty(title)){
                        holder.edTitle.setError("Title is required");
                        return;
                    }if (TextUtils.isEmpty(description)){
                        holder.edDesc.setError("Description is required");
                        return;
                    }
                    if (TextUtils.isEmpty(location)){
                        holder.edLocation.setError("Location is required");
                        return;
                    }
                    if (holder.edPrice.getText() == null){
                        holder.edPrice.setError("Price is required");
                    }
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private EditText edTitle, edDesc, edLocation, edPrice;
        private Button delete, update;
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edTitle = itemView.findViewById(R.id.vcardTitle);
            edDesc = itemView.findViewById(R.id.vcardDescription);
            edLocation = itemView.findViewById(R.id.vcardLocation);
            edPrice = itemView.findViewById(R.id.vcardPrice);
            update = itemView.findViewById(R.id.vcardUpdate);
            delete = itemView.findViewById(R.id.vcardDelete);
            imageView = itemView.findViewById(R.id.vcardImage);
        }
    }
}