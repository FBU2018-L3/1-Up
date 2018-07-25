package com.l3.one_up.model;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by luzcamacho on 7/24/18.
 */

public class FacebookUser {
    private static String tag = "FacebookUser";
    public String Username;
    public String UserID;
    public String UserProfilePicUrl;



    public FacebookUser(String Username, String UserID){
        this.Username = Username;
        this.UserID = UserID;
    }

    public void setProfilePic(String parseURL){
        this.UserProfilePicUrl = parseURL;
    }

    public static class Query {
        private String USER_QUERY_ID;

        public Query() {
            this.USER_QUERY_ID = AccessToken.getCurrentAccessToken().getUserId();
        }

        public ArrayList<FacebookUser> getFacebookFriends(){
            final ArrayList<FacebookUser> facebookFriends = new ArrayList<>();
            /*set to get friends */
            final String PARAM = "/friends";
            /* combo for the full URL */
            final String FULL_URL = USER_QUERY_ID + PARAM;
            /* Send our graphrequest */
            GraphRequest request = GraphRequest.newGraphPathRequest(AccessToken.getCurrentAccessToken(),
                    FULL_URL, new GraphRequest.Callback() {
                        @Override
                        public void onCompleted(GraphResponse response) {
                            Log.d(tag, "Graph request completed");
                            JSONObject jsonObject = response.getJSONObject();
                            /* return the JSON array labeled "data" */
                            try {
                                JSONArray friendData = jsonObject.getJSONArray("data");
                                Log.d(tag, "We got an array of JSON data info");
                                for(int i = 0; i < friendData.length(); i++) {
                                    /* Each friend is in a JSON object  */
                                    JSONObject aFriend = friendData.getJSONObject(i);
                                    String Username = aFriend.getString("name");
                                    String UserID = aFriend.getString("id");
                                    Log.d(tag, "Username: " + Username + " UserID: " + UserID);
                                    FacebookUser single = new FacebookUser(Username, UserID);
                                    facebookFriends.add(single);
                                }
                            } catch (JSONException e) {
                                Log.d(FacebookUser.tag, "Failed to get data array");
                                e.printStackTrace();
                            }
                        }
                    });
            request.executeAsync();
            return facebookFriends;
        }

        public void getUserProfilePicture(String id){
            /* strings to get final base url */
            final String PARAM = "/picture";
            final String TYPE = "?type=square";
            Bundle fields = new Bundle();
            fields.putBoolean("redirect", false);
            final String FULL_URL = id + PARAM + TYPE;
            GraphRequest graphRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), FULL_URL,
                    fields, HttpMethod.GET, new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse response) {
                    Log.d(tag, "We completed the thing!");
                    JSONObject ourResponse = response.getJSONObject();
                    try {
                        JSONObject data = ourResponse.getJSONObject("data");
                        String parsedUrl = data.getString("url");
                    } catch (JSONException e) {
                        Log.d(tag, "Failed to parse data");
                        e.printStackTrace();
                    }
                }
            });
            graphRequest.executeAsync();
        }

    }
}
