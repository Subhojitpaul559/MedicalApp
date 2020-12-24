package com.example.smplmedicalapp.fragmentmain;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smplmedicalapp.ClientRegisterActivity;
import com.example.smplmedicalapp.MainActivity;
import com.example.smplmedicalapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentEditAcc#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentEditAcc extends Fragment {
    private EditText mOrg_name,mClient_name,mPhn_no,mEmail,mFull_address,mGst,mPassword;
    private Button update;
    DatabaseReference databaseReference;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentEditAcc() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentEditAcc.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentEditAcc newInstance(String param1, String param2) {
        FragmentEditAcc fragment = new FragmentEditAcc();
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
        View view = inflater.inflate(R.layout.fragment_edit_acc, container, false);
        mOrg_name=view.findViewById(R.id.Uorganisation_name);
        mClient_name=view.findViewById(R.id.Uperson_name);
        mPhn_no=view.findViewById(R.id.Uclient_phn_no);
        mEmail=view.findViewById(R.id.Uclient_email);
        mFull_address=view.findViewById(R.id.Uclient_address);
        mGst=view.findViewById(R.id.Ugst_no);
        update=view.findViewById(R.id.btn_update);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
       update.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              databaseReference = FirebaseDatabase.getInstance().getReference("users");
               if(!mClient_name.getText().toString().matches("")) { databaseReference.child(mAuth.getUid()).child("client_name").setValue(mClient_name.getText().toString());}
               if(!mOrg_name.getText().toString().matches(""))  { databaseReference.child(mAuth.getUid()).child("org_name").setValue(mOrg_name.getText().toString());}
               if(!mPhn_no.getText().toString().matches("") || mPhn_no.getText().toString().length() ==10)   { databaseReference.child(mAuth.getUid()).child("phone").setValue(mPhn_no.getText().toString());}
               if(!mEmail.getText().toString().matches(""))  { databaseReference.child(mAuth.getUid()).child("email").setValue(mEmail.getText().toString());}
               if(!mFull_address.getText().toString().matches("")) { databaseReference.child(mAuth.getUid()).child("full_addr").setValue(mFull_address.getText().toString());}
               if(!mGst.getText().toString().matches("") || isValidGSTNo(mGst.getText().toString())) { databaseReference.child(mAuth.getUid()).child("gst").setValue(mGst.getText().toString()); }
               Toast.makeText(getContext(), "User Updated", Toast.LENGTH_SHORT).show();

               Profile_Fragment profile_fragment = new Profile_Fragment();
               FragmentManager manager = getFragmentManager();
               manager.beginTransaction()

                       .replace(R.id.HomeActivity, profile_fragment, profile_fragment.getTag())
                       .commit();

           }
       });
        return view;
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