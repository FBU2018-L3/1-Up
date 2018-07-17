package com.l3.one_up;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.l3.one_up.R;

import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        HomeFragment fragmentDemo = HomeFragment.newInstance();
        ft.replace(R.id.fragmentHolder, fragmentDemo);
        ft.commit();
    }

    @Override
    public void onCategoryClick(String categoryName) {
        Toast.makeText(getApplicationContext(), categoryName + " button was clicked!", Toast.LENGTH_SHORT).show();
    }
}
