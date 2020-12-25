package com.example.smplmedicalapp.fragmentmain;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

import com.example.smplmedicalapp.ClientLogin;
import com.example.smplmedicalapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.content.ContentValues.TAG;


public class Profile_Fragment extends Fragment {

    private ImageView Displaypic, Edit_pic;
    private TextView Person, Org, Email_id, Mobile_no, address_client, gst_no, Logout_txt, reset_password;
    private Button edit_btn;
    private TextView resetpassword;
    private FirebaseAuth mAuth;
    private Uri imguri;

    FirebaseFirestore db;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    DatabaseReference databaseReference;

    public Profile_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_, container, false);

         mAuth = FirebaseAuth.getInstance();
        Person = view.findViewById(R.id.Name);
        Logout_txt = view.findViewById(R.id.logout);
        reset_password = view.findViewById(R.id.reset_pass);
        Org = view.findViewById(R.id.organisation_name);
        Email_id = view.findViewById(R.id.Email);
        Mobile_no = view.findViewById(R.id.phone);
        address_client = view.findViewById(R.id.address);
        gst_no = view.findViewById(R.id.gst);
        resetpassword = view.findViewById(R.id.gotoresetpasswword);
        Edit_pic = view.findViewById(R.id.ProfilePic);
        edit_btn = view.findViewById(R.id.EditAccount);

        db = FirebaseFirestore.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();
        StorageReference myProfileRef = firebaseStorage
                .getReference();
        myProfileRef.child("users/" +mAuth.getCurrentUser().getUid()+ "profile.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(Edit_pic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               Picasso.get().load(R.drawable.doc_img).into(Edit_pic);
            }
        });


        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentEditAcc fragmentEditAcc = new FragmentEditAcc();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction()

                        .replace(R.id.HomeActivity, fragmentEditAcc, fragmentEditAcc.getTag())
                        .commit();
            }
        });

        Log.i(TAG, mAuth.getUid());
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //String name =  snapshot.child("client_name").getValue().toString();
                //Log.i("test key: ", name);
                Person.setText(snapshot.child("client_name").getValue().toString());
                Org.setText(snapshot.child("org_name").getValue().toString());
                Email_id.setText(snapshot.child("email").getValue().toString());
                Mobile_no.setText(snapshot.child("phone").getValue().toString());
                address_client.setText(snapshot.child("full_addr").getValue().toString());
                gst_no.setText(snapshot.child("gst").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Logout_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), ClientLogin.class);
                startActivity(intent);
            }
        });

        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentResetPass fragmentResetPass = new FragmentResetPass();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction()

                        .replace(R.id.HomeActivity, fragmentResetPass, fragmentResetPass.getTag())
                        .commit();
            }
        });

       Edit_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open gallery

                Intent GalleryImg = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(GalleryImg, 1000);
            }
        });


        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                 imguri = data.getData();
                //Edit_pic.setImageURI(imguri);
                UploadImg(imguri);

            }
        }
    }

    /*private void uploadImageToFirebase(Uri imguri) {
        //upload image to firebase storage
        StorageReference fileRef = storageReference.child("users/" + mAuth.getCurrentUser().getUid() + "profile.jpg");
        fileRef.putFile(imguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(Edit_pic);
                    }
                });
                Toast.makeText(getContext(), "Image Uploaded.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Uploading Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    private void UploadImg(Uri imageuri){
        firebaseStorage = FirebaseStorage.getInstance();
        StorageReference imgref = firebaseStorage.getReference();
        imgref.child("users/" +mAuth.getCurrentUser().getUid()+ "profile.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.i("existing file path : ", "users/" +mAuth.getCurrentUser().getUid()+ "profile.jpg");
                imgref.child("users/" +mAuth.getCurrentUser().getUid()+ "profile.jpg").putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity().getApplicationContext(), "Image Uploaded.", Toast.LENGTH_SHORT).show();
                        FragmentHome fragmentHome = new FragmentHome();
                        FragmentManager manager = getFragmentManager();
                        manager.beginTransaction()

                                .replace(R.id.HomeActivity, fragmentHome, fragmentHome.getTag())
                                .commit();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), "Image Exists", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                imgref.child("users/" +mAuth.getCurrentUser().getUid()+ "profile.jpg").putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity().getApplicationContext(), "Image Uploaded.", Toast.LENGTH_SHORT).show();
                        FragmentHome fragmentHome = new FragmentHome();
                        FragmentManager manager = getFragmentManager();
                        manager.beginTransaction()

                                .replace(R.id.HomeActivity, fragmentHome, fragmentHome.getTag())
                                .commit();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), "Image Upload Failed !", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}

