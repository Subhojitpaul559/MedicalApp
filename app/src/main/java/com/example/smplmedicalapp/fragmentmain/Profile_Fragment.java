package com.example.smplmedicalapp.fragmentmain;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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


public class Profile_Fragment extends Fragment {

   private ImageView Displaypic,Edit_pic;
   private TextView Person,Org,Email_id,Mobile_no,address_client,gst_no,Logout_txt,reset_password;
   private Button edit_btn;
    private FirebaseAuth mAuth;


    public Profile_Fragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_, container, false);

        Logout_txt = view.findViewById(R.id.logout);
        reset_password = view.findViewById(R.id.reset_pass);
        mAuth = FirebaseAuth.getInstance();
        Logout_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), ClientLogin.class);
                startActivity(intent);
            }
        });

        return view;

    }

}