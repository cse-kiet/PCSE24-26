package com.samshor.foodyhomeSS;

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

public class RMLAdapter extends FirebaseRecyclerAdapter<RegularModel,RMLAdapter.MyAdapter> {

    private RMLAdapter.OnItemClickListener listener;
    List<View> itemViewList = new ArrayList<>();
    public RMLAdapter(@NonNull FirebaseRecyclerOptions<RegularModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyAdapter holder, int position, @NonNull RegularModel model) {
        holder.Name.setText(model.getName());
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.regular_medium_card_design,parent,false);
        itemViewList.add(view);
        return new MyAdapter(view);
    }

    public class MyAdapter extends RecyclerView.ViewHolder {
        TextView Name;
        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            Name=itemView.findViewById(R.id.TextView_RML_RecyclerView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                        for(View tempItemView : itemViewList) {
                            if(itemViewList.get(position) == tempItemView) {
                                tempItemView.setBackgroundResource(R.color.Yellow);
                            }
                            else{
                                tempItemView.setBackgroundResource(R.color.matBlack);
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
    public void setOnItemCLickListener(RMLAdapter.OnItemClickListener listener){
        this.listener =listener;

    }
}
