package com.shorsam.foodyhomeSS;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AddOnRecyclerViewAdapter extends FirebaseRecyclerAdapter<AddOnModel,AddOnRecyclerViewAdapter.MyViewHolder> {

    public AddOnRecyclerViewAdapter(@NonNull FirebaseRecyclerOptions<AddOnModel> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull AddOnModel model) {
        holder.Name.setText(model.getName());
        holder.Price.setText("+"+model.getPrice()+" ONlY");

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.add_on_individual_product_cardview,parent,false);
        return new MyViewHolder(view);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Name,Price;
        Button Add;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Name=itemView.findViewById(R.id.AddOn_Name_IndividualProduct2);
            Price=itemView.findViewById(R.id.AddOn_Price_IndividualProduct2);
            Add=itemView.findViewById(R.id.AddOn_Button_IndividualProduct2);
        }
    }
}
