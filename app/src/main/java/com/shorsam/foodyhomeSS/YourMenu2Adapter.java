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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class YourMenu2Adapter extends FirestoreRecyclerAdapter<YourOrder2Model,YourMenu2Adapter.MyViewHolder> {

    public YourMenu2Adapter(@NonNull FirestoreRecyclerOptions<YourOrder2Model> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int i, @NonNull YourOrder2Model model) {
        holder.PName.setText(model.getName());
        holder.PPrice.setText(model.getPrice());
        holder.PMRP.setText(model.getMRP());
        if (model.getAddOn0()==null){
            holder.A0.setText("");
        }
        if (model.getAddOn1()==null){
            holder.A1.setText("");
        }
        if (model.getAddOn2()==null){
            holder.A2.setText("");
        }
        if (model.getAddOn3()==null){
            holder.A3.setText("");
        }
        if (model.getAddOn4()==null){
            holder.A4.setText("");
        }
        if (model.getAddOn0()!=null){
            holder.A0.setText("+ " +model.getAddOn0());
        }
        if (model.getAddOn1()!=null){
            holder.A1.setText("+ " +model.getAddOn1());
        }
        if (model.getAddOn2()!=null){
            holder.A2.setText("+ " +model.getAddOn2());
        }
        if (model.getAddOn3()!=null){
            holder.A3.setText("+ " +model.getAddOn3());
        }
        if (model.getAddOn4()!=null){
            holder.A4.setText("+ " +model.getAddOn4());
        }
        holder.QTY.setText(model.getQTY());
        Glide.with(holder.PImage).load(model.getImage()).into(holder.PImage);
        holder.PSize.setText(model.getSize());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.your_order_2_card_design,parent,false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView PName,PSize,PMRP,PPrice,A0,A1,A2,A3,A4,QTY;
        ImageView PImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            PName=itemView.findViewById(R.id.YourOrder_2_Product_Name);
            PPrice=itemView.findViewById(R.id.YourOrder_2_Product_Price);
            PMRP=itemView.findViewById(R.id.YourOrder_2_Product_MRP);
            A0=itemView.findViewById(R.id.AddOns_0_YourOrder_2);
            A1=itemView.findViewById(R.id.AddOns_1_YourOrder_2);
            A2=itemView.findViewById(R.id.AddOns_2_YourOrder_2);
            A3=itemView.findViewById(R.id.AddOns_3_YourOrder_2);
            A4=itemView.findViewById(R.id.AddOns_4_YourOrder_2);
            PImage=itemView.findViewById(R.id.YourOrder_2_Image_View);
            PSize=itemView.findViewById(R.id.YourOrder_2_Product_Size);
            QTY=itemView.findViewById(R.id.YourOrder_2_Product_QTY);
        }
    }
}
