package com.example.smplmedicalapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        holder.quantity.setText("Qty:"+list.get(position).getQuantity());
        holder.address.setText("address:"+list.get(position).getAddress());
        holder.medicineName.setText(list.get(position).getMedicineName());
        holder.name.setText("name: "+list.get(position).getName());
        holder.amount.setText("₹"+list.get(position).getAmount());
        holder.tax.setText("₹"+list.get(position).getTax());
        holder.orderid.setText("id"+list.get(position).getOrderID());
        float amount = Float.parseFloat(list.get(position).getAmount());
        float tax = Float.parseFloat(list.get(position).getTax());
        float total = amount + tax;

        holder.changeOdr.setText("Accept");
        holder.cancelOdr.setText("Cancel");

        holder.total.setText("₹"+String.valueOf(total));
        holder.phone.setText("phone: "+list.get(position).getPhone());


        if(currentItem.getUstatus().matches("ORDERED")){

            holder.cancelOdr.setEnabled(true);
            holder.changeOdr.setEnabled(true);
            //holder.cancelOdr.setText(currentItem.getUstatus());
        }

        if(currentItem.getUstatus().matches("CANCELLED")){

            holder.cancelOdr.setEnabled(false);
            holder.changeOdr.setEnabled(false);
            holder.cancelOdr.setText(currentItem.getUstatus());
        }

        else if(currentItem.getUstatus().matches("ACCEPTED")){

            //holder.cancelOdr.setEnabled(false);
            holder.changeOdr.setEnabled(false);
            holder.changeOdr.setText(currentItem.getUstatus());
        }
        else{

            holder.cancelOdr.setEnabled(true);
            holder.changeOdr.setEnabled(true);

        }


        holder.changeOdr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uorderID = currentItem.getOrderID();



                String umedID = currentItem.getUmedID();
                String orderStoreID = currentItem.getStoreId();
                String UID = currentItem.getUID();



               // bohonPlaceOrder(orderStoreID, UID, currentItem.getAddress());


                //inventory update

                DatabaseReference qref1 = FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("items")
                        .child(orderStoreID);

              //  Log.i("quantity vn 1", qref1.child(umedID).child("qty").toString());


                qref1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot shopsnapshot: snapshot.getChildren()){

                            DatabaseReference shopmedref = qref1.child(shopsnapshot.getKey());



                            shopmedref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    Log.i("medkeys", snapshot.getKey());

                                    if (snapshot.getKey().matches(umedID)){
                                        Log.i("quantity vn 3", snapshot.child("qty").getValue().toString());
                                        ItemData medData = snapshot.getValue(ItemData.class);
                                        int qty = Integer.parseInt(snapshot.child("qty").getValue().toString()) ;
                                        int custqty = Integer.parseInt(currentItem.getQuantity());


                                        qref1.child(umedID).child("qty").setValue(String.valueOf(qty - custqty));
                                        medData.setQty(String.valueOf(qty - custqty));

                                        Log.i("quantity vn upd", medData.getQty());

                                    }

                                    //    Log.i("quantity vn", String.valueOf(snapshot.child("qty")));
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


                DatabaseReference uidref = FirebaseDatabase
                        .getInstance()
                        .getReferenceFromUrl("https://smplmedicalapp-408ea-default-rtdb.firebaseio.com/userorders")
                        .child(UID)
                        .child(uorderID);

                //DatabaseReference statusref = uidref.child(umedID);




                uidref.child(umedID).child("ustatus").setValue("ACCEPTED");
                holder.changeOdr.setEnabled(false);

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

        holder.cancelOdr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uorderID = currentItem.getOrderID();

                String curstatus;


                String umedID = currentItem.getUmedID();
                String orderStoreID = currentItem.getStoreId();
                String UID = currentItem.getUID();


                //check status
                DatabaseReference uidref = FirebaseDatabase
                        .getInstance()
                        .getReferenceFromUrl("https://smplmedicalapp-408ea-default-rtdb.firebaseio.com/userorders")
                        .child(UID)
                        .child(uorderID);

                uidref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            DatabaseReference umedref = uidref.child(dataSnapshot.getKey());


                            umedref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    OrderModel statuscheck = snapshot.getValue(OrderModel.class);
                                    //curstatus = statuscheck.getUstatus();


                                    if(statuscheck.getUstatus().matches("ACCEPTED")){

                                        uidref.child(umedID).child("ustatus").setValue("CANCELLED");

                                        //inventory update

                                        DatabaseReference qref1 = FirebaseDatabase
                                                .getInstance()
                                                .getReference()
                                                .child("items")
                                                .child(orderStoreID);

                                        //  Log.i("quantity vn 1", qref1.child(umedID).child("qty").toString());


                                        qref1.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for(DataSnapshot shopsnapshot: snapshot.getChildren()){

                                                    DatabaseReference shopmedref = qref1.child(shopsnapshot.getKey());

                                                    shopmedref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                            Log.i("medkeys", snapshot.getKey());

                                                            if (snapshot.getKey().matches(umedID)){
                                                                Log.i("quantity vn 3", snapshot.child("qty").getValue().toString());
                                                                ItemData medData = snapshot.getValue(ItemData.class);

                                                                int qty = Integer.parseInt(snapshot.child("qty").getValue().toString()) ;
                                                                int custqty = Integer.parseInt(currentItem.getQuantity());


                                                                qref1.child(umedID).child("qty").setValue(String.valueOf(qty + custqty));
                                                                medData.setQty(String.valueOf(qty + custqty));

                                                                Log.i("quantity vn upd", medData.getQty());

                                                            }

                                                            //    Log.i("quantity vn", String.valueOf(snapshot.child("qty")));
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


                                    }else{
                                        uidref.child(umedID).child("ustatus").setValue("CANCELLED");
                                    }


                                    Log.i("ustatus cur", statuscheck.getUstatus());
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

                //Log.i("ustatus: ", uidref.child(umedID).child("ustatus").toString());

                holder.cancelOdr.setEnabled(false);
                holder.changeOdr.setEnabled(false);

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


                holder.cancelOdr.setText("Cancelled");
                holder.changeOdr.setText("Accept");

            }
        });

    }


   /* private void bohonPlaceOrder(String storeID, String userID, String cAddress) {

        final String[] pLoc = new String[1];
        final String[] pPhone = new String[1];
        final String[] pName = new String[1];
        final String[] dLoc = new String[1];
        final String[] dPhone = new String[1];
        final String[] dName = new String[1];


        JSONObject orderObject = new JSONObject();
        JSONObject drop = new JSONObject();
        JSONObject pickup = new JSONObject();


        DatabaseReference customerReference = FirebaseDatabase.getInstance()
                .getReference().child("customer");

        DatabaseReference storeReference = FirebaseDatabase.getInstance().getReference()
                .child("medicineProfile");


        storeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot store: snapshot.getChildren()){
                    if(store.getKey().matches(storeID)){

                        pLoc[0] = (String) store.child("address").getValue();
                        pPhone[0] = (String) store.child("phone").getValue();
                        pName[0] = (String) store.child("shopName").getValue();



                        try {
                            pickup.put("location", pLoc[0]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            pickup.put("vendor_name", pName[0]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            pickup.put("contact_no", pPhone[0]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Log.i(TAG, "bohonPlaceOrder: "+ pickup);

                        try {
                            orderObject.put("pickup", pickup);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.i(TAG, "bohonPlaceOrder: "+ orderObject);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        customerReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot store: snapshot.getChildren()){
                    if(store.getKey().matches(userID)){

                        dLoc[0] = cAddress;


                        dPhone[0] = (String) store.child("phone").getValue();
                        dName[0] = (String) store.child("name").getValue();



                        try {
                            drop.put("location", dLoc[0]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            drop.put("vendor_name", dName[0]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            drop.put("contact_no", dPhone[0]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Log.i(TAG, "bohonPlaceOrder: "+ drop);

                        try {
                            orderObject.put("drop", drop);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.i(TAG, "bohonPlaceOrder : "+ orderObject);


                    }
                }

                Log.i(TAG, "bohonPlaceOrder final 001: "+ orderObject);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Log.i(TAG, "bohonPlaceOrder final  2: "+ orderObject);

        // Log.i(TAG, "bohonPlaceOrder final: "+orderObject);
      *//*  Log.i(TAG, "bohonPlaceOrder: "+ pLoc[0]);
        Log.i(TAG, "bohonPlaceOrder: "+ dLoc[0]);
        Log.i(TAG, "bohonPlaceOrder: "+ pPhone[0]);
        Log.i(TAG, "bohonPlaceOrder: "+ dPhone[0]);
        Log.i(TAG, "bohonPlaceOrder: "+ pName[0]);
        Log.i(TAG, "bohonPlaceOrder: "+ dName[0]);*//*



        RequestQueue queue = Volley.newRequestQueue(getContext());



        final String url = "http://bohon.herokuapp.com/api/order/generate/";

        // prepare the Request

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        //registerUser( email, password,fullname,  phone,username);

                        try {


                            JSONObject obj = new JSONObject(response);

                            String b_id = obj.getString("id");
                            Log.i("password check", b_id);

                            Log.d("My App", obj.toString());



                        } catch (Throwable t) {
                            Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                        }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error

                        //Toast.makeText(RegisterActivity.this, "Choose a better password", Toast.LENGTH_SHORT).show();
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("first_name", fullname);
                params.put("last_name", "");
                params.put("email", email);
                params.put("password", password);
                params.put("password2", password2);
                params.put("user_type", u_type);

                Log.i("params", params.toString());
                return params;
            }
        };
        queue.add(postRequest);

        Log.i("call", "bohonRegister ");


    }*/



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class orderViewHolder extends RecyclerView.ViewHolder{

            TextView medicineName, address, name,phone, amount, tax,quantity, total, orderid;
            RelativeLayout relativeLayout;
            Button changeOdr, cancelOdr;

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
                cancelOdr = itemView.findViewById(R.id.cancelodr);
                relativeLayout = itemView.findViewById(R.id.order_relativeLayout);

            }
        }
}
