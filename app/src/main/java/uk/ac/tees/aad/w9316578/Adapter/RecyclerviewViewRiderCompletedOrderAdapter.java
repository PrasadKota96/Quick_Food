package uk.ac.tees.aad.w9316578.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import uk.ac.tees.aad.w9316578.Activity.RiderViewOrderDetialActivity;
import uk.ac.tees.aad.w9316578.Model.OrderInfoForRider;
import uk.ac.tees.aad.w9316578.R;

public class RecyclerviewViewRiderCompletedOrderAdapter extends RecyclerView.Adapter<RecyclerviewViewRiderCompletedOrderAdapter.MyViewHolder> {

    List<OrderInfoForRider> list;
    Context context;

    public RecyclerviewViewRiderCompletedOrderAdapter(List<OrderInfoForRider> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerviewViewRiderCompletedOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleview_order_info, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerviewViewRiderCompletedOrderAdapter.MyViewHolder holder, int position) {

       holder.orderStatus.setText(list.get(position).getStatus());
       holder.orderNumber.setText(position+1+"");
       holder.orderID.setText(list.get(position).getOrderID());
       holder.orderDate.setText(list.get(position).getDateOrder());

       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(context, RiderViewOrderDetialActivity.class);
               intent.putExtra("orderInfo",list.get(position));
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               context.startActivity(intent);
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
