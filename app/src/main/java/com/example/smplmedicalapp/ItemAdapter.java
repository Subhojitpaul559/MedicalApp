package com.example.smplmedicalapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smplmedicalapp.fragmentmain.FragmentHome;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.FirebaseRecyclerOptions.Builder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static java.util.Objects.*;

public class ItemAdapter extends FirebaseRecyclerAdapter<ItemData, ItemAdapter.ViewHolder> {



    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ItemAdapter(@NonNull FirebaseRecyclerOptions<ItemData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position,
                                    @NonNull ItemData model) {
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
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("items")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(getRef(position).getKey())
                        .removeValue();
                Toast.makeText( v.getContext(), "Deleted !", Toast.LENGTH_SHORT).show();

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
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_item_layout
        , parent, false);

        return new ItemAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView name, desc, price, qty, discount, size, org_price, discountPrice ;
        public Button button;

        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView){
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
            relativeLayout = itemView.findViewById(R.id.item_relativeLayout);
        }

    }

}
