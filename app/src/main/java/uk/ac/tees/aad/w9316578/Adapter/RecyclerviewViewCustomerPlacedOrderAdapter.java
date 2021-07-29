package uk.ac.tees.aad.w9316578.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.aad.w9316578.Model.CartFood;
import uk.ac.tees.aad.w9316578.Model.OrderInfo;
import uk.ac.tees.aad.w9316578.R;

public class RecyclerviewViewCustomerPlacedOrderAdapter extends RecyclerView.Adapter<RecyclerviewViewCustomerPlacedOrderAdapter.MyViewHolder> {

    List<OrderInfo> list;
    Context context;

    public RecyclerviewViewCustomerPlacedOrderAdapter(List<OrderInfo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerviewViewCustomerPlacedOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleview_order_info, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerviewViewCustomerPlacedOrderAdapter.MyViewHolder holder, int position) {

       holder.orderStatus.setText(list.get(position).getStatus());
       holder.orderNumber.setText(position+1+"");
       holder.orderID.setText(list.get(position).getOrderID());
       holder.orderDate.setText(list.get(position).getDateOrder());
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

           }
       });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView orderNumber, orderDate, orderStatus, orderID;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            orderNumber = itemView.findViewById(R.id.orderNumber);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            orderID = itemView.findViewById(R.id.orderID);

        }
    }
}
