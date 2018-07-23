package com.l3.one_up;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class DeepLinkingActivity extends AppCompatActivity {
    private String tag = "DeepLinkActivity";
    private LoginButton btFBConnect;
    public CallbackManager callbackManager;
    final private static String EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_linking);

        callbackManager = CallbackManager.Factory.create();
        btFBConnect = findViewById(R.id.btFBConnect);
        btFBConnect.setReadPermissions(Arrays.asList(EMAIL));

        // Callback registration
        btFBConnect.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), "Login sucessful", Toast.LENGTH_LONG).show();
                Log.d(tag, "FB Login Sucessful");
                Log.d(tag, "Printing our access token: " + loginResult.getAccessToken().getUserId());
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(tag, "We in here");
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
