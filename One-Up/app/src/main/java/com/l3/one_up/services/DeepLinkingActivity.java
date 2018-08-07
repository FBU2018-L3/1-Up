package com.l3.one_up.services;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.l3.one_up.R;
import com.l3.one_up.model.User;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Arrays;

public class DeepLinkingActivity extends AppCompatActivity {
    private String tag = "DeepLinkActivity";
    private LoginButton btFBConnect;
    private ImageView userAvatar;
    public CallbackManager callbackManager;
    final private static String EMAIL = "email";
    final private static String FRIENDS = "user_friends";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_linking);

        callbackManager = CallbackManager.Factory.create();
        btFBConnect = findViewById(R.id.btFBConnect);
        btFBConnect.setReadPermissions(Arrays.asList(EMAIL, FRIENDS));

        userAvatar = findViewById(R.id.ivParseUserPic);
        userAvatar.setImageResource(new AvatarFinder().getAvatarId(getApplicationContext()));

        btFBConnect.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                /* We can use this LoginResult object to see recently granted permissions and to
                * get the access token (sets our access token to the current access token ) */
                Toast.makeText(getApplicationContext(), "Login sucessful", Toast.LENGTH_LONG).show();
                Log.d(tag, "FB Login Sucessful");
                /* store the ID within the parse user to develop that connection */
                /* NOTE: every time a facebook user is logged into our app, their facebook id is updated! */
                String userId = AccessToken.getCurrentAccessToken().getUserId();
                User currUser = (User) ParseUser.getCurrentUser();
                Log.d(tag, "Current user is: " + currUser.getUsername());
                Log.d(tag, "Facebook id is: " + userId);
                currUser.setFacebookId(userId);
                currUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.d(tag, "Updated parse user with facebook id");
                    }
                });
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

    /* on activity result needed for facebook login */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}