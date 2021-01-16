package com.example.smplmedicalapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;

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
        holder.changeOdr.setText(list.get(position).getUstatus());
        holder.total.setText(String.valueOf(total));
        holder.phone.setText(list.get(position).getPhone());
        holder.changeOdr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uorderID = currentItem.getOrderID();


                String umedID = currentItem.getUmedID();
                String orderStoreID = currentItem.getStoreId();
                String UID = currentItem.getUID();



                DatabaseReference uidref = FirebaseDatabase
                        .getInstance()
                        .getReferenceFromUrl("https://smplmedicalapp-408ea-default-rtdb.firebaseio.com/userorders")
                        .child(UID)
                        .child(uorderID);



                uidref.child(umedID).child("ustatus").setValue("ACCEPTED");

/*

String url = "https://smplmedicalapp-408ea-default-rtdb.firebaseio.com/";
                DatabaseReference vendorOderRef = FirebaseDatabase.getInstance()
                        .getReferenceFromUrl(url)
                        .child("orders");
                vendorOderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            DatabaseReference voderRef1 = FirebaseDatabase.getInstance()
                                    .getReferenceFromUrl(url).child("orders").child(dataSnapshot.getKey());

                            voderRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot snapshot2: snapshot.getChildren()){

                                        Log.i("testing which ID", snapshot2.getKey());

                                        DatabaseReference voderRef2 = voderRef1.child(snapshot2.getKey());
                                        DatabaseReference voderRefcust = voderRef1.child(snapshot2.child("customer").getKey());

                                        VendorOrderModel vendorOrderModel = snapshot2.getValue(VendorOrderModel.class);

                                        VendorOrderModel vendorOrderModelcust = snapshot2.child("customer").getValue(VendorOrderModel.class);
                                        Log.i("testing which key: ", snapshot2.getValue().toString());
                                        Log.i("testing which key 2: ", vendorOrderModel.getStoreId());


                                        if(vendorOrderModel.getStoreId().equals(orderStoreID) &&
                                                vendorOrderModel.getMedicineId().equals(umedID)  &&
                                        vendorOrderModelcust.getUid().equals(UID)){
                                            vendorOrderModel.setStatus("ACCEPTED");
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

*/


                holder.changeOdr.setText("Accepted");

            }
        });
        //holder.quantity.setText(list.get(position).getQuantity());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class orderViewHolder extends RecyclerView.ViewHolder{

            TextView medicineName, address, name,phone, amount, tax,quantity, total, orderid;
            RelativeLayout relativeLayout;
            Button changeOdr;

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
                changeOdr = itemView.findViewById(R.id.changeodr);
                relativeLayout = itemView.findViewById(R.id.order_relativeLayout);

            }
        }
}
