package com.example.foodyhomeSS;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;

public class AllComboAdapter extends FirebaseRecyclerAdapter<AllComboModel,AllComboAdapter.MyViewHolder> {

    private static AllComboAdapter.OnItemClickListener listener;
    public AllComboAdapter(@NonNull FirebaseRecyclerOptions<AllComboModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull AllComboModel model) {
        Glide.with(holder.Image).load(model.getImage()).into(holder.Image);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.combo_cardview,parent, false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView Image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Image=itemView.findViewById(R.id.ImageView_ComboCardView);
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

    public interface OnItemClickListener{
        void onItemClick(DataSnapshot dataSnapshot, int position);
    }
    public void setOnItemCLickListener(AllComboAdapter.OnItemClickListener listener){
        AllComboAdapter.listener =listener;

    }
}
