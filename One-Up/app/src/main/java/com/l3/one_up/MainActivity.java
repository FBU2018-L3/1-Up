package com.l3.one_up;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.l3.one_up.R;
import com.l3.one_up.fragments.InputFragment;
import com.l3.one_up.model.Activity;
import com.parse.GetCallback;
import com.parse.ParseException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Activity.Query query = new Activity.Query();
        query.getAll().getFirstInBackground(new GetCallback<Activity>() {
            @Override
            public void done(Activity object, ParseException e) {
                FragmentManager fm = getSupportFragmentManager();
                InputFragment inputFragment = InputFragment.newInstance(object);
                inputFragment.show(fm, "Input");
            }
        });


    }
}
