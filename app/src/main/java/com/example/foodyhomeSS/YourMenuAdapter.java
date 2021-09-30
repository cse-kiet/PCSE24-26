package com.example.foodyhomeSS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class YourMenuAdapter extends FirestoreRecyclerAdapter<YourMenuModel, YourMenuAdapter.MyViewHolder> {
    public YourMenuAdapter.OnItemClickListener listener;

    public YourMenuAdapter(@NonNull FirestoreRecyclerOptions<YourMenuModel> options) {
        super(options);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.your_menu_card_design,parent,false);
        return new MyViewHolder(view);
    }



    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull YourMenuModel yourMenuModel) {
        holder.PName.setText(yourMenuModel.getName());
        holder.PPrice.setText(yourMenuModel.getPrice());
        holder.PMRP.setText(yourMenuModel.getMRP());
        if (yourMenuModel.getAddOn0()==null){
            holder.A0.setText("");
        }
        if (yourMenuModel.getAddOn1()==null){
            holder.A1.setText("");
        }
        if (yourMenuModel.getAddOn2()==null){
            holder.A2.setText("");
        }
        if (yourMenuModel.getAddOn3()==null){
            holder.A3.setText("");
        }
        if (yourMenuModel.getAddOn4()==null){
            holder.A4.setText("");
        }
        if (yourMenuModel.getAddOn0()!=null){
            holder.A0.setText("+ " +yourMenuModel.getAddOn0());
        }
        if (yourMenuModel.getAddOn1()!=null){
            holder.A1.setText("+ " +yourMenuModel.getAddOn1());
        }
        if (yourMenuModel.getAddOn2()!=null){
            holder.A2.setText("+ " +yourMenuModel.getAddOn2());
        }
        if (yourMenuModel.getAddOn3()!=null){
            holder.A3.setText("+ " +yourMenuModel.getAddOn3());
        }
        if (yourMenuModel.getAddOn4()!=null){
            holder.A4.setText("+ " +yourMenuModel.getAddOn4());
        }
        holder.QTY.setText(yourMenuModel.getQTY());
        Glide.with(holder.PImage).load(yourMenuModel.getImage()).into(holder.PImage);

    }



    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView PName,PPrice,PMRP,A0,A1,A2,A3,A4,QTY;
        ImageView PImage;
        Button Remove,BuyNow;
        ImageButton Increase,Decrease;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            PName=itemView.findViewById(R.id.YourMenu_Product_Name);
            PPrice=itemView.findViewById(R.id.YourMenu_Product_Price);
            PMRP=itemView.findViewById(R.id.YourMenu_Product_MRP);
            A0=itemView.findViewById(R.id.AddOns_0_YourMenu);
            A1=itemView.findViewById(R.id.AddOns_1_YourMenu);
            A2=itemView.findViewById(R.id.AddOns_2_YourMenu);
            A3=itemView.findViewById(R.id.AddOns_3_YourMenu);
            A4=itemView.findViewById(R.id.AddOns_4_YourMenu);
            PImage=itemView.findViewById(R.id.YourMenu_Image_View);
            Remove=itemView.findViewById(R.id.YourMenu_Remove_Button);
            BuyNow=itemView.findViewById(R.id.YourMenu_BuyNow_Button);
            Increase=itemView.findViewById(R.id.YourMenu_QTY_increase_ImageButton);
            Decrease=itemView.findViewById(R.id.YourMenu_QTY_decrease_ImageButton);
            QTY=itemView.findViewById(R.id.YourMenu_QTY_TextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
            Increase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onIncreaseClick(getSnapshots().getSnapshot(position), position);

                        String S=QTY.getText().toString();
                        if (S!=null) {
                            Integer I = Integer.parseInt(S);
                            I += 1;
                            S = I.toString();
                            QTY.setText(S);
                            Map<String, Object> user = new HashMap<>();
                            user.put("QTY",S);
                            getSnapshots().getSnapshot(position).getReference().update(user);
                        }
                    }
                }
            });
            Decrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onDecreaseClick(getSnapshots().getSnapshot(position), position);
                        String S=QTY.getText().toString();
                        if (S!=null) {
                            Integer I = Integer.parseInt(S);
                            if (I>1){
                                I -= 1;
                                S = I.toString();
                                QTY.setText(S);
                                Map<String, Object> user = new HashMap<>();
                                user.put("QTY",S);
                                getSnapshots().getSnapshot(position).getReference().update(user);
                            }

                        }
                    }
                }
            });
            Remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onRemoveClick(getSnapshots().getSnapshot(position), position);
                        deleteItem(position);
                    }
                }
            });
            BuyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onBuyNowClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

        }
    }
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
        void onIncreaseClick(DocumentSnapshot documentSnapshot,int position);
        void onDecreaseClick(DocumentSnapshot documentSnapshot,int position);
        void onBuyNowClick(DocumentSnapshot documentSnapshot,int position);
        void onRemoveClick(DocumentSnapshot documentSnapshot,int position);
    }
    public void setOnItemClickListener(YourMenuAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

}
