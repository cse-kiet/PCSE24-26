package com.samshor.foodyhomeSS;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllAddressAdapter extends FirestoreRecyclerAdapter<AllAddressModel,AllAddressAdapter.MyViewHolder> {
    public AllAddressAdapter.OnItemClickListener listener;
    List<View> itemViewList = new ArrayList<>();

    public AllAddressAdapter(@NonNull FirestoreRecyclerOptions<AllAddressModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull AllAddressModel model) {
        holder.Name.setText(model.getName());
        holder.Address.setText(model.getAddress());
        holder.Phone.setText(model.getPhone());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.address_card_layout,parent,false);
        itemViewList.add(view);
        return new MyViewHolder(view);
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Name, Address,Phone;
        ConstraintLayout layout;

        ImageButton Remove;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Name=itemView.findViewById(R.id.Name_Address_Section_Address_Card);
            Address=itemView.findViewById(R.id.Address_Default_Address_Profile);
            Phone=itemView.findViewById(R.id.Phone_Default_Address_Profile);
            layout=itemView.findViewById(R.id.ConstraintLayout_Address_Card_design);
            Remove=itemView.findViewById(R.id.AllAddress_RemoveImageButton);
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                        for(View tempItemView : itemViewList) {
                            /** navigate through all the itemViews and change color
                             of selected view to colorSelected and rest of the views to colorDefault **/
                            if(itemViewList.get(position) == tempItemView) {
                                tempItemView.setBackgroundResource(R.color.purple_700);
                            }
                            else{
                                tempItemView.setBackgroundResource(R.color.white);
                            }
                        }

                    }
                }
            });

        }
    }
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
        void onRemoveClick(DocumentSnapshot documentSnapshot,int position);
    }

    public void setOnItemClickListener(AllAddressAdapter.OnItemClickListener listener) {
        this.listener =listener;
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

}
