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
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class YourMenuAdapter extends RecyclerView.Adapter<YourMenuAdapter.MyViewHolder>{
    ArrayList<YourMenuModel> DataList;
    Context context;
    private YourMenuAdapter.OnItemClickListener Listener;
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onIncreaseClick(int position);
        void onDecreaseClick(int position);
    }

    public YourMenuAdapter(ArrayList<YourMenuModel> dataList, Context context) {
        this.DataList = dataList;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.your_menu_card_design,parent,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.PName.setText(DataList.get(position).getName());
        holder.PPrice.setText(DataList.get(position).getPrice());
        holder.PMRP.setText(DataList.get(position).getMRP());
        if (DataList.get(position).getAddOn0()!=null) {

            holder.A0.setText("+ " +DataList.get(position).getAddOn0());
        }
        if (DataList.get(position).getAddOn1()!=null) {
            holder.A1.setText("+ " +DataList.get(position).getAddOn1());
        }
        if (DataList.get(position).getAddOn2()!=null) {
            holder.A2.setText("+ " +DataList.get(position).getAddOn2());
        }
        if (DataList.get(position).getAddOn3()!=null) {
            holder.A3.setText("+ " +DataList.get(position).getAddOn3());
        }
        if (DataList.get(position).getAddOn4()!=null) {
            holder.A4.setText("+ " +DataList.get(position).getAddOn4());
        }
        Glide.with(holder.PImage).load(DataList.get(position).getImage()).into(holder.PImage);

    }

    @Override
    public int getItemCount() {
        return DataList.size();
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
            Increase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Listener.onIncreaseClick(position);

                            String Sqty= QTY.getText().toString();
                            Integer Iqty=Integer.parseInt(Sqty);
                            Iqty+=1;
                            Sqty=Iqty.toString();
                            QTY.setText(Sqty);
                        }
                    }


                }
            });
            Decrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Listener.onDecreaseClick(position);
                            String Sqty= QTY.getText().toString();
                            Integer Iqty=Integer.parseInt(Sqty);
                            if (Iqty>1){
                                Iqty-=1;
                            }
                            Sqty=Iqty.toString();
                            QTY.setText(Sqty);
                        }
                    }

                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Listener.onItemClick(position);
                        }
                    }
                }
            });


        }
    }
    public void setOnItemClickListener(YourMenuAdapter.OnItemClickListener listener) {
        Listener = listener;
    }

}
