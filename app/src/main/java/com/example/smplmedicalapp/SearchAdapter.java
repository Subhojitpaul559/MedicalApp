package com.example.smplmedicalapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ExampleViewHolder> implements  Filterable{
    private List<SearchModel> exampleList;
    private List<SearchModel> exampleListFull;

    @Override
    public Filter getFilter() {
        return examplefilter;
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView name, desc, price, qty, discount, size, org_price, discountPrice ;
        public Button button;
        public RelativeLayout relativeLayout;

        public ExampleViewHolder(View itemView){
            super(itemView);
            this.imageView = itemView.findViewById(R.id.item_img);
            this.name = itemView.findViewById(R.id.item_name);
            this.desc = itemView.findViewById(R.id.item_desc);
            this.org_price = itemView.findViewById(R.id.item_original_price);
            this.discountPrice = itemView.findViewById(R.id.item_price);
            this.discount = itemView.findViewById(R.id.item_discount);
            this.qty = itemView.findViewById(R.id.item_qty);
            this.size = itemView.findViewById(R.id.item_size);
            this.button = itemView.findViewById(R.id.btn_remove);
            button.setVisibility(View.INVISIBLE);
            relativeLayout = itemView.findViewById(R.id.item_relativeLayout);
        }

    }
    public SearchAdapter(List<SearchModel> exampleList) {
        this.exampleList = exampleList;
        exampleListFull = new ArrayList<>(exampleList);
    }
    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_item_layout, parent, false);
        return new ExampleViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
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

//        holder.button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseDatabase.getInstance().getReference().child("items")
//                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                        .child(getRef(position).getKey())
//                        .removeValue();
//                Toast.makeText( v.getContext(), "Deleted !", Toast.LENGTH_SHORT).show();
//
//            }
//        });


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage
                .getReferenceFromUrl("gs://smplmedicalapp-b4a88.appspot.com/Images/")
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
