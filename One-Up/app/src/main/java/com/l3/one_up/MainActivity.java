package com.l3.one_up;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.l3.one_up.fragments.SleepFragment;
import com.l3.one_up.listeners.OnUserTogglesSleepListener;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener, OnUserTogglesSleepListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayFragment();
    }

    @Override
    public void onCategoryClick(String categoryName) {
        Toast.makeText(getApplicationContext(), categoryName + " button was clicked!", Toast.LENGTH_SHORT).show();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ActivitySelectionFragment fragmentDemo = ActivitySelectionFragment.newInstance(categoryName);
        ft.replace(R.id.fragmentHolder, fragmentDemo);
        ft.addToBackStack("tag").commit();

    }

    @Override
    public void toggleSleep(boolean asleep) {
        ParseUser user = ParseUser.getCurrentUser();
        user.put("isAsleep", asleep);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    displayFragment();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Hey, there was a problem, you can't go to sleep :c", Toast.LENGTH_SHORT);
                    e.printStackTrace();
                }
            }
        });
    }

    private void displayFragment(){
        Fragment fragment;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(ParseUser.getCurrentUser().getBoolean("isAsleep")){
            fragment = SleepFragment.newInstance(this);
        }
        else{
            fragment = HomeFragment.newInstance(this);
        }
        ft.replace(R.id.fragmentHolder, fragment);
        ft.commit();
    }
}
