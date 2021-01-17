package com.example.smplmedicalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.smplmedicalapp.fragmentmain.FragmentAdd;
import com.example.smplmedicalapp.fragmentmain.FragmentHome;
import com.example.smplmedicalapp.fragmentmain.FragmentUserFilterOrder;
import com.example.smplmedicalapp.fragmentmain.Profile_Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

           newFragment(new FragmentHome());


            BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) this);

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
        }
        return newFragment(fragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}