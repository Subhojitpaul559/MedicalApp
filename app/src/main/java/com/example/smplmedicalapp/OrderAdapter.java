package com.example.smplmedicalapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.orderViewHolder> {
    private List<OrderModel> list;

    public OrderAdapter(List<OrderModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public orderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Context context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent, false);
        return new OrderAdapter.orderViewHolder(v);
    }

    @Override
    public  void onBindViewHolder(@NonNull orderViewHolder holder, int position) {

        OrderModel currentItem = list.get(position);
        //final int index = holder.getAdapterPosition();

        holder.quantity.setText(list.get(position).getQuantity());
        holder.address.setText(list.get(position).getAddress());
        holder.medicineName.setText(list.get(position).getMedicineName());
        holder.name.setText(list.get(position).getName());
        holder.amount.setText(list.get(position).getAmount());
        holder.tax.setText(list.get(position).getTax());
        holder.orderid.setText(list.get(position).getOrderID());
        float amount = Float.parseFloat(list.get(position).getAmount());
        float tax = Float.parseFloat(list.get(position).getTax());
        float total = amount + tax;
        holder.total.setText(String.valueOf(total));
        holder.phone.setText(list.get(position).getPhone());
        //holder.quantity.setText(list.get(position).getQuantity());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class orderViewHolder extends RecyclerView.ViewHolder{

            TextView medicineName, address, name,phone, amount, tax,quantity, total, orderid;
            RelativeLayout relativeLayout;

            public orderViewHolder(@NonNull View itemView) {
                super(itemView);

                medicineName = itemView.findViewById(R.id.item_name);
                address = itemView.findViewById(R.id.address);
                name = itemView.findViewById(R.id.user_name);
                phone = itemView.findViewById(R.id.phone);
                amount = itemView.findViewById(R.id.item_original_price);
                tax = itemView.findViewById(R.id.item_discount);
                quantity = itemView.findViewById(R.id.item_qty);
                total = itemView.findViewById(R.id.item_price);
                orderid = itemView.findViewById(R.id.odrid);
                relativeLayout = itemView.findViewById(R.id.order_relativeLayout);

            }
        }
}
