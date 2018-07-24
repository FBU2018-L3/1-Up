package com.l3.one_up.model;

import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by luzcamacho on 7/23/18.
 */

public class FacebookQuery {
    private String tag = "FacebookQuery";
    private String USER_ID;
    private String PARAM;

    public FacebookQuery(){
        this.USER_ID = AccessToken.getCurrentAccessToken().getUserId();
    }

    public ArrayList<FacebookUser> getFriends() {
        ArrayList<FacebookUser> userFriends = new ArrayList<>();
        PARAM = "friends";
        String fullURL = USER_ID + PARAM;
        GraphRequest request = GraphRequest.newGraphPathRequest(AccessToken.getCurrentAccessToken(),
                fullURL, new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        JSONObject jsonObject = response.getJSONObject();
                        try {
                            JSONArray friendData = jsonObject.getJSONArray("data");
                        } catch (JSONException e) {
                            Log.d(tag, "Something went wrong in our facbeook query :(");
                            e.printStackTrace();
                        }
                    }
                });
        request.executeAsync();
        return userFriends;
    }

    public class FacebookUser {
        public String Username;
        public String UserID;

        public FacebookUser(String Username, String ID){
            this.Username = Username;
            this.UserID = ID;
        }
    }
}
