package com.example.foodyhomeSS;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AllAddressAdapter extends FirestoreRecyclerAdapter<AllAddressModel,AllAddressAdapter.MyViewHolder> {

    public AllAddressAdapter(@NonNull FirestoreRecyclerOptions<AllAddressModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int i, @NonNull AllAddressModel model) {
        holder.Name.setText(model.getName());
        holder.Address.setText(model.getName());
        holder.Phone.setText(model.getName());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.address_card_layout,parent,false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Name, Address,Phone;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Name=itemView.findViewById(R.id.Name_Address_Section_Address_Card);
            Address=itemView.findViewById(R.id.Address_Address_Section_Address_Card);
            Phone=itemView.findViewById(R.id.Phone_Address_Section_Address_Card);
        }
    }
}
