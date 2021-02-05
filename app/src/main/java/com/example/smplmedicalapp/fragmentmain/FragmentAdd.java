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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Random.*;

import com.example.smplmedicalapp.GenerateRandomString;
import com.example.smplmedicalapp.ItemData;
import com.example.smplmedicalapp.MainActivity;
import com.example.smplmedicalapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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
    private Spinner spinner;
    File imgfile;
    public String[] size_list;
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
    public  String  val = "";
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

        ((MainActivity)getActivity()).checkOrder();
        Toast.makeText(getContext(), "Choose Image first !", Toast.LENGTH_SHORT).show();

        addbtn = view.findViewById(R.id.btn_additem);
        chooseimg = view.findViewById(R.id.choose_img);
        itemname = view.findViewById(R.id.name_value);
        imgname = view.findViewById(R.id.filename);
        itemdesc = view.findViewById(R.id.desc_value); //company
        spinner = view.findViewById(R.id.size_spinner);
        itemprice = view.findViewById(R.id.price);
        itemdiscount = view.findViewById(R.id.discount);
        itemqty = view.findViewById(R.id.qty);
        itemsize = view.findViewById(R.id.sizeml);

        imageView = view.findViewById(R.id.itemimageview);
        imageView.setImageResource(R.drawable.ic_baseline_add_a_photo_24);



        if(getArguments() != null){

            val = getArguments().getString("editname");
            addbtn.setText("Edit Item");

        }


        size_list = new String[3];
        size_list[0] = "Choose mg/mL";
        size_list[1] = "mL";
        size_list[2] = "mg";

        ArrayAdapter<String > aa=new ArrayAdapter<String> (getContext(),
                R.layout.support_simple_spinner_dropdown_item, size_list);

