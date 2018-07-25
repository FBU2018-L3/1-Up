package com.l3.one_up.model;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.l3.one_up.DeepLinkingActivity;
import com.l3.one_up.interfaces.FacebookCallComplete;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by luzcamacho on 7/23/18.
 */

public class FacebookQuery {
    private String tag = "FacebookQuery";
    private String USER_ID;

    public FacebookQuery(){
        this.USER_ID = AccessToken.getCurrentAccessToken().getUserId();
    }

    public ArrayList<FacebookUser> getFriends(final FacebookCallComplete callback) {
        final ArrayList<FacebookUser> userFriends = new ArrayList<>();
        /* our parameter since we are returning the friends */
        final String PARAM = "/friends";
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
                            callback.notifyDataChanged(userFriends);
                        } catch (JSONException e) {
                            Log.d(tag, "Something went wrong in our facbeook query :(");
                            e.printStackTrace();
                        }
                    }
                });
        request.executeAsync();
        return userFriends;
    }

    private ArrayList<String> getFacebookPro(String id) {
        final ArrayList<String> dumDataSet = new ArrayList<>();
        String profilePicUrl;

        final String PARAM = "/picture";
        final String TYPE = "?type=square";
        Bundle params = new Bundle();
        params.putBoolean("redirect", false);
        final String FULL_URL = id + PARAM + TYPE;
        Log.d(tag, "Full url: " + FULL_URL);
        GraphRequest graphRequest = new GraphRequest(AccessToken.getCurrentAccessToken(),
                FULL_URL, params, HttpMethod.GET, new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        Log.d(tag, "We completed the thing!");
                        JSONObject ourResponse = response.getJSONObject();
                        try {
                            JSONObject data = ourResponse.getJSONObject("data");
                            String url = data.getString("url");
                            if(data.has("url")) Log.d(tag, "sus");
                            Log.d(tag, "Our url: " + url);
                            dumDataSet.add(data.getString("url"));
                        } catch (JSONException e) {
                            Log.d(tag, "Failed to get picture data :(");
                            e.printStackTrace();
                        }
                    }


                });
        graphRequest.executeAsync();
        return dumDataSet;
    }

    /* simple class that holds basic user information. Can expand if we decide to extract more */
    public class FacebookUser {
        public String Username;
        public String UserID;
        public String UserProfilePicUrl;

        public FacebookUser(String Username, String ID){
            this.Username = Username;
            this.UserID = ID;
            ArrayList<String> placeholder = getFacebookPro(UserID);
//            UserProfilePicUrl = placeholder.get(0);
//            Log.d(tag, "prof: " + UserProfilePicUrl);
        }

        public void setProfilePic(String profilePic){
            UserProfilePicUrl = profilePic;
        }

    }
}
