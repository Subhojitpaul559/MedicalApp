package com.example.smplmedicalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



       /* if(null != mAuth.getCurrentUser()){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        else{
            startActivity(new Intent(getApplicationContext(),ClientLogin.class));
            finish();
        }*/

        if(getSupportActionBar()!=null) {
            getSupportActionBar().hide();

            Thread thread = new Thread() {

                public void run() {
                    try {
                        sleep(2500);

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        Intent intent = new Intent(SplashActivity.this, Navigation_activity.class);
                        startActivity(intent);
                    }

                }
            };
            thread.start();
        }

    }
}