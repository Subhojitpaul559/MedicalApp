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
import android.widget.ListView;
import android.widget.Toast;

import com.example.smplmedicalapp.OrderAdapter;
import com.example.smplmedicalapp.OrderModel;
import com.example.smplmedicalapp.R;
import com.example.smplmedicalapp.SearchModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

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

       // listView = view.findViewById(R.id.userlistview);

        //recyclerView = view.findViewById(R.id.allorder_rclv);

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



}