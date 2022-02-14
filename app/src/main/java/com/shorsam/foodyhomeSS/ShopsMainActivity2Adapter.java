package com.shorsam.foodyhomeSS;

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


public class  ShopsMainActivity2Adapter extends FirebaseRecyclerAdapter<CategoryModelTop, ShopsMainActivity2Adapter.holder> {
        private static ShopsMainActivity2Adapter.OnItemClickListener listener;
        public ShopsMainActivity2Adapter(@NonNull FirebaseRecyclerOptions<CategoryModelTop> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull holder holder, int position, @NonNull CategoryModelTop model) {
            holder.Offer.setText(model.getName());
            holder.Name.setText(model.getName());
            Glide.with(holder.Image.getContext()).load(model.getImage()).into(holder.Image);

        }


        @NonNull
        @Override
        public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.shops_mainactivity2_cardview,parent,false);
            return new ShopsMainActivity2Adapter.holder(view);

        }
    public interface OnItemClickListener{
        void onItemClick(DataSnapshot dataSnapshot, int position);
    }
    public void setOnItemCLickListener(ShopsMainActivity2Adapter.OnItemClickListener listener){
        ShopsMainActivity2Adapter.listener =listener;

    }

        public    class holder extends RecyclerView.ViewHolder {
            ImageView Image;
            TextView Offer;
            TextView Name;
            public holder(@NonNull View itemView) {
                super(itemView);
                Offer=itemView.findViewById(R.id.offers_Shops);
                Name=itemView.findViewById(R.id.Name_Shops);
                Image=itemView.findViewById(R.id.image_Shops);
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
    }


