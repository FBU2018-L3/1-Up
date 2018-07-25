package com.l3.one_up;

import android.content.Intent;
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
import com.l3.one_up.fragments.ActivitySelectionFragment;
import com.l3.one_up.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.OnFragmentInteractionListener, OnUserTogglesSleepListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectFragment();
    }

    @Override
    public void onCategoryInteraction(String categoryName) {
        Toast.makeText(getApplicationContext(), categoryName + " button was clicked!", Toast.LENGTH_SHORT).show();
        ActivitySelectionFragment activitySelectionFragment = ActivitySelectionFragment.newInstance(categoryName);
        startFragment(activitySelectionFragment);
    }

    @Override
    public void onProfilePictureClick() {
        startActivity(ProfileActivity.class);
    }

    private void startActivity(Class activityClass) {
        Intent i = new Intent(this, activityClass);
        startActivity(i);
    }

    private void startFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentHolder, fragment);
        ft.addToBackStack("main").commit();
    }

    @Override
    public void toggleSleep(boolean asleep) {
        ParseUser user = ParseUser.getCurrentUser();
        user.put("isAsleep", asleep);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    selectFragment();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Hey, there was a problem, you can't go to sleep :c", Toast.LENGTH_SHORT);
                    e.printStackTrace();
                }
            }
        });
    }

    private void selectFragment(){
        if(ParseUser.getCurrentUser().getBoolean("isAsleep")){
            startFragment(SleepFragment.newInstance(this));
        }
        else{
            startFragment(HomeFragment.newInstance(this));
        }
    }
}
