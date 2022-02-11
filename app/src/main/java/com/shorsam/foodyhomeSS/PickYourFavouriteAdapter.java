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
import com.google.firebase.database.DataSnapshot;

public class PickYourFavouriteAdapter extends FirebaseRecyclerAdapter<CategoryModelTop,PickYourFavouriteAdapter.myViewHolder> {
    private static PickYourFavouriteAdapter.OnItemClickListener listener;
    public PickYourFavouriteAdapter(@NonNull FirebaseRecyclerOptions<CategoryModelTop> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PickYourFavouriteAdapter.myViewHolder holder, int position, @NonNull CategoryModelTop model) {
        holder.Name.setText(model.getName());
        Glide.with(holder.Image.getContext()).load(model.getImage()).into(holder.Image);
    }
    public interface OnItemClickListener{
        void onItemClick(DataSnapshot dataSnapshot, int position);
    }
    public void setOnItemCLickListener(PickYourFavouriteAdapter.OnItemClickListener listener){
        PickYourFavouriteAdapter.listener =listener;

    }
    @NonNull
    @Override
    public PickYourFavouriteAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.pick_your_favourite_cardview,parent,false);
        return new myViewHolder(view);
    }

    public  class myViewHolder extends RecyclerView.ViewHolder {
        TextView Name;
        ImageView Image;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            Name=itemView.findViewById(R.id.TextView_PickYourFavourite);
            Image=itemView.findViewById(R.id.ImageView_PickYourFavourite);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position!=RecyclerView.NO_POSITION && listener!=null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);

                    }
                }
            });
        }
    }
}
