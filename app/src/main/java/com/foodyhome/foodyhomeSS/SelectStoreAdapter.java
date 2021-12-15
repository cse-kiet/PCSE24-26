package com.foodyhome.foodyhomeSS;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SelectStoreAdapter extends FirebaseRecyclerAdapter<SelectStoreModel,SelectStoreAdapter.MyViewHolder> {

    private SelectStoreAdapter.OnItemClickListener listener;
    List<View> itemViewList = new ArrayList<>();
    public SelectStoreAdapter(@NonNull FirebaseRecyclerOptions<SelectStoreModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull SelectStoreModel model) {
        holder.Name.setText(model.getName());
        holder.Address.setText(model.getAddress());
        holder.Rating.setText(model.getRating());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.store_card_design,parent,false);
        itemViewList.add(view);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Name,Address,Rating;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Name=itemView.findViewById(R.id.Name_Select_StoreTV);
            Address=itemView.findViewById(R.id.Address_Select_Store_TV);
            Rating=itemView.findViewById(R.id.Store_Rating_SelectStore_TV);
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
    public interface OnItemClickListener{
        void onItemClick(DataSnapshot dataSnapshot, int position);
    }
    public void setOnItemCLickListener(SelectStoreAdapter.OnItemClickListener listener){
        this.listener =listener;

    }
}
