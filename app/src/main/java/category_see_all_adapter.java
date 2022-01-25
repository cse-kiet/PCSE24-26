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
import com.shorsam.foodyhomeSS.CategoryTopAdapter;
import com.shorsam.foodyhomeSS.R;

public class category_see_all_adapter extends FirebaseRecyclerAdapter<Category_See_ALL_Model,category_see_all_adapter.MyViewHolder>{


    public category_see_all_adapter(@NonNull FirebaseRecyclerOptions<Category_See_ALL_Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull category_see_all_adapter.MyViewHolder holder, int position, @NonNull Category_See_ALL_Model model) {
        holder.CategoryName.setText(model.getCategoryName());
        holder.Description.setText(model.getDescription());
        holder.Discount.setText(model.getDiscount());
        holder.Price.setText(model.getPrice());

        Glide.with(holder.Image.getContext()).load(model.getImage()).into(holder.Image);
    }

    @NonNull
    @Override
    public category_see_all_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_see_all_card_design,parent,false);
        return new category_see_all_adapter.MyViewHolder(view);
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView CategoryName;
        TextView Discount;
        TextView Price;
        TextView Description;
        ImageView Image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            CategoryName=itemView.findViewById(R.id.Category_Name_See_All);
            Discount=itemView.findViewById(R.id.Discount_See_All);
            Price=itemView.findViewById(R.id.Price_See_ALL);
            Description=itemView.findViewById(R.id.Description_See_All);
            Image=itemView.findViewById(R.id.Image_View_See_All);
        }
    }
}
