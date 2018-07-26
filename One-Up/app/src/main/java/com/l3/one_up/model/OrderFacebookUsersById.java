package com.l3.one_up.model;

import java.math.BigInteger;
import java.util.Comparator;

/**
 * Created by luzcamacho on 7/26/18.
 */

public class OrderFacebookUsersById implements Comparator<FacebookQuery.FacebookUser> {
    @Override
    public int compare(FacebookQuery.FacebookUser t1, FacebookQuery.FacebookUser t2) {
        return Long.valueOf(t1.UserID).compareTo(Long.valueOf(t2.UserID));
    }
}
