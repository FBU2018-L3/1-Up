package com.l3.one_up;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etUsername) EditText etUsername;
    @BindView(R.id.etPassword) EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.l3.one_up.model.Activity.Query query = new com.l3.one_up.model.Activity.Query();
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if(ParseUser.getCurrentUser()!=null){
            startHomeActivity();
        }

        //Obtaining the sleep activity to persist it during all the app
        query.getInBackground("v3IoWcJGy9", new GetCallback<com.l3.one_up.model.Activity>() {
            @Override
            public void done(com.l3.one_up.model.Activity sleepActivity, ParseException e) {
                if(e == null){
                    sleepActivity.pinInBackground();
                }
                else{
                    Log.d("LoginActivity", "failed");
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick(R.id.btnLogin)
    public void login(){
        login(etUsername.getText().toString(), etPassword.getText().toString());
    }

    public void login(String user, String pass){
        toast("LogIn");
        ParseUser.logInInBackground(etUsername.getText().toString(), etUsername.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e==null){
                    toast("Logged in");
                    startHomeActivity();
                }
                else {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity", e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick(R.id.btnSignUp)
    public void signUp(){
        final String userName = etUsername.getText().toString();
        final String pass = etUsername.getText().toString();
        ParseUser user = new ParseUser();
        user.setUsername(userName);
        user.setPassword(pass);
        user.put("isAsleep", false);
        user.signUpInBackground(new SignUpCallback(){

            @Override
            public void done(ParseException e) {
                if(e==null){
                    login(userName, pass);
                }
                else{
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    Log.e("LoginActivity", e.getMessage());
                }
            }
        });
    }

    private void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void startHomeActivity(){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }
}
