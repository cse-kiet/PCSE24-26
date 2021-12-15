package com.foodyhome.foodyhomeSS;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class PaymentItemAdapter extends FirestoreRecyclerAdapter<PaymentItemModel,PaymentItemAdapter.MyViewHolder> {
    public PaymentItemAdapter(@NonNull FirestoreRecyclerOptions<PaymentItemModel> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull PaymentItemModel paymentItemModel) {
        holder.Name.setText(paymentItemModel.getName());
        holder.QTY.setText("X "+paymentItemModel.getQTY());
        if (paymentItemModel.getAddOn0()==null){
            holder.AddOn0.setText("");
        }
        if (paymentItemModel.getAddOn1()==null){
            holder.AddOn1.setText("");
        }
        if (paymentItemModel.getAddOn2()==null){
            holder.AddOn2.setText("");
        }
        if (paymentItemModel.getAddOn3()==null){
            holder.AddOn3.setText("");
        }
        if (paymentItemModel.getAddOn4()==null){
            holder.AddOn4.setText("");
        }
        if (paymentItemModel.getAddOn0()!=null){
            holder.AddOn0.setText("+ " +paymentItemModel.getAddOn0());
        }
        if (paymentItemModel.getAddOn1()!=null){
            holder.AddOn1.setText("+ " +paymentItemModel.getAddOn1());
        }
        if (paymentItemModel.getAddOn2()!=null){
            holder.AddOn2.setText("+ " +paymentItemModel.getAddOn2());
        }
        if (paymentItemModel.getAddOn3()!=null){
            holder.AddOn3.setText("+ " +paymentItemModel.getAddOn3());
        }
        if (paymentItemModel.getAddOn4()!=null){
            holder.AddOn4.setText("+ " +paymentItemModel.getAddOn4());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_cart_item_cart_design,parent,false);
        return new MyViewHolder(view);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Name,QTY,AddOn0,AddOn1,AddOn2,AddOn3,AddOn4;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Name=itemView.findViewById(R.id.Payment_cart_item_Product_Name);
            QTY=itemView.findViewById(R.id.Payment_cart_item_Product_QTY);
            AddOn0=itemView.findViewById(R.id.AddOns_0_Payment_cart_item);
            AddOn1=itemView.findViewById(R.id.AddOns_1_Payment_cart_item);
            AddOn2=itemView.findViewById(R.id.AddOns_2_Payment_cart_item);
            AddOn3=itemView.findViewById(R.id.AddOns_3_Payment_cart_item);
            AddOn4=itemView.findViewById(R.id.AddOns_4_Payment_cart_item);
        }
    }
}
