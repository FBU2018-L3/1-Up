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
import com.l3.one_up.interfaces.FacebookCallComplete;
import com.l3.one_up.model.FacebookQuery;
import com.l3.one_up.model.FacebookUser;

import java.util.ArrayList;
import java.util.Arrays;

public class DeepLinkingActivity extends AppCompatActivity implements FacebookCallComplete {
    private String tag = "DeepLinkActivity";
    private LoginButton btFBConnect;
    private Button testButton;
    public CallbackManager callbackManager;
    final private static String EMAIL = "email";
    final private static String FRIENDS = "user_friends";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_linking);

        callbackManager = CallbackManager.Factory.create();
        btFBConnect = findViewById(R.id.btFBConnect);
        testButton = findViewById(R.id.btTestButton);
        btFBConnect.setReadPermissions(Arrays.asList(EMAIL, FRIENDS));

        btFBConnect.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                /* We can use this LoginResult object to see recently granted permissions and to
                * get the access token (sets our access token to the current access token ) */
                Toast.makeText(getApplicationContext(), "Login sucessful", Toast.LENGTH_LONG).show();
                Log.d(tag, "FB Login Sucessful");
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

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Do test things", Toast.LENGTH_LONG).show();
                FacebookQuery fb = new FacebookQuery();
                fb.getFriends(DeepLinkingActivity.this);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void notifyDataChanged(ArrayList<FacebookQuery.FacebookUser> list) {
        Log.d(tag, "size: " + list.size());
    }

}