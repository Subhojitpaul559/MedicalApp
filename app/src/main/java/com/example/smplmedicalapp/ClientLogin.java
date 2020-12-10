package com.example.smplmedicalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.text.TextUtils.*;

public class ClientLogin extends AppCompatActivity {

    private EditText mEmail,mPassword;
    private TextView Sign_up_txt,forgotten_pass;
    private Button mBtn_login;
    private FirebaseAuth mAuth;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_login);

        mEmail=findViewById(R.id.client_email);
        mPassword=findViewById(R.id.pass);
        mBtn_login=findViewById(R.id.btn_login);
        Sign_up_txt=findViewById(R.id.signup_pg);
        forgotten_pass=findViewById(R.id.reset_pass);

        mAuth = FirebaseAuth.getInstance();
        progressbar=findViewById(R.id.progressbar);

        mBtn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=mEmail.getText().toString().trim();
                String password= mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email address required.");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password required");
                    return;
                }
                if (password.length() <6){
                    mPassword.setError("Password must have 6 charecters");
                    return;
                }

                progressbar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ClientLogin.this, "Login successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ClientLogin.this, MainActivity.class));

                        } else {
                            Toast.makeText(ClientLogin.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressbar.setVisibility(View.GONE);

                        }
                    }
                });
            }
        });

        Sign_up_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext() , ClientRegisterActivity.class));
            }
        });

    }
}