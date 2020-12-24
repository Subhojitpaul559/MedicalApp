package com.example.smplmedicalapp.fragmentmain;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Random.*;

import com.example.smplmedicalapp.GenerateRandomString;
import com.example.smplmedicalapp.MainActivity;
import com.example.smplmedicalapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAdd#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAdd extends Fragment {

    private Button addbtn;
    private TextView chooseimg, imgname;
    private EditText itemname, itemdesc, itemprice, itemsize,itemdiscount, itemqty;
    private Uri imgpath;
    private ImageView imageView;
    File imgfile;
    private final int PICK_IMAGE = 100;

    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String filestring;
    public FragmentAdd() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAdd.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAdd newInstance(String param1, String param2) {
        FragmentAdd fragment = new FragmentAdd();
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
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        addbtn = view.findViewById(R.id.btn_additem);
        chooseimg = view.findViewById(R.id.choose_img);
        itemname = view.findViewById(R.id.name_value);
        imgname = view.findViewById(R.id.filename);
        itemdesc = view.findViewById(R.id.desc_value);

        itemprice = view.findViewById(R.id.price);
        itemdiscount = view.findViewById(R.id.discount);
        itemqty = view.findViewById(R.id.qty);
        itemsize = view.findViewById(R.id.sizeml);

        imageView = view.findViewById(R.id.itemimageview);


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        chooseimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImg();
            }
        });

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItem();
            }
        });
        return view;
    }

    private void AddItem() {

        if (itemname.getText().toString().matches("") || itemdesc.getText().toString().matches("") ||
                itemprice.getText().toString().matches("") ||
                itemdiscount.getText().toString().matches("") || itemqty.getText().toString().matches("")
                || itemsize.getText().toString().matches("")) {
            Toast.makeText(getActivity(), "Fill all fields !", Toast.LENGTH_SHORT).show();
        } else {
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();
            if (imgpath != null) {
                final ProgressDialog progressDialog
                        = new ProgressDialog(getView().getContext());
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                StorageReference ref = storageReference
                        .child("Images/" + "" + filestring);
                ref.putFile(imgpath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        //String itemID = databaseReference.push().getKey();
                        int db_or_price = Integer.parseInt(itemprice.getText().toString());
                        int db_dis = Integer.parseInt(itemdiscount.getText().toString());
                        double db_dis_price = ((db_or_price) * (100 - db_dis))/100;
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        Log.i(TAG, "onSuccess: " + user.getUid());
                        String curuser = user.getUid();
                        databaseReference = FirebaseDatabase.getInstance().getReference("items");
                        //String itemURL = storageReference.child("Images/").getDownloadUrl().toString();
                        String random = GenerateRandomString.randomString(9);
                        databaseReference.child(curuser).child(random).child("image").setValue(filestring);
                        databaseReference.child(curuser).child(random).child("name").setValue(itemname.getText().toString());
                        databaseReference.child(curuser).child(random).child("description").setValue(itemdesc.getText().toString());
                        databaseReference.child(curuser).child(random).child("price").setValue(itemprice.getText().toString());


                        databaseReference.child(curuser).child(random).child("dprice").setValue(String.valueOf(db_dis_price));
                        databaseReference.child(curuser).child(random).child("discount").setValue(itemdiscount.getText().toString());
                        databaseReference.child(curuser).child(random).child("qty").setValue(itemqty.getText().toString());
                        databaseReference.child(curuser).child(random).child("size").setValue(itemsize.getText().toString());

                        Toast.makeText(getView().getContext(), "Uploaded !!", Toast.LENGTH_SHORT).show();

                        FragmentHome fragmentHome = new FragmentHome();
                        FragmentManager manager = getFragmentManager();
                        manager.beginTransaction()

                                .replace(R.id.HomeActivity, fragmentHome, fragmentHome.getTag())
                                .commit();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getView().getContext(), "Failed !!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    }
                });

           /* storageReference.child("Images/").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    databaseReference.child("items/").child("hjufnrimt").child("image").setValue(uri.toString());
                    databaseReference.child("items/").child("hjufnrimt").child("name").setValue(imgname);

                }
            });*/

            }
        }
    }


    private void ChooseImg() {

        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        //intent.setType("image/*");
       // intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "choose item image... "), PICK_IMAGE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK
        && data != null && data.getData() != null){
            imgpath = data.getData();
             imgfile = new File(""+Uri.parse(String.valueOf(imgpath)));

              filestring = imgfile.getName();
             int pos = imgfile.getName().lastIndexOf("%2F");
             filestring = filestring.substring(pos+3);
            imgname.setText(filestring);
            imageView.setImageURI(imgpath);
           // imgname.setText(data.getDataString());
            //Bitmap bitmap = MediaStore.Image.Media.getBitmap(get)
           // displayitem.setImageBitmap(BitmapFactory.decodeFile(imgpath.toString()));

        }

        
    }

}