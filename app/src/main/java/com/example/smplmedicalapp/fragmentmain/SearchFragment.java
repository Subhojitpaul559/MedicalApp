package com.example.smplmedicalapp.fragmentmain;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.smplmedicalapp.ItemAdapter;
import com.example.smplmedicalapp.ItemData;
import com.example.smplmedicalapp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private TextView textView;

    private RecyclerView recyclerView;
    ItemAdapter itemAdapter;
    DatabaseReference databaseReference;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        textView = view.findViewById(R.id.tv1);
        String val = getArguments().getString("key");
        Log.i(TAG, "searchactivity: " + val);
        //textView.setText(val);
        recyclerView = view.findViewById(R.id.searchrclv);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        String url = "https://smplmedicalapp-408ea-default-rtdb.firebaseio.com"; //https://smplmedicalapp-408ea-default-rtdb.firebaseio.com
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Log.i(TAG, "onSuccess: " + user.getUid());
        String curuser = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(url).child("items").child(curuser);
        Query query = databaseReference.orderByChild("name_lowercase").startAt(val.toLowerCase()).endAt(val.toLowerCase()+"\uf8ff");



        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseRecyclerOptions<ItemData> options = new FirebaseRecyclerOptions
                .Builder<ItemData>()
                .setQuery(query, ItemData.class)
                .build();
        itemAdapter = new ItemAdapter(options);
        recyclerView.setAdapter(itemAdapter);

       /* if (searchItemName.indexOf(val) == 0) {

            FirebaseRecyclerOptions<ItemData> options =
                    new FirebaseRecyclerOptions.
                            Builder<ItemData>().setQuery(databaseReference, ItemData.class).
                            build();
            itemAdapter = new ItemAdapter(options);
            recyclerView.setAdapter(itemAdapter);
        }*/
            return view;
        }
    @Override
    public void onStart() {
        super.onStart();
        itemAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        itemAdapter.stopListening();
    }
}