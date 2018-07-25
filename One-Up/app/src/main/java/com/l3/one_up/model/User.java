package com.l3.one_up.model;

import android.os.Bundle;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.Map;

import bolts.Task;

@ParseClassName("_User")
public class User extends ParseUser {

    private static final String KEY_EXPERIENCE_POINTS = "experiencePoints";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_IS_ASLEEP = "isAsleep";
    private static final String KEY_AVATAR = "avatar";
    private static final String KEY_FACEBOOK_ID = "facebookId";


    public ParseFile getAvatar(){ return getParseFile(KEY_AVATAR); }
    public int getExperiencePoints(){ return getInt(KEY_EXPERIENCE_POINTS); }
    public int getLevel(){ return getInt(KEY_LEVEL); }
    public boolean isAsleep(){ return getBoolean(KEY_IS_ASLEEP); }

    /* Luz: added new setters and getters for facebook id in order to match parse user w/ fb user */
    public String getFacebookId() { return getString(KEY_FACEBOOK_ID); }

    public void setFacebookId(String facebookId) {
        put(KEY_FACEBOOK_ID, facebookId);
    }

    /**
     * This function updates the xp points of the user and automatically submits it to the database
     * @param xpToAdd The xp that are going to be added to the user
     * @return the initial xp the user had
     */
    public int updateExperiencePoints(int xpToAdd){
        return updateExperiencePoints(xpToAdd, null);
    }

    /**
     * This function updates the xp points of the user and automatically submits it to the database
     * @param xpToAdd The xp that are going to be added to the user
     * @param saveCallback The callback for the saveInBackground function
     * @return the initial xp the user had
     */
    public int updateExperiencePoints(int xpToAdd, SaveCallback saveCallback){
        int startXp = this.getExperiencePoints();
        put(KEY_EXPERIENCE_POINTS, startXp + xpToAdd);
        this.setLevel();
        if(saveCallback==null)
            saveInBackground();
        else
            saveInBackground(saveCallback);
        return startXp;
    }

    private void setLevel(){
        put(KEY_LEVEL, this.getExperiencePoints()/100);
    }

    public int getCurrentXpFromLevel(){
        return this.getExperiencePoints()%100;
    }

    public int getNeededXpToLevelUp() { return 100; }

    public static User getCurrentUser(){
        return (User)ParseUser.getCurrentUser();
    }



}
