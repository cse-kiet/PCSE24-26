package com.example.foodyhomeSS;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;

import java.util.Objects;

public class IndividualCategoryAdapter extends FirebaseRecyclerAdapter<IndividualCategoryModel,IndividualCategoryAdapter.ViewHolder> {

    private IndividualCategoryAdapter.OnItemClickListener listener;
    public IndividualCategoryAdapter(@NonNull FirebaseRecyclerOptions<IndividualCategoryModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull IndividualCategoryModel model) {
        Glide.with(holder.Image).load(model.getImage()).into(holder.Image);
        holder.Name.setText(model.getName());
        holder.Price.setText(model.getPrice());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_product_card_design,parent,false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        int count=1;
        TextView Name,Price;
        ImageView Image;
        CardView tick, MainCV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Image=itemView.findViewById(R.id.Individual_Product_ImageView);
            tick=itemView.findViewById(R.id.Tick_Individual_Product_card_design_Add_On);
            Name=itemView.findViewById(R.id.Individual_Product_Name);
            Price=itemView.findViewById(R.id.Individual_Product_Price);
            MainCV=itemView.findViewById(R.id.Main_CardView_Individual_Product_Card_design);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position!=RecyclerView.NO_POSITION && listener!=null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);

                        if (count%2!=0){
                            tick.setVisibility(View.VISIBLE);
                            MainCV.setAlpha((float) 0.6);
                            count++;
                        }
                        else{
                            tick.setVisibility(View.GONE);
                            MainCV.setAlpha(1);
                           count++;
                        }
                    }
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClick(DataSnapshot dataSnapshot, int position);
    }
    public void setOnItemCLickListener(IndividualCategoryAdapter.OnItemClickListener listener){
        this.listener=listener;

    }
}
