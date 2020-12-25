package com.example.smplmedicalapp.fragmentmain;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.example.smplmedicalapp.ItemAdapter;
import com.example.smplmedicalapp.ItemData;
import com.example.smplmedicalapp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.StringReader;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentHome extends Fragment {
    private RecyclerView recyclerView;
    private View view;
    private SearchView itemsearchView;
    ItemAdapter itemAdapter;
    DatabaseReference databaseReference;
   // ProgressBar pgsBar ;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHome.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHome newInstance(String param1, String param2) {
        FragmentHome fragment = new FragmentHome();
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

        view = inflater.inflate(R.layout.fragment_home, container, false);
       // pgsBar = view.findViewById(R.id.pBar);
        //pgsBar.setVisibility(view.GONE);

        itemsearchView = view.findViewById(R.id.searchItem);
        itemsearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.i(TAG, "onQueryTextSubmit: "+query);
                Bundle bundleString = new Bundle();
                bundleString.putString("key", query);
                SearchFragment searchFragment = new SearchFragment();
                searchFragment.setArguments(bundleString);
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction()

                        .replace(R.id.HomeActivity, searchFragment, searchFragment.getTag())
                        .commit();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

       recyclerView = view.findViewById(R.id.rclv1);
       recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
       String url = "https://smplmedicalapp-408ea-default-rtdb.firebaseio.com"; //https://smplmedicalapp-408ea-default-rtdb.firebaseio.com
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

       // Log.i(TAG, "onSuccess: "+user.getUid());
        String curuser = user.getUid();
       databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(url).child("items").child(curuser);


           FirebaseRecyclerOptions<ItemData> options =
                   new FirebaseRecyclerOptions.
                           Builder<ItemData>().setQuery(databaseReference, ItemData.class).
                           build();
           itemAdapter = new ItemAdapter(options);
           recyclerView.setAdapter(itemAdapter);
        //pgsBar.setVisibility(View.GONE);


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