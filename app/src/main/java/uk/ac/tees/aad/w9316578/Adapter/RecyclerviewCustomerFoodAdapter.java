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

import uk.ac.tees.aad.w9316578.Activity.AdminViewFoodActivity;
import uk.ac.tees.aad.w9316578.Activity.CustomerViewFoodActivity;
import uk.ac.tees.aad.w9316578.Model.Food;
import uk.ac.tees.aad.w9316578.R;

public class RecyclerviewCustomerFoodAdapter extends RecyclerView.Adapter<RecyclerviewCustomerFoodAdapter.MyViewHolder> {

    List<Food> list;
    Context context;

    public RecyclerviewCustomerFoodAdapter(List<Food> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerviewCustomerFoodAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_item_customer, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerviewCustomerFoodAdapter.MyViewHolder holder, int position) {

        holder.menuName.setText(list.get(position).getFoodName());
        holder.menuPrice.setText("Price : £" + list.get(position).getFoodPrice());
        //  holder.addedDate.setText(list.get(position).getDate());
        Picasso.get().load(list.get(position).getFoodImageUri()).into(holder.foodImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CustomerViewFoodActivity.class);
                intent.putExtra("food", list.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView addedDate, menuName, menuPrice;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            foodImage = itemView.findViewById(R.id.imageView);
            // addedDate=itemView.findViewById(R.id.addedDate);
            menuName = itemView.findViewById(R.id.menuName);
            menuPrice = itemView.findViewById(R.id.menuPrice);
        }
    }
}
