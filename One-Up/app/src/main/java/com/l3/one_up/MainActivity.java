package com.l3.one_up;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.l3.one_up.fragments.SleepFragment;
import com.l3.one_up.interfaces.BackIsClickable;
import com.l3.one_up.listeners.OnUserTogglesSleepListener;
import com.l3.one_up.model.User;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.l3.one_up.fragments.ActivitySelectionFragment;
import com.l3.one_up.fragments.HomeFragment;

import java.util.HashMap;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener, OnUserTogglesSleepListener {

    private static final String TAG_SLEEP_FRAGMENT = "sleepFragment";
    private static final String TAG_HOME_FRAGMENT = "homeFragment";
    private static final String TAG_ACTIVITY_SELECTION_FRAGMENT = "activitySelectionFragment";
    private Stack<String> tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tags = new Stack<>();
        selectDefaultFragment();
    }

    @Override
    public void onCategoryClick(String categoryName) {
        Toast.makeText(getApplicationContext(), categoryName + " button was clicked!", Toast.LENGTH_SHORT).show();
        ActivitySelectionFragment activitySelectionFragment = ActivitySelectionFragment.newInstance(categoryName);
        startFragment(activitySelectionFragment, TAG_ACTIVITY_SELECTION_FRAGMENT);
    }

    @Override
    public void onProfilePictureClick() {
        startActivity(ProfileActivity.class);
    }

    private void startActivity(Class activityClass) {
        Intent i = new Intent(this, activityClass);
        startActivity(i);
    }

    private void startFragment(Fragment fragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentHolder, fragment, tag);
        ft.addToBackStack("main").commit();
        tags.push(tag);
    }

    @Override
    public void toggleSleep(boolean asleep) {
        User user = User.getCurrentUser();
        if(asleep){
            HashMap<String, String> params = new HashMap();
            params.put("userId", user.getObjectId());
            ParseCloud.callFunctionInBackground("setSleepReminder", params, new FunctionCallback<String>() {
                @Override
                public void done(String value, ParseException e) {
                    if (e != null) {
                        e.printStackTrace();
                    }
                    Log.d("ParseCloud", "ok");
                }
            });

        }
        user.put("isAsleep", asleep);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    selectDefaultFragment();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Hey, there was a problem, you can't go to sleep :c", Toast.LENGTH_SHORT);
                    e.printStackTrace();
                }
            }
        });
    }

    private void selectDefaultFragment(){
        tags.clear();
        if(ParseUser.getCurrentUser().getBoolean("isAsleep")){
            startFragment(SleepFragment.newInstance(this), TAG_SLEEP_FRAGMENT);
        }
        else{
            startFragment(HomeFragment.newInstance(this), TAG_HOME_FRAGMENT);
        }
    }

    @Override
    public void onBackPressed() {
        final BackIsClickable backIsClickable = (BackIsClickable) getSupportFragmentManager().findFragmentByTag(tags.peek());
        if (backIsClickable.allowBackPressed()) {
            super.onBackPressed();
            tags.pop();
        }
    }
}
