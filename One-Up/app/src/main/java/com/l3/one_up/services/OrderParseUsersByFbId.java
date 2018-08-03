package com.l3.one_up.services;

import android.support.annotation.NonNull;

import com.l3.one_up.model.User;

import java.util.Comparator;

/**
 * Created by luzcamacho on 7/26/18.
 */

public class OrderParseUsersByFbId implements Comparator<User> {
    @Override
    public int compare(User UserA, User UserB){
        return Long.valueOf(UserA.getFacebookId()).compareTo(Long.valueOf(UserB.getFacebookId()));
    }
}
