package com.example.smplmedicalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.service.autofill.Dataset;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smplmedicalapp.fragmentmain.FragmentAdd;
import com.example.smplmedicalapp.fragmentmain.FragmentHome;
import com.example.smplmedicalapp.fragmentmain.FragmentUserFilterOrder;
import com.example.smplmedicalapp.fragmentmain.Profile_Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public TextView badgeview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener
                    ((BottomNavigationView.OnNavigationItemSelectedListener)
                    this);

        BottomNavigationMenuView bottomNavigationMenuView
                = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(2);
        BottomNavigationItemView itemView
                = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(this)
                .inflate(R.layout.badge_layout, itemView, true);



        badgeview = findViewById(R.id.notifications_badge);
        badgeview.setVisibility(View.INVISIBLE);

        //count start


     /*   final Handler handler = new Handler();
        final int delay = 10000; // 1000 milliseconds == 1 second


        handler.postDelayed(new Runnable() {

            int secondsAlive = 0;
            public void run() {
                Log.i("check order", String.valueOf(secondsAlive));
                secondsAlive+=delay;
               // checkCart();
                checkOrder();
                handler.postDelayed(this, delay);
            }
        }, delay);
*/


       checkOrder();

        //count end

       // String countval = String.valueOf(getodrcount());
       /* Log.i("count check 2", String.valueOf(finalcount[0]));

        if(finalcount[0] !=0 ) {
            badgeview.setText(finalcount[0]);
            badgeview.setVisibility(View.VISIBLE);
        }

*/
        newFragment(new FragmentHome());

    }

    public void checkOrder() {


        badgeview.setVisibility(View.INVISIBLE);
        final int[] finalcount = {0};

        DatabaseReference countreference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("userorders");

        countreference.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot usnap: snapshot.getChildren()){

                    DatabaseReference odrReference = countreference.child(usnap.getKey());

                    odrReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot osnap : snapshot.getChildren()){
                                DatabaseReference medref = odrReference.child(osnap.getKey());

                                //Log.i("medref ", osnap.getKey());
                                medref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        for (DataSnapshot medIDsnap: snapshot.getChildren()){


                                            if(String.valueOf(medIDsnap.child("ustatus").getValue()).matches("ORDERED")  &&
                                                    String.valueOf(medIDsnap.child("storeId").getValue()).matches(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            ){

                                                Log.i("medID status ", String.valueOf(medIDsnap.child("ustatus").getValue()));
                                                finalcount[0] =5;
                                                badgeview.setVisibility(View.VISIBLE);
                                                break;
                                            }
                                            Log.i("count check", String.valueOf(finalcount[0]));

                                            medIDsnap.getChildrenCount();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

//        Log.i("final count", String.valueOf(count));

    }

    public  boolean newFragment(Fragment fragment){

        if(fragment!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.HomeActivity, fragment).commit();
            return true;
        }
        return false;
    }


   @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.app_home:
                fragment = new FragmentHome();
                break;
            case R.id.add_item:
                fragment = new FragmentAdd();
                break;
            case R.id.profile:
                fragment = new Profile_Fragment();
                break;
            case R.id.order:
                fragment = new FragmentUserFilterOrder();
                break;
            case R.id.deliery:
                Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
                break;
        }
        return newFragment(fragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}