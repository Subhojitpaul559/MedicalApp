package com.example.smplmedicalapp.fragmentmain;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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


public class Profile_Fragment extends Fragment {

   private ImageView Displaypic,Edit_pic;
   private TextView Person,Org,Email_id,Mobile_no,address_client,gst_no,Logout_txt,reset_password;
   private Button edit_btn;
   private TextView resetpassword;
    private FirebaseAuth mAuth;
 DatabaseReference databaseReference;

    public Profile_Fragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_, container, false);

        Person = view.findViewById(R.id.Name);
        Logout_txt = view.findViewById(R.id.logout);
        reset_password = view.findViewById(R.id.reset_pass);
        Org = view.findViewById(R.id.organisation_name);
        Email_id = view.findViewById(R.id.Email);
        Mobile_no = view.findViewById(R.id.phone);
        address_client = view.findViewById(R.id.address);
        gst_no = view.findViewById(R.id.gst);
        resetpassword = view.findViewById(R.id.gotoresetpasswword);
        edit_btn = view.findViewById(R.id.EditAccount);

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

        mAuth = FirebaseAuth.getInstance();

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

        return view;

    }

}