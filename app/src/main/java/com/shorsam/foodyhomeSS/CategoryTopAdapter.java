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

public class CategoryTopAdapter extends FirebaseRecyclerAdapter<CategoryModelTop,CategoryTopAdapter.myViewHolder> {
    private static CategoryTopAdapter.OnItemClickListener listener;

    public CategoryTopAdapter(@NonNull FirebaseRecyclerOptions<CategoryModelTop> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CategoryTopAdapter.myViewHolder holder, int position, @NonNull CategoryModelTop model) {
        holder.Name.setText(model.getName());
        Glide.with(holder.Image.getContext()).load(model.getImage()).into(holder.Image);

    }
    public interface OnItemClickListener{
        void onItemClick(DataSnapshot dataSnapshot, int position);
    }
    public void setOnItemCLickListener(CategoryTopAdapter.OnItemClickListener listener){
        CategoryTopAdapter.listener =listener;

    }
    @NonNull
    @Override
    public CategoryTopAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_top_mainactivity2_cardview,parent,false);
        return new myViewHolder(view);
    }

    public  class myViewHolder extends RecyclerView.ViewHolder {
        TextView Name;
        ImageView Image;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            Name=itemView.findViewById(R.id.Category_card_Name_MainActivity2);
            Image=itemView.findViewById(R.id.Category_cardImage_MainActivity2);
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