//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
//                R.array.size_array, android.R.layout.simple_spinner_item);
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // Apply the adapter to the spinner
        spinner.setAdapter(aa);
        spinner.setSelection(0);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        fillData(val);
        chooseimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImg();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImg();
            }
        });

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(val.matches(""))
                    AddItem();
                else{



                        EditItem(val);

                }

            }
        });
        return view;
    }

    private void fillData(String val) {




        /*int db_or_price = Integer.parseInt(itemprice.getText().toString());
        int db_dis = Integer.parseInt(itemdiscount.getText().toString());
        double db_dis_price = ((db_or_price) * (100 - db_dis))/100;*/
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Log.i(TAG, "onSuccess: " + user.getUid());
        String curuser = user.getUid();
        String sizefinal =  itemsize.getText().toString().concat(spinner.getSelectedItem().toString());
        databaseReference = FirebaseDatabase.getInstance().getReference("items").child(curuser);
        //String itemURL = storageReference.child("Images/").getDownloadUrl().toString();
        DatabaseReference editref =  FirebaseDatabase.getInstance().getReference()
                .child("items")
                .child(curuser);

        Query editquery = editref
                .orderByChild("name").equalTo(val) ;
        editquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){

                    databaseReference = databaseReference.child(ds.getKey());
                    ItemData getdata = ds.getValue(ItemData.class);

                    itemname.setText(getdata.getName());
                    itemdesc.setText(getdata.getCompany());
                    itemprice.setText(getdata.getPrice());
                    itemdiscount.setText(getdata.getDiscount());
                    itemqty.setText(getdata.getQty());
                    itemsize.setText(getdata.getSize().replaceAll("[^0-9]", ""));
                    imgname.setText(getdata.getImage());

                    String str = getdata.getSize();
                    StringBuffer unit = new StringBuffer();
                    for (int i=0; i<str.length(); i++)
                    {
                        if(Character.isAlphabetic(str.charAt(i)))
                            unit.append(str.charAt(i));

                    }

                    spinner.setSelection(unitpos(unit));
                    Log.i("get data", getdata.getName());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        String random = GenerateRandomString.randomString(19);



    }

    private int unitpos( StringBuffer unit) {

        int pos = -1 ;
        if(String.valueOf(unit).toLowerCase().equals(size_list[1].toLowerCase()))
            pos = 1;
        if(String.valueOf(unit).toLowerCase().equals(size_list[2].toLowerCase()))
            pos = 2;
        return pos;
    }

    private void EditItem(String val) {



        int db_or_price = Integer.parseInt(itemprice.getText().toString());
        int db_dis = Integer.parseInt(itemdiscount.getText().toString());
        double db_dis_price = ((db_or_price) * (100 - db_dis))/100;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Log.i(TAG, "onSuccess: " + user.getUid());
        String curuser = user.getUid();
        String sizefinal =  itemsize.getText().toString().concat(spinner.getSelectedItem().toString());
        databaseReference = FirebaseDatabase.getInstance().getReference("items").child(curuser);
        //String itemURL = storageReference.child("Images/").getDownloadUrl().toString();
        DatabaseReference editref =  FirebaseDatabase.getInstance().getReference()
                .child("items")
                .child(curuser);

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
                    Query editquery = editref
                            .orderByChild("name").equalTo(val) ;
                    editquery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot ds : snapshot.getChildren()){

                                databaseReference = databaseReference.child(ds.getKey());

                                if(!spinner.getSelectedItem().toString().matches("Choose mg/mL")) {


                                    databaseReference.child("image").setValue(filestring);
                                    databaseReference.child("name").setValue(itemname.getText().toString());
                                    databaseReference.child("company").setValue(itemdesc.getText().toString());
                                    databaseReference.child("price").setValue(itemprice.getText().toString());
                                    databaseReference.child("image").setValue(imgname.getText().toString());
                                    databaseReference.child("dprice").setValue(String.valueOf(db_dis_price));
                                    databaseReference.child("discount").setValue(itemdiscount.getText().toString());
                                    databaseReference.child("qty").setValue(itemqty.getText().toString());
                                    databaseReference.child("size").setValue(sizefinal);

                                    Toast.makeText(getView().getContext(), "Item Updated !!", Toast.LENGTH_SHORT).show();

                                    FragmentHome fragmentHome = new FragmentHome();
                                    FragmentManager manager = getFragmentManager();
                                    manager.beginTransaction()

                                            .replace(R.id.HomeActivity, fragmentHome, fragmentHome.getTag())
                                            .commit();

                                }
                                else{
                                    Toast.makeText(getView().getContext(), "Choose mg/mL !", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getView().getContext(), "Item Update Failed !!", Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot
                            .getTotalByteCount());
                    progressDialog.setMessage("Item Updated " + (int) progress + "%");

                }
            });
        }




        String random = GenerateRandomString.randomString(19);


    }

    private void AddItem() {

        if (itemname.getText().toString().matches("") || itemdesc.getText().toString().matches("") ||
                itemprice.getText().toString().matches("") ||
                itemdiscount.getText().toString().matches("") || itemqty.getText().toString().matches("")
                || itemsize.getText().toString().matches("") || spinner.getSelectedItem().toString().matches("Choose mg/mL")) {
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
                        String sizefinal =  itemsize.getText().toString().concat(spinner.getSelectedItem().toString());
                        databaseReference = FirebaseDatabase.getInstance().getReference("items");
                        //String itemURL = storageReference.child("Images/").getDownloadUrl().toString();
                        String random = GenerateRandomString.randomString(19);
                        databaseReference.child(curuser).child(random).child("image").setValue(filestring);
                        databaseReference.child(curuser).child(random).child("name").setValue(itemname.getText().toString());
                        databaseReference.child(curuser).child(random).child("company").setValue(itemdesc.getText().toString());
                        databaseReference.child(curuser).child(random).child("price").setValue(itemprice.getText().toString());


                        databaseReference.child(curuser).child(random).child("dprice").setValue(String.valueOf(db_dis_price));
                        databaseReference.child(curuser).child(random).child("discount").setValue(itemdiscount.getText().toString());
                        databaseReference.child(curuser).child(random).child("qty").setValue(itemqty.getText().toString());
                        databaseReference.child(curuser).child(random).child("size").setValue(sizefinal);

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