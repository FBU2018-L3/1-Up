package com.l3.one_up;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.l3.one_up.R;
import com.l3.one_up.fragments.SleepFragment;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragment;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(ParseUser.getCurrentUser().getBoolean("isAsleep")){
            fragment = HomeFragment.newInstance();
        }
        else{
            fragment = SleepFragment.newInstance();
        }
        ft.replace(R.id.fragmentHolder, fragment);
        ft.commit();
    }

    @Override
    public void onCategoryClick(String categoryName) {
        Toast.makeText(getApplicationContext(), categoryName + " button was clicked!", Toast.LENGTH_SHORT).show();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ActivitySelectionFragment fragmentDemo = ActivitySelectionFragment.newInstance(categoryName);
        ft.replace(R.id.fragmentHolder, fragmentDemo);
        ft.addToBackStack("tag").commit();

    }
}
