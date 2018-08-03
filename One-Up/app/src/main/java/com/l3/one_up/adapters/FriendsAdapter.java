package com.l3.one_up.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.l3.one_up.R;
import com.l3.one_up.model.FacebookQuery;
import com.l3.one_up.services.GlideApp;

import java.util.ArrayList;

/**
 * Created by luzcamacho on 7/25/18.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {
    private static String tag = "FriendsAdapter";
    public ArrayList<FacebookQuery.FacebookUser> facebookFriends;
    Context context;

    public FriendsAdapter(ArrayList<FacebookQuery.FacebookUser> facebookFriends) {
        this.facebookFriends = facebookFriends;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View friendViewer = inflater.inflate(R.layout.item_friend, parent, false);
        FriendsAdapter.ViewHolder viewHolder = new FriendsAdapter.ViewHolder(friendViewer);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FacebookQuery.FacebookUser oneFriend = facebookFriends.get(position);
        String friendName = oneFriend.username;
        String friendLevel = oneFriend.userLevel;
        String friendProfUrl = oneFriend.userProfilePicUrl;
        String friendParseName = oneFriend.parseUsername;
        friendParseName = "AKA " + friendParseName;

        holder.tvFriendName.setText(friendName);
        holder.tvFriendLevel.setText(friendLevel);
        holder.tvFriendParseName.setText(friendParseName);

        GlideApp.with(context)
                .load(friendProfUrl)
                .into(holder.ivFriendProfilePic);
    }

    @Override
    public int getItemCount() {
        return facebookFriends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        /* init our views */
        public TextView tvFriendName;
        public TextView tvFriendLevel;
        public ImageView ivFriendProfilePic;
        public TextView tvFriendParseName;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFriendName = itemView.findViewById(R.id.tvFriendName);
            tvFriendLevel = itemView.findViewById(R.id.tvFriendLevel);
            ivFriendProfilePic = itemView.findViewById(R.id.ivFriendProfilePic);
            tvFriendParseName = itemView.findViewById(R.id.tvFriendParseName);
        }
    }
}
