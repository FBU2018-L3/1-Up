package com.l3.one_up.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by luzcamacho on 7/27/18.
 */

@ParseClassName("PowerUp")
public class PowerUp extends ParseObject {
    private final static String KEY_SENTBY_USER = "sentBy";
    private final static String KEY_SENTTO_USER = "sentTo";
    private final static String KEY_MESSAGE = "message";
    private final static String KEY_XP = "bonusXP";
    private final static String KEY_REDEEM = "isRedeemed";

    public ParseUser getSentByUser() {
        return getParseUser(KEY_SENTBY_USER);
    }

    public void setSentByUser(ParseUser user){
        put(KEY_SENTBY_USER, user);
    }

    public ParseUser getSentToUser() {
        return getParseUser(KEY_SENTTO_USER);
    }

    public void setSentToUser(ParseUser user) {
        put(KEY_SENTTO_USER, user);
    }

    public String getMessage() {
        return getString(KEY_MESSAGE);
    }

    public void setMessage(String message) {
        put(KEY_MESSAGE, message);
    }

    public int getBonusXP() {
        return getInt(KEY_XP);
    }

    public void setXP(int XP) {
        put(KEY_XP, XP);
    }

    public boolean getIsRedeemed() {
        return getBoolean(KEY_REDEEM);
    }

    public void setIsRedeemed(boolean isRedeemed){
        put(KEY_REDEEM, isRedeemed);
    }

    public static class Query extends ParseQuery<PowerUp>
    {

        public Query() { super(PowerUp.class); }

        public Query getAllRecievedPowerUps(ParseUser user) {
            whereEqualTo(KEY_SENTTO_USER, user);
            return this;
        }

        public Query getAllUnredeemed() {
            whereEqualTo(KEY_REDEEM, false);
            return this;
        }

        public Query getAllSentPowerUps(ParseUser user){
            whereEqualTo(KEY_SENTBY_USER, user);
            return this;
        }
    }
}
