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
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.l3.one_up.fragments.FeedFragment;
import com.l3.one_up.fragments.ProfileFragment;
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
                        /* creating a new Instance of our feed fragment */
                        boolean isTimeline = true; // for clarity's sake
                        FeedFragment timelineFragment = FeedFragment.newInstance(isTimeline);
                        startFragment(timelineFragment);
                        return true;
                    case R.id.action_friends:
                        Toast.makeText(getApplicationContext(), "Friends section is under construction!", Toast.LENGTH_LONG).show();
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

    public boolean isFacebookLoggedIn(){
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if(accessToken == null) return false;
        else return true;
    }

    public void FacebookLogout(){
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.logOut();
    }

    @Override
    public void onLogoutClicked() {
        ParseUser.logOut();
        if(isFacebookLoggedIn()){
            Log.d("HomeActivity", "Logging out the Facebook user");
            FacebookLogout();
        }
        Log.d("HomeActivity", "User logged out");
        Intent returnToLogin = new Intent(ProfileActivity.this, LoginActivity.class);
        returnToLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(returnToLogin);
    }
}
