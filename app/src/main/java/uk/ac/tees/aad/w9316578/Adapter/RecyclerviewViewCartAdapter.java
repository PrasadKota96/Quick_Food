package uk.ac.tees.aad.w9316578.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.aad.w9316578.Activity.CustomerViewFoodActivity;
import uk.ac.tees.aad.w9316578.Model.CartFood;
import uk.ac.tees.aad.w9316578.Model.Food;
import uk.ac.tees.aad.w9316578.R;

public class RecyclerviewViewCartAdapter extends RecyclerView.Adapter<RecyclerviewViewCartAdapter.MyViewHolder> {

    List<CartFood> list;
    Context context;

    public RecyclerviewViewCartAdapter(List<CartFood> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerviewViewCartAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleview_cart, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerviewViewCartAdapter.MyViewHolder holder, int position) {

        holder.foodName.setText(list.get(position).getFoodName());
        holder.foodDesc.setText(list.get(position).getFoodDesc());
        holder.addedDate.setText(list.get(position).getDate());
      Picasso.get().load(list.get(position).getFoodImageUri()).into(holder.foodImage);
        holder.foodPrice.setText("Price : £" + list.get(position).getFoodPrice());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView foodImage;
        TextView addedDate, foodName, foodDesc, foodPrice;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            foodName = itemView.findViewById(R.id.foodName);
            foodImage = itemView.findViewById(R.id.foodImage);
            addedDate = itemView.findViewById(R.id.date);
            foodDesc = itemView.findViewById(R.id.foodDesc);
            foodPrice = itemView.findViewById(R.id.foodPrice);
        }
    }
}
