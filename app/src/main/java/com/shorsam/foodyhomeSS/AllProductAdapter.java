package com.shorsam.foodyhomeSS;

import android.annotation.SuppressLint;
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

import java.util.Objects;

public class AllProductAdapter extends FirebaseRecyclerAdapter<AllProductModel,AllProductAdapter.ViewHolder> {

    String SRating;
    Float FRating;
    ImageView Star1,Star2,Star3,Star4,Star5;
    private static AllProductAdapter.OnItemClickListener listener;
    public AllProductAdapter(@NonNull FirebaseRecyclerOptions<AllProductModel> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull AllProductModel model) {
        String name=model.getName();
        if (name.length()>40){
            holder.Name.setText(name.substring(0,40)+"...");
        }
        else{
            holder.Name.setText(model.getName());
        }

        holder.Price.setText(model.getPrice());
        holder.Discount.setText(model.getDiscount());
        holder.MRP.setText(model.getMRP());
        holder.DeliveryStatus.setText(model.getDeliveryStatus());
        if (model.getImage()!=null){
            Glide.with(holder.Image.getContext()).load(model.getImage()).into(holder.Image);
        }
        holder.Store.setText(model.getStoreName());

//        holder.Rating.setText(model.getRating());
        SRating= model.getRating();
        SRating=SRating.replace("+","");
        FRating=Float.parseFloat(SRating);
        SelectRating();

    }
    private void SelectRating() {
        if (FRating<=0.5){
            Star1.setImageResource(R.drawable.ic_baseline_star_half_24);
            Star1.setVisibility(View.VISIBLE);
            Star2.setVisibility(View.INVISIBLE);
            Star3.setVisibility(View.INVISIBLE);
            Star4.setVisibility(View.INVISIBLE);
            Star5.setVisibility(View.INVISIBLE);
        }
        if (FRating<=1.0 && FRating>0.5){
            Star1.setVisibility(View.VISIBLE);
            Star2.setVisibility(View.INVISIBLE);
            Star3.setVisibility(View.INVISIBLE);
            Star4.setVisibility(View.INVISIBLE);
            Star5.setVisibility(View.INVISIBLE);
            Star1.setImageResource(R.drawable.ic_baseline_star_24);
        }
        if (FRating<=1.5 && FRating>1.0){

            Star1.setVisibility(View.VISIBLE);
            Star2.setVisibility(View.VISIBLE);
            Star2.setImageResource(R.drawable.ic_baseline_star_half_24);

            Star3.setVisibility(View.INVISIBLE);
            Star4.setVisibility(View.INVISIBLE);
            Star5.setVisibility(View.INVISIBLE);
            Star1.setImageResource(R.drawable.ic_baseline_star_24);

        }
        if (FRating<=2.0 && FRating>1.5){
            Star2.setVisibility(View.VISIBLE);
            Star1.setVisibility(View.VISIBLE);
            Star3.setVisibility(View.INVISIBLE);
            Star4.setVisibility(View.INVISIBLE);
            Star5.setVisibility(View.INVISIBLE);
            Star1.setImageResource(R.drawable.ic_baseline_star_24);
            Star2.setImageResource(R.drawable.ic_baseline_star_24);
        }
        if (FRating<=2.5 && FRating>2.0){

            Star1.setVisibility(View.VISIBLE);
            Star2.setVisibility(View.VISIBLE);
            Star3.setVisibility(View.VISIBLE);
            Star3.setImageResource(R.drawable.ic_baseline_star_half_24);

            Star4.setVisibility(View.INVISIBLE);
            Star5.setVisibility(View.INVISIBLE);
            Star1.setImageResource(R.drawable.ic_baseline_star_24);
            Star2.setImageResource(R.drawable.ic_baseline_star_24);

        }
        if (FRating<=3.0 && FRating>2.5){

            Star1.setVisibility(View.VISIBLE);
            Star2.setVisibility(View.VISIBLE);
            Star3.setVisibility(View.VISIBLE);
            Star4.setVisibility(View.INVISIBLE);
            Star5.setVisibility(View.INVISIBLE);
            Star1.setImageResource(R.drawable.ic_baseline_star_24);
            Star2.setImageResource(R.drawable.ic_baseline_star_24);
            Star3.setImageResource(R.drawable.ic_baseline_star_24);

        }
        if (FRating<=3.5 && FRating>3.0){

            Star1.setVisibility(View.VISIBLE);
            Star2.setVisibility(View.VISIBLE);
            Star3.setVisibility(View.VISIBLE);
            Star4.setVisibility(View.VISIBLE);
            Star4.setImageResource(R.drawable.ic_baseline_star_half_24);

            Star5.setVisibility(View.INVISIBLE);
            Star1.setImageResource(R.drawable.ic_baseline_star_24);
            Star2.setImageResource(R.drawable.ic_baseline_star_24);
            Star3.setImageResource(R.drawable.ic_baseline_star_24);

        }
        if (FRating<=4.0 && FRating>3.5){

            Star1.setVisibility(View.VISIBLE);
            Star2.setVisibility(View.VISIBLE);
            Star3.setVisibility(View.VISIBLE);
            Star4.setVisibility(View.VISIBLE);

            Star5.setVisibility(View.INVISIBLE);
            Star1.setImageResource(R.drawable.ic_baseline_star_24);
            Star2.setImageResource(R.drawable.ic_baseline_star_24);
            Star3.setImageResource(R.drawable.ic_baseline_star_24);
            Star4.setImageResource(R.drawable.ic_baseline_star_24);
        }
        if (FRating<=4.5 && FRating>4.0){

            Star1.setVisibility(View.VISIBLE);
            Star2.setVisibility(View.VISIBLE);
            Star3.setVisibility(View.VISIBLE);
            Star4.setVisibility(View.VISIBLE);
            Star5.setVisibility(View.VISIBLE);
            Star5.setImageResource(R.drawable.ic_baseline_star_half_24);
            Star1.setImageResource(R.drawable.ic_baseline_star_24);
            Star2.setImageResource(R.drawable.ic_baseline_star_24);
            Star3.setImageResource(R.drawable.ic_baseline_star_24);
            Star4.setImageResource(R.drawable.ic_baseline_star_24);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_product_card_design2,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name,Price,MRP,Rating,Discount,Store,DeliveryStatus;
        ImageView Image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Image=itemView.findViewById(R.id.AllProduct2_Product_Image_ImageView);
            Name=itemView.findViewById(R.id.AllProduct2_Product_Name_TextView);
            Price=itemView.findViewById(R.id.AllProduct2_Product_Price_TextView);
            MRP=itemView.findViewById(R.id.AllProduct2_Product_MRP_TextView);
            DeliveryStatus=itemView.findViewById(R.id.AllProduct2_Product_DeliveryStatus_TextView);
            Store=itemView.findViewById(R.id.AllProduct2_Product_StoreName_TextView);
            Discount=itemView.findViewById(R.id.AllProduct2_Product_Discount_TextView);
            Star1=itemView.findViewById(R.id.AllProduct2_Product_Star1_ImageView);
            Star2=itemView.findViewById(R.id.AllProduct2_Product_Star2_ImageView);
            Star3=itemView.findViewById(R.id.AllProduct2_Product_Star3_ImageView);
            Star4=itemView.findViewById(R.id.AllProduct2_Product_Star4_ImageView);
            Star5=itemView.findViewById(R.id.AllProduct2_Product_Star5_ImageView);
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
    public void setOnItemCLickListener(AllProductAdapter.OnItemClickListener listener){
        AllProductAdapter.listener =listener;

    }
}
