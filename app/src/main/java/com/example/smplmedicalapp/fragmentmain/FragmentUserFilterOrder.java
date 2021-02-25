package com.example.smplmedicalapp.fragmentmain;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smplmedicalapp.ItemData;
import com.example.smplmedicalapp.MainActivity;
import com.example.smplmedicalapp.OrderModel;
import com.example.smplmedicalapp.R;
import com.example.smplmedicalapp.SearchModel;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static java.security.AccessController.getContext;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentUserFilterOrder#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentUserFilterOrder extends Fragment {

    DatabaseReference databaseReference;
    ListView listView;
   // ArrayAdapter<String> arrayAdapteruser;
   // String[] userArray;


    RecyclerView recyclerView;
    private OrderAdapter adapter;
    public TextView badge;
    private List<OrderModel> orderlist=new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentUserFilterOrder() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentUserFilterOrder.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentUserFilterOrder newInstance(String param1, String param2) {
        FragmentUserFilterOrder fragment = new FragmentUserFilterOrder();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_user_filter_order, container, false);

        View view2 = getActivity().findViewById(R.id.HomeActivity);
       // ((MainActivity)getActivity()).checkOrder();
        badge = view2.findViewById(R.id.notifications_badge);
        badge.setVisibility(View.INVISIBLE);

       // listView = view.findViewById(R.id.userlistview);

        //recyclerView = view.findViewById(R.id.allorder_rclv);
        //badge


        List<String> ulist = new ArrayList<String>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String url = "https://smplmedicalapp-408ea-default-rtdb.firebaseio.com/";
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(url).child("userorders");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DatabaseReference chref = FirebaseDatabase.getInstance()
                            .getReferenceFromUrl(url).child("userorders").child(dataSnapshot.getKey());

                    chref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot snapshot2: snapshot.getChildren()){

                                Log.i("StoreID", snapshot2.getKey());

                                DatabaseReference medref = chref.child(snapshot2.getKey());



                                medref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot snapshot3: snapshot.getChildren()) {

                                            Log.i("check keys 3", snapshot3.getKey());
                                            OrderModel orderModel = snapshot3.getValue(OrderModel.class);
                                            //orderModel.
                                            Log.i("childval3:-- ", String.valueOf(snapshot3.getValue()));

                                            Log.i("joe styles", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                           if (FirebaseAuth.getInstance().getCurrentUser()
                                                   .getUid().equals(orderModel.getStoreId())  &&  !orderModel.getUstatus().matches("CART")) {
                                            Log.i("curuser", String.valueOf(user));
                                            String name = orderModel.getName();
                                            String mName = orderModel.getMedicineName();
                                            String address = orderModel.getAddress();
                                            String amount = orderModel.getAmount();
                                            String phone = orderModel.getPhone();
                                            String tax = orderModel.getTax();
                                            String quantity = orderModel.getQuantity();



                                               DecimalFormat df = new DecimalFormat();
                                               df.setMaximumFractionDigits(2);
                                              // System.out.println(df.format(decimalNumber));
                                            String total = df.format(Float.parseFloat(amount) + Float.parseFloat(tax));
                                            String orderID = snapshot2.getKey();
                                            String storeId = orderModel.getStoreId();
                                            String umedID = orderModel.getUmedID();
                                            String UID = orderModel.getUID();
                                            String ustatus = orderModel.getUstatus();

                                            Log.i("storeId", storeId);

                                            Log.i("childval3//amount:-- ", String.valueOf(orderModel.getAmount()));

                                            OrderModel item = new OrderModel(mName, quantity, amount, address,
                                                    name, phone, tax, total, orderID, storeId, umedID, UID, ustatus);
                                            orderlist.add(item);

                                            Log.i("orderlist value :--", String.valueOf(orderlist));


                                            }
                                        }

                                        RecyclerView recyclerView = view.findViewById(R.id.allorder_rclv);
                                        recyclerView.setHasFixedSize(true);
                                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                                        adapter = new OrderAdapter(orderlist);
                                        recyclerView.setLayoutManager(layoutManager);
                                        //adapter.getFilter().filter(val);
                                        recyclerView.setAdapter(adapter);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                Log.i("childval2:-- ", String.valueOf(snapshot2.getKey()));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    Log.i("childval", String.valueOf(dataSnapshot.getValue()));

                    ulist.add(dataSnapshot.getKey());

                }

                /* userArray = new String[ulist.size()];
                userArray = ulist.toArray(userArray);
                 arrayAdapteruser = new ArrayAdapter<String>(view.getContext(),
                        R.layout.userlist, ulist);
                listView.setAdapter(arrayAdapteruser);*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(view.getContext(), userArray[position], Toast.LENGTH_SHORT).show();

               // Log.i(TAG, "onQueryTextSubmit: "+query);
                Bundle bundleString = new Bundle();
                bundleString.putString("userid", userArray[position]);


                FragmentOrder fragmentOrder = new FragmentOrder();
                fragmentOrder.setArguments(bundleString);
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction()

                        .replace(R.id.HomeActivity, fragmentOrder, fragmentOrder.getTag())
                        .commit();

            }
        });*/

        /*RecyclerView recyclerView = view.findViewById(R.id.allorder_rclv);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new OrderAdapter(orderlist);
        recyclerView.setLayoutManager(layoutManager);
        //adapter.getFilter().filter(val);
        recyclerView.setAdapter(adapter);*/

        return view;
    }



    public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.orderViewHolder> {
        private List<OrderModel> list;

        public OrderAdapter(List<OrderModel> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public OrderAdapter.orderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //Context context = parent.getContext();
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent, false);
            return new OrderAdapter.orderViewHolder(v);
        }

        @Override
        public  void onBindViewHolder(@NonNull OrderAdapter.orderViewHolder holder, int position) {

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



                    bohonPlaceOrder(orderStoreID, UID, currentItem.getAddress());


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


        private void bohonPlaceOrder(String storeID, String userID, String cAddress) {

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
                    try {
                        orderObject.put("product_weight", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.i(TAG, "bohonPlaceOrder final 001: "+ orderObject);

                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    final String url = "http://bohonapi.herokuapp.com/api/order/generate/";

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.POST,url, orderObject, new com.android.volley.Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        //TODO: Handle your response here
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    Log.i("Order Response ", response.toString());
                                   // Toast.makeText(getContext(), "BohonAPI called", Toast.LENGTH_SHORT).show();

                                }
                            }, new com.android.volley.Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO: Handle error
                                    error.printStackTrace();
                                    Log.i(TAG, "onErrorResponse: "+error);

                                }


                            }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {

                            Map<String, String> headers = new HashMap<>();
                            String auth = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6" +
                                    "InZlbmRvcm5hbWUwMDEiLCJpYXQiOjE2MTMxNDUyMTYsImV4cCI6MTYxNTczNzI" +
                                    "xNiwianRpIjoiZjdhYzQ3ZDYtMWViNC00Zjc4LWI2OWUtZDhiMzY4YTgwMDgwIiwidXNlcl" +
                                    "9pZCI6M30.Kc3IAXxDF-dS0s8jueuPhQ8Gv6rwt1xYI4aXEAglW4o";
                            headers.put("Authorization", "Bearer "+auth);
                            headers.put("Accept", "*/*");
                            headers.put("Accept-Encoding", "gzip, deflate, br");
                            headers.put("Connection", "keep-alive");
                            headers.put("Content-Type", "application/json");
                            return headers;
                        }


                    } ;

                    jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
                        @Override
                        public int getCurrentTimeout() {
                            return 20000;
                        }

                        @Override
                        public int getCurrentRetryCount() {
                            return 20000;
                        }

                        @Override
                        public void retry(VolleyError error) throws VolleyError {

                        }
                    });
                    queue.add(jsonObjectRequest);
                    Log.i(TAG, "onDataChange: Order Generated");


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


         //  Log.i("final BOHON", "bohonPlaceOrder final  2: "+ orderObject);

            // Log.i(TAG, "bohonPlaceOrder final: "+orderObject);
      /*  Log.i(TAG, "bohonPlaceOrder: "+ pLoc[0]);
        Log.i(TAG, "bohonPlaceOrder: "+ dLoc[0]);
        Log.i(TAG, "bohonPlaceOrder: "+ pPhone[0]);
        Log.i(TAG, "bohonPlaceOrder: "+ dPhone[0]);
        Log.i(TAG, "bohonPlaceOrder: "+ pName[0]);
        Log.i(TAG, "bohonPlaceOrder: "+ dName[0]);*/


/*

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
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> headers = new HashMap<>();
                    String auth = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InZlbmRvcm5hbWUwMDEiLCJpYXQiOjE2MTMxNDUyMTYsImV4cCI6MTYxNTczNzIxNiwianRpIjoiZjdhYzQ3ZDYtMWViNC00Zjc4LWI2OWUtZDhiMzY4YTgwMDgwIiwidXNlcl9pZCI6M30.Kc3IAXxDF-dS0s8jueuPhQ8Gv6rwt1xYI4aXEAglW4o";
                    headers.put("Authorization", auth);
                    headers.put("Content-Type", "application/json");
                    return headers;
                }


            };
            queue.add(postRequest);
*/

            Log.i("call", "bohonRegister ");

        }





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




}