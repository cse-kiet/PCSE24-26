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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class CategoryTopAdapter extends FirebaseRecyclerAdapter<CategoryModelTop,CategoryTopAdapter.myViewHolder> {
    public CategoryTopAdapter(@NonNull FirebaseRecyclerOptions<CategoryModelTop> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CategoryTopAdapter.myViewHolder holder, int position, @NonNull CategoryModelTop model) {
        holder.Name.setText(model.getName());
        Glide.with(holder.Image.getContext()).load(model.getImage()).into(holder.Image);

    }

    @NonNull
    @Override
    public CategoryTopAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card_design1,parent,false);
        return new myViewHolder(view);
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        TextView Name;
        ImageView Image;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            Name=itemView.findViewById(R.id.Category_card_Name_MainActivity2);
            Image=itemView.findViewById(R.id.Category_cardImage_MainActivity2);
        }
    }
}
