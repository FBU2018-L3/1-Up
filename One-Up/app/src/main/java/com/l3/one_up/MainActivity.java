package com.l3.one_up;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.l3.one_up.fragments.ActivitySelectionFragment;
import com.l3.one_up.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HomeFragment homeFragment = HomeFragment.newInstance();
        startFragment(homeFragment);
    }

    @Override
    public void onCategoryClick(String categoryName) {
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
}
