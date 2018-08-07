package com.l3.one_up.services;

import android.content.Context;
import android.content.res.TypedArray;

import com.l3.one_up.R;
import com.l3.one_up.model.User;

public class AvatarFinder {

    public int getAvatarId(Context context) {
        TypedArray avatarIDs = context.getResources().obtainTypedArray(R.array.avatar_id);
        User user = User.getCurrentUser();
        int index = user.getAvatar();
        int placeholderID = R.mipmap.ic_launcher;
        return avatarIDs.getResourceId(index, placeholderID);
    }
}
