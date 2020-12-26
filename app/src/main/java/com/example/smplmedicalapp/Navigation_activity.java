package com.example.smplmedicalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.MediaRouteButton;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Navigation_activity extends AppCompatActivity {
    CardView meds;
    FirebaseAuth mAuth;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_activity);
        mAuth = FirebaseAuth.getInstance();

        progressBar=findViewById(R.id.progressbar_nav);

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

        if(InternetConnection.checkConnection(getApplicationContext())) {

            if (firebaseUser == null) {
                startActivity(new Intent(this, ClientLogin.class));
                finish();
            } else {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
            progressBar.setVisibility(View.VISIBLE);
        }else {
            Toast.makeText(this, "Internet connection disable!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }
}