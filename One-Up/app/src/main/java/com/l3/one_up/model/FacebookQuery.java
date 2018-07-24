package com.l3.one_up.model;

import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

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
        final ArrayList<FacebookUser> userFriends = new ArrayList<>();
        /* our parameter since we are returning the friends */
        PARAM = "/friends";
        /* combo for the full URL */
        String fullURL = USER_ID + PARAM;
        /* Send our graphrequest */
        GraphRequest request = GraphRequest.newGraphPathRequest(AccessToken.getCurrentAccessToken(),
                fullURL, new GraphRequest.Callback() {
                    /* App Code necessary for returning friend data */
                    @Override
                    public void onCompleted(GraphResponse response) {
                        Log.d(tag, "Graph request completed");
                        /* retreive the JSON object from the Facebook request */
                        JSONObject jsonObject = response.getJSONObject();
                        try {
                            /* return the JSON array labeled "data" */
                            JSONArray friendData = jsonObject.getJSONArray("data");
                            /* if the JSON array is empty, that means no user friends can be found logged into the app */
                            Log.d(tag, "We got an array of JSON data info");
                            for(int i = 0; i < friendData.length(); i++) {
                                /* Each friend is in a JSON object  */
                                JSONObject aFriend = friendData.getJSONObject(i);
                                String Username = aFriend.getString("name");
                                String UserID = aFriend.getString("id");
                                Log.d(tag, "Username: " + Username + " UserID: " + UserID);
                                FacebookUser single = new FacebookUser(Username, UserID);
                                userFriends.add(single);
                            }
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
