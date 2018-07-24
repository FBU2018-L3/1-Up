package com.l3.one_up;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.l3.one_up.model.FacebookQuery;

import java.util.ArrayList;
import java.util.Arrays;

public class DeepLinkingActivity extends AppCompatActivity {
    private String tag = "DeepLinkActivity";
    private LoginButton btFBConnect;
    private Button btTestFriends;
    public CallbackManager callbackManager;
    final private static String EMAIL = "email";
    final private static String FRIENDS = "user_friends";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_linking);

        callbackManager = CallbackManager.Factory.create();
        btFBConnect = findViewById(R.id.btFBConnect);
        btTestFriends = findViewById(R.id.btTestFriends);
        btFBConnect.setReadPermissions(Arrays.asList(EMAIL, FRIENDS));
//        LoginManager loginManager = LoginManager.getInstance();
        // Callback registration
        btFBConnect.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), "Login sucessful", Toast.LENGTH_LONG).show();
                Log.d(tag, "FB Login Sucessful");
                Log.d(tag, "Printing our access token: " + loginResult.getAccessToken().getUserId());
                AccessToken test = AccessToken.getCurrentAccessToken();
                Log.d(tag, "Testing. Please give me a user id: " + test.getUserId());
                Log.d(tag, "Get token?: " + test.getUserId());
            }

            @Override
            public void onCancel() {
                Log.d(tag, "Cancelled our fb login");
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Failed to login to FB :(", Toast.LENGTH_LONG).show();
                Log.d(tag, "Failed to login to FB");
                error.printStackTrace();
            }
        });

        btTestFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testFriends();
            }
        });
    }

    public boolean isLoggedIn(){
        AccessToken test = AccessToken.getCurrentAccessToken();
        if(test == null){
            Log.d(tag, "Guess it is null???");
            return false;
        }
        else{
            Log.d(tag, "What is printed?: " + test.getToken());
            return true;
        }
    }

    private void testFriends() {
        if(isLoggedIn()) logout();
        else Toast.makeText(getApplicationContext(), "User is already logged out!", Toast.LENGTH_LONG).show();
    }

    public void logout() {
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.logOut();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(tag, "We in here");
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
