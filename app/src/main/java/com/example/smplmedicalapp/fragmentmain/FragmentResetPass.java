package com.example.smplmedicalapp.fragmentmain;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smplmedicalapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentResetPass#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentResetPass extends Fragment {

    EditText  curpass,pass1, pass2;
    Button reset;
    String checkemail, checkpass;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentResetPass() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentResetPass.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentResetPass newInstance(String param1, String param2) {
        FragmentResetPass fragment = new FragmentResetPass();
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
        View view = inflater.inflate(R.layout.fragment_reset_pass, container, false);
        curpass = view.findViewById(R.id.curpass);
        pass1 = view.findViewById(R.id.pass1);
        pass2 = view.findViewById(R.id.pass2);

        reset = view.findViewById(R.id.btn_reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pass1.getText().toString().matches(pass2.getText().toString())){
                    Toast.makeText(getContext(), "New passwords don't match !", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                   DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getUid());


                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                          checkemail =  snapshot.child("email").getValue().toString();
                          checkpass = curpass.getText().toString();
                            Log.i("email found", checkemail);

                            AuthCredential credential = EmailAuthProvider.getCredential(checkemail, checkpass);
                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.i("check-user", "User re-authenticated.");
                                    if (task.isSuccessful()){
                                        user.updatePassword(pass2.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Log.i("user-check-pass", "onComplete: updated password");
                                                    Toast.makeText(getContext(), "Password Updated", Toast.LENGTH_LONG).show();
                                                    Profile_Fragment profile_fragment = new Profile_Fragment();
                                                    FragmentManager manager = getFragmentManager();
                                                    manager.beginTransaction()

                                                            .replace(R.id.HomeActivity, profile_fragment, profile_fragment.getTag())
                                                            .commit();
                                                }
                                                else {
                                                    Log.i("user-check-pass", "onFailure: cannot update password");
                                                }
                                            }
                                        });
                                    }

                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(),"Failed to authenticate", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.


                }
            }
        });


        return view;
    }
}