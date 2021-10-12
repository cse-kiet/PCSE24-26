package com.example.foodyhomeSS;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class YourOder_1_Adapter extends FirestoreRecyclerAdapter<YourOrder_1_Model,YourOder_1_Adapter.MyViewHolder> {
    public YourOder_1_Adapter.OnItemClickListener listener;

    public YourOder_1_Adapter(@NonNull FirestoreRecyclerOptions<YourOrder_1_Model> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int i, @NonNull YourOrder_1_Model model) {
        holder.Address.setText(model.getAddress());
        holder.TotalPay.setText("Rs "+model.getTotalPay().toString()+"/-");
        String TDateS=getDate();
        Integer TDateI=Integer.parseInt(TDateS);
        int diff=Integer.parseInt(model.getDate())-TDateI;
        if (diff==0){
            holder.Date.setText("Today");
        }
        if (diff>0){
            holder.Date.setText(diff+" day ago");
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.your_order1_card_design,parent,false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Address,TotalPay,Date;
        Button Track , Help;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Address=itemView.findViewById(R.id.YourOrder1_Address_TV);
            TotalPay=itemView.findViewById(R.id.YourOrder1_TotalPay_TV);
            Date=itemView.findViewById(R.id.YourOrder1_Date_TV);
            Track=itemView.findViewById(R.id.YourOrder1_Track_Button);
            Help=itemView.findViewById(R.id.YourOrder1_Help_Button);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }
    private String getDate() {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        return dateFormat.format(date);
    }
    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
        void onTrackClick(DocumentSnapshot documentSnapshot,int position);
        void onHelpClick(DocumentSnapshot documentSnapshot,int position);
    }
    public void setOnItemClickListener(YourOder_1_Adapter.OnItemClickListener listener) {
        this.listener = listener;
    }
    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }
}
