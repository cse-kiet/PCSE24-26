package com.example.foodyhomeSS;

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

public class AllProductAdapter extends FirebaseRecyclerAdapter<AllProductModel,AllProductAdapter.ViewHolder> {

    public AllProductAdapter(@NonNull FirebaseRecyclerOptions<AllProductModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull AllProductModel model) {
        holder.Name.setText(model.getName());
        holder.Price.setText(model.getPrice());
        holder.Discount.setText(model.getDiscount());
        holder.MRP.setText(model.getMRP());
        holder.Rating.setText(model.getRating());
        Glide.with(holder.Image.getContext()).load(model.getImage()).into(holder.Image);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_grid_card_design,parent,false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name,Price,MRP,Rating,Discount;
        ImageView Image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Image=itemView.findViewById(R.id.AllProductUniversal_Product_Image);
            Name=itemView.findViewById(R.id.AllProductUniversal_Product_Name);
            Price=itemView.findViewById(R.id.AllProductUniversal_Product_Price);
            MRP=itemView.findViewById(R.id.AllProductUniversal_Product_MRP);
            Rating=itemView.findViewById(R.id.AllProductUniversal_Product_Star_Rating);
            Discount=itemView.findViewById(R.id.AllProductUniversal_Product_Discount);
        }
    }
}
