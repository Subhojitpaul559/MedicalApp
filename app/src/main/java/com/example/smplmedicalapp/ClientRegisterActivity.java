package com.example.smplmedicalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientRegisterActivity extends AppCompatActivity {

    private EditText mOrg_name,mClient_name,mPhn_no,mEmail,mFull_address,mGst,mPassword;
    private Button mBtn_submit;
    private FirebaseAuth mAuth;
    private TextView Signin;
    private ProgressBar progressbar;

    DatabaseReference databaseReference;

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





        mBtn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=mEmail.getText().toString().trim();
                String password=mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email address required");
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
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                if(mOrg_name.getText().toString().matches("") ||
                        mClient_name.getText().toString().matches("") || !mEmail.getText().toString().trim().matches(emailPattern)||
                        mFull_address.getText().toString().matches(""))
                {

                    Toast.makeText(getApplicationContext(), "Enter all details; follow proper format !", Toast.LENGTH_SHORT).show();
                }else if( mPhn_no.getText().toString().matches("") ||
                                mPhn_no.getText().length() != 10){

                    Toast.makeText(getApplicationContext(), "Enter phone (10 digits only) !", Toast.LENGTH_SHORT).show();
                }else if(!isValidGSTNo(mGst.getText().toString())){

                    Toast.makeText(getApplicationContext(), "Invalid GSTN !", Toast.LENGTH_SHORT).show();
                }else {

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                databaseReference = FirebaseDatabase.getInstance().getReference("users");
                                databaseReference.child(mAuth.getUid()).child("org_name").setValue(mOrg_name.getText().toString());
                                databaseReference.child(mAuth.getUid()).child("client_name").setValue(mClient_name.getText().toString());
                                databaseReference.child(mAuth.getUid()).child("phone").setValue(mPhn_no.getText().toString());
                                databaseReference.child(mAuth.getUid()).child("email").setValue(mEmail.getText().toString());
                                databaseReference.child(mAuth.getUid()).child("full_addr").setValue(mFull_address.getText().toString());
                                databaseReference.child(mAuth.getUid()).child("gst").setValue(mGst.getText().toString());


                                Toast.makeText(ClientRegisterActivity.this, "User created", Toast.LENGTH_SHORT).show();
                                Log.i("UID", "onComplete: " + mAuth.getUid());
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));


                            } else {
                                Toast.makeText(ClientRegisterActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressbar.setVisibility(View.GONE);
                            }

                        }
                    });

                }

            }
        });

        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext() , ClientLogin.class));
            }
        });


    }
    public static boolean isValidGSTNo(String str)
    {
        String regex = "^[0-9]{2}[A-Z]{5}[0-9]{4}"
                + "[A-Z]{1}[1-9A-Z]{1}"
                + "Z[0-9A-Z]{1}$";
        Pattern p = Pattern.compile(regex);
        if (str == null) {
            return false;
        }
        Matcher m = p.matcher(str);
        return m.matches();
    }
}