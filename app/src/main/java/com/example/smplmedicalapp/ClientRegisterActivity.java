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

public class ClientRegisterActivity extends AppCompatActivity {

    private EditText mOrg_name,mClient_name,mPhn_no,mEmail,mFull_address,mGst,mPassword;
    private Button mBtn_submit;
    private FirebaseAuth mAuth;
    private TextView Signin;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_register);

        mOrg_name=findViewById(R.id.organisation_name);
        mClient_name=findViewById(R.id.person_name);
        mPhn_no=findViewById(R.id.client_phn_no);
        mEmail=findViewById(R.id.client_email);
        mFull_address=findViewById(R.id.client_address);
        mGst=findViewById(R.id.gst_no);
        mPassword=findViewById(R.id.pass);
        mBtn_submit=findViewById(R.id.btn_submit);
        mAuth = FirebaseAuth.getInstance();
        progressbar=findViewById(R.id.progress);
        Signin=findViewById(R.id.signin_pg);

        if(mAuth.getCurrentUser() !=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();

        }

        mBtn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=mEmail.getText().toString().trim();
                String password=mPassword.getText().toString().trim();

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

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ClientRegisterActivity.this, "User created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        }else {
                            Toast.makeText(ClientRegisterActivity.this, "Error!"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressbar.setVisibility(View.GONE);
                        }

                    }
                });



            }
        });

        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext() , ClientLogin.class));
            }
        });


    }
}