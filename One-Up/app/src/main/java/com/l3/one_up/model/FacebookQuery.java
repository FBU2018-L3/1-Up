package com.l3.one_up.model;

import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.l3.one_up.interfaces.FacebookCallComplete;

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

    public FacebookQuery(){
        this.USER_ID = AccessToken.getCurrentAccessToken().getUserId();
    }

    public void getFriends(final FacebookCallComplete callback) {
        final ArrayList<String> friendIds = new ArrayList<>();
        /* total data set with all info */
        final ArrayList<FacebookUser> userFriends = new ArrayList<>();
        /* our parameter since we are returning the friends */
        final String PARAM = "/friends";
        final String TYPE = "?fields=id,name,picture&type=square";
        /* combo for the full URL */
        String fullURL = USER_ID + PARAM + TYPE;
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
                            for(int i = 0; i < friendData.length(); i++) {
                                /* Each friend is in a JSON object  */
                                JSONObject aFriend = friendData.getJSONObject(i);
                                String Username = aFriend.getString("name");
                                String UserID = aFriend.getString("id");
                                /* try parsing the profile pic url here */
                                JSONObject picData = aFriend.getJSONObject("picture").getJSONObject("data");
                                String url = picData.getString("url");
                                FacebookUser single = new FacebookUser(Username, UserID, url);
                                friendIds.add(UserID);
                                userFriends.add(single);
                            }
                            callback.notifyCompleteList(userFriends, friendIds);
                        } catch (JSONException e) {
                            Log.d(tag, "Something went wrong in our facbeook query :(");
                            e.printStackTrace();
                        }
                    }
                });
        request.executeAsync();
    }

    /**
     * Simple container class that holds necessary friend information. Needs to be expanded once we decide
     * to bring in more parse user information in conjunction with the facebook information.
     */
    public class FacebookUser {
        public String username;
        public String userID;
        public String userProfilePicUrl;
        public String userLevel;

        public FacebookUser(String Username, String ID, String profileURL){
            this.username = Username;
            this.userID = ID;
            this.userProfilePicUrl = profileURL;
            /* placeholder value so we know that something is wrong without crashing the thing */
            this.userLevel = "-1";
        }

        /* passed in later vias connectivity to the parse user */
        public void setUserLevel(int parseUserLevel) {
            userLevel = String.valueOf(parseUserLevel);
        }

    }
}
