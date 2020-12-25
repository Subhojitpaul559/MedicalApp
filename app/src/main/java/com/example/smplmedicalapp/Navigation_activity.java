package com.example.smplmedicalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Navigation_activity extends AppCompatActivity {
    CardView meds;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_activity);
        mAuth = FirebaseAuth.getInstance();

        meds = findViewById(R.id.medicine);
        meds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
         }
        });
        if(getSupportActionBar()!=null){
            getSupportActionBar().hide();

        }
    }


    protected void checkLogin() {

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser == null){
            startActivity(new Intent(this, ClientLogin.class));
            finish();
        }else{
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        finish();
    }
}