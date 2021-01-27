package com.example.smplmedicalapp.fragmentmain;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smplmedicalapp.R;
import com.example.smplmedicalapp.SearchModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private TextView textView;

    private RecyclerView recyclerView;
    //ItemAdapter itemAdapter;
    DatabaseReference databaseReference;

    private SearchAdapter adapter;
    private List<SearchModel> exampleList=new ArrayList<>();

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



        fillExampleList(val);

       /* recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
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
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.i("check srch", query.toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

*/

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
//    @Override
//    public void onStart() {
//        super.onStart();
//        adapter.startListening();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }

    private void fillExampleList(String val) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String curuser = user.getUid();
        String url = "https://smplmedicalapp-408ea-default-rtdb.firebaseio.com"; //https://smplmedicalapp-408ea-default-rtdb.firebaseio.com
        databaseReference = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(url)
                .child("items").child(curuser);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    Log.i("demo", ds.getKey());


                    String name=ds.child("name").getValue().toString();
                    String company=ds.child("company").getValue().toString();
                    String image=ds.child("image").getValue().toString();
                    String discount=ds.child("discount").getValue().toString();
                    //String dprice=ds.child("dprice").getValue().toString();
                    String price=ds.child("price").getValue().toString();
                    String qty=ds.child("qty").getValue().toString();
                    String size=ds.child("size").getValue().toString();

                    SearchModel item = new SearchModel(name, company, image,
                            price,  size, qty, discount);
                    exampleList.add(item);
                }

                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new
                        LinearLayoutManager(getContext());
                adapter = new SearchAdapter(exampleList);
                recyclerView.setLayoutManager(layoutManager);
                adapter.getFilter().filter(val);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ExampleViewHolder> implements Filterable {
        private List<SearchModel> exampleList;
        private List<SearchModel> exampleListFull;

        @Override
        public Filter getFilter() {
            return examplefilter;
        }

        public class ExampleViewHolder extends RecyclerView.ViewHolder {

            public ImageView imageView;
            public TextView name, desc, price, qty, discount, size, org_price, discountPrice ;
            public Button button, edit;
            public RelativeLayout relativeLayout;

            public ExampleViewHolder(View itemView){
                super(itemView);
                this.edit = itemView.findViewById(R.id.item_edit);
                this.imageView = itemView.findViewById(R.id.item_img);
                this.name = itemView.findViewById(R.id.item_name);
                this.desc = itemView.findViewById(R.id.item_desc);
                this.org_price = itemView.findViewById(R.id.item_original_price);
                this.discountPrice = itemView.findViewById(R.id.item_price);
                this.discount = itemView.findViewById(R.id.item_discount);
                this.qty = itemView.findViewById(R.id.item_qty);
                this.size = itemView.findViewById(R.id.item_size);
                this.button = itemView.findViewById(R.id.btn_remove);
                // button.setVisibility(View.INVISIBLE);
                relativeLayout = itemView.findViewById(R.id.item_relativeLayout);
            }

        }
        public SearchAdapter(List<SearchModel> exampleList) {
            this.exampleList = exampleList;
            exampleListFull = new ArrayList<>(exampleList);
        }
        @NonNull
        @Override
        public SearchAdapter.ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.add_item_layout, parent, false);
            return new SearchAdapter.ExampleViewHolder(v);
        }
        @Override
        public void onBindViewHolder(@NonNull SearchAdapter.ExampleViewHolder holder, int position) {
            SearchModel currentItem = exampleList.get(position);
            //holder.imageView.setImageResource(currentItem.getImageResource());
            //holder.textView2.setText(currentItem.getText2());


            holder.name.setText(currentItem.getName());
            holder.desc.setText(currentItem.getDescription());
            holder.org_price.setText("₹"+currentItem.getPrice());


            float discount = Integer.parseInt(currentItem.getDiscount());
            float price = Integer.parseInt(currentItem.getPrice());

            int dprice = (int) ((price*(100-discount))/100);
            holder.discountPrice.setText("₹"+dprice);
            holder.qty.setText("Qty :"+ currentItem.getQty());
            holder.discount.setText(currentItem.getDiscount()+"%off");
            holder.size.setText("Size :"+ currentItem.getSize());

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editItem(currentItem.getName());
                }
            });

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  DatabaseReference removeref =  FirebaseDatabase.getInstance().getReference()
                    .child("items")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                Query removequery = removeref
                        .orderByChild("name").equalTo(currentItem.getName()) ;

                removequery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds : snapshot.getChildren()){

                            ds.getRef().removeValue();
                            adapter.notifyDataSetChanged();

                            FragmentHome fragmentHome = new FragmentHome();
                            FragmentManager manager = getFragmentManager();
                            manager.beginTransaction()

                                    .replace(R.id.HomeActivity, fragmentHome, fragmentHome.getTag())
                                    .commit();

                            break;

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                Toast.makeText( v.getContext(), "Removed !", Toast.LENGTH_SHORT).show();


            }
        });


            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage
                    .getReferenceFromUrl("gs://smplmedicalapp-408ea.appspot.com/Images/")
                    .child(currentItem.getImage());
            File file = null;
            try {
                file = File.createTempFile("image", "jpg");
            } catch (IOException e) {
                e.printStackTrace();
            }

            File finalFile = file;
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(finalFile.getAbsolutePath());
                    holder.imageView.setImageBitmap(bitmap);
                }
            });

        }
        @Override
        public int getItemCount() {
            return exampleList.size();
        }

        private Filter examplefilter= new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<SearchModel> filterlist=new ArrayList<>();
                if(constraint==null|| constraint.length()==0){
                    filterlist.addAll(exampleListFull);
                }
                else{
                    String pattrn=constraint.toString().toLowerCase().trim();
                    for(SearchModel item :exampleListFull){
                        if(item.getName().toLowerCase().contains(pattrn)){
                            filterlist.add(item);
                        }
                    }
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=filterlist;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                exampleList.clear();
                exampleList.addAll((List)results.values);
                notifyDataSetChanged();
            }
        };
    }

    private void editItem(String name) {


        Bundle bundleString = new Bundle();
        bundleString.putString("editname", name);


        FragmentAdd fragmentAdd = new FragmentAdd();
        fragmentAdd.setArguments(bundleString);
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction()

                .replace(R.id.HomeActivity, fragmentAdd, fragmentAdd.getTag())
                .commit();
    }

}