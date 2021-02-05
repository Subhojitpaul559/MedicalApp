package com.example.smplmedicalapp.fragmentmain;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smplmedicalapp.ItemData;
import com.example.smplmedicalapp.MainActivity;
import com.example.smplmedicalapp.R;
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

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

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

    private ItemAdapter adapter;
    private List<ItemData> itemlist=new ArrayList<>();
    DatabaseReference databaseReference;
    public ProgressBar progressBar;
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

        ((MainActivity)getActivity()).checkOrder();
       // progressBar = view.findViewById(R.id.progressbar);
        //progressBar.setVisibility(View.VISIBLE);
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
       //recyclerView.setVisibility(View.INVISIBLE);
      // progressBar.setVisibility(View.VISIBLE);
       recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));


       String url = "https://smplmedicalapp-408ea-default-rtdb.firebaseio.com"; //https://smplmedicalapp-408ea-default-rtdb.firebaseio.com
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

       // Log.i(TAG, "onSuccess: "+user.getUid());
        String curuser = user.getUid();
       databaseReference = FirebaseDatabase.getInstance()
               .getReferenceFromUrl(url).child("items").child(curuser);


       databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                   Log.i("MedID", dataSnapshot.getKey());

                   ItemData itemData = dataSnapshot.getValue(ItemData.class);

                   String name = itemData.getName();
                   String company= itemData.getCompany();
                   String image = itemData.getImage();
                   String price = itemData.getPrice();
                   String size = itemData.getSize();
                   String qty = itemData.getQty();
                   String discount = itemData.getDiscount();

                   ItemData allitems = new ItemData(name, company, image, price, size, qty,discount);

                   itemlist.add(allitems);
               }


               //RecyclerView recyclerView = view.findViewById(R.id.rclv1);
               recyclerView.setHasFixedSize(true);
               RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
               adapter = new ItemAdapter(itemlist);
               recyclerView.setLayoutManager(layoutManager);
               //adapter.getFilter().filter(val);
               recyclerView.setAdapter(adapter);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });


       /*FirebaseRecyclerOptions<ItemData> options =
                   new FirebaseRecyclerOptions.
                           Builder<ItemData>().setQuery(databaseReference, ItemData.class).
                           build();
           itemAdapter = new ItemAdapter(options);
           recyclerView.setAdapter(itemAdapter);



               new Handler().postDelayed(new Runnable() {

                   @Override
                   public void run() {
                       recyclerView.setVisibility(View.VISIBLE);
                       progressBar.setVisibility(View.GONE);
                   }

               }, 1500);*/

               //progressBar.setVisibility(View.GONE);

           //progressBar.setVisibility(View.GONE);
        //pgsBar.setVisibility(View.GONE);


        return view;
    }

    public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

        private List<ItemData> list;


        public ItemAdapter(List<ItemData> list) {
            this.list = list;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            ItemData model = list.get(position);
            holder.name.setText(model.getName());
            holder.desc.setText(model.getCompany());
            holder.org_price.setText("₹"+model.getPrice());

            float discount = Integer.parseInt(model.getDiscount());
            float price = Integer.parseInt(model.getPrice());

            int dprice = (int) ((price*(100-discount))/100);


            holder.discountPrice.setText("₹"+dprice);
            holder.qty.setText("Qty :"+ model.getQty());
            holder.discount.setText(model.getDiscount()+"%off");
            holder.size.setText("Size :"+ model.getSize());

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editItem(model.getName());
                }
            });

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    DatabaseReference removeref =  FirebaseDatabase.getInstance().getReference()
                            .child("items")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    Query removequery = removeref
                            .orderByChild("name").equalTo(model.getName()) ;

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
                    .child(model.getImage());
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
            return list.size();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.add_item_layout
                    , parent, false);

            return  new ViewHolder(view);
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView imageView;
            public TextView name, desc, price, qty, discount, size, org_price, discountPrice ;
            public Button button, edit;

            public RelativeLayout relativeLayout;

            public ViewHolder(View itemView){
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
                relativeLayout = itemView.findViewById(R.id.item_relativeLayout);
            }

            public void setItem(ItemData item){
                name.setText(item.getName());
                desc.setText(item.getCompany());
                qty.setText(item.getQty());
                discount.setText(item.getDiscount());
                org_price.setText(item.getPrice());

                float discount = Integer.parseInt(item.getDiscount());
                float price = Integer.parseInt(item.getPrice());

                int dprice = (int) ((price*(100-discount))/100);

                discountPrice.setText(dprice);
            }

        }
    }

    private void editItem(String itemName) {

        Bundle bundleString = new Bundle();
        bundleString.putString("editname", itemName);


        FragmentAdd fragmentAdd = new FragmentAdd();
        fragmentAdd.setArguments(bundleString);
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction()

                .replace(R.id.HomeActivity, fragmentAdd, fragmentAdd.getTag())
                .commit();
    }


}