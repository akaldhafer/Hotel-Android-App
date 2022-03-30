package com.example.hotelstars.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelstars.R;
import com.example.hotelstars.userapi.UserModel;

import java.util.ArrayList;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<UserModel> arrayList = new ArrayList<>();

    public CustomerListAdapter(Context context, ArrayList<UserModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CustomerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_customer_list, parent, false);
        CustomerListAdapter.ViewHolder holder = new CustomerListAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String name, email;
        name = arrayList.get(holder.getAdapterPosition()).getName();
        email = arrayList.get(holder.getAdapterPosition()).getEmail();
        //set view
        holder.edName.setText(name);
        holder.edEmail.setText(email);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView edName, edEmail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            edEmail = itemView.findViewById(R.id.ccardEmail);
            edName = itemView.findViewById(R.id.cndName);

        }
    }
}