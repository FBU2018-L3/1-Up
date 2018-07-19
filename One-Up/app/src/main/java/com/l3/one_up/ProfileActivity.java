package com.l3.one_up;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.parse.ParseUser;

public class ProfileActivity extends AppCompatActivity implements ProfileFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final ProfileFragment profileFragment = ProfileFragment.newInstance();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_stats:
                        // do something here
                        return true;
                    case R.id.action_profile:
                        startFragment(profileFragment);
                        return true;
                    case R.id.action_timeline:
                        // do something here
                        return true;
                }
                return false;
            }
        });

        startFragment(profileFragment);

    }

    private void startFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentHolder, fragment);
        ft.commit();
    }

    @Override
    public void onLogoutClicked() {
        ParseUser.logOut();
        Log.d("HomeActivity", "User logged out");
        Intent returnToLogin = new Intent(ProfileActivity.this, LoginActivity.class);
        returnToLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(returnToLogin);
    }
}
