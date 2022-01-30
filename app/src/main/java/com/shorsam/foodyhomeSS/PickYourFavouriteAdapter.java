package com.shorsam.foodyhomeSS;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class PickYourFavouriteAdapter extends FirebaseRecyclerAdapter<CategoryModelTop,PickYourFavouriteAdapter.myViewHolder> {

    public PickYourFavouriteAdapter(@NonNull FirebaseRecyclerOptions<CategoryModelTop> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PickYourFavouriteAdapter.myViewHolder holder, int position, @NonNull CategoryModelTop model) {
        holder.Name.setText(model.getName());
        Glide.with(holder.Image.getContext()).load(model.getImage()).into(holder.Image);
    }

    @NonNull
    @Override
    public PickYourFavouriteAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pick_your_favourite_cardview,parent,false);
        return new myViewHolder(view);
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        TextView Name;
        ImageView Image;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            Name=itemView.findViewById(R.id.TextView_PickYourFavourite);
            Image=itemView.findViewById(R.id.ImageView_PickYourFavourite);
        }
    }
}
