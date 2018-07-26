package com.l3.one_up.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.l3.one_up.R;
import com.l3.one_up.model.FacebookQuery;
import com.l3.one_up.model.GlideApp;
import com.l3.one_up.model.GlideAppModule;

import org.w3c.dom.Text;

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
        String friendName = oneFriend.Username;
        String friendLevel = oneFriend.UserLevel;
        Log.d(tag , friendName + " has level " + friendLevel);
        String friendProfUrl = oneFriend.UserProfilePicUrl;

        holder.tvFriendName.setText(friendName);
        holder.tvFriendLevel.setText(friendLevel);

        // TODO: Use glide to upload the profile picture
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

        public ViewHolder(View itemView) {
            super(itemView);
            tvFriendName = itemView.findViewById(R.id.tvFriendName);
            tvFriendLevel = itemView.findViewById(R.id.tvFriendLevel);
            ivFriendProfilePic = itemView.findViewById(R.id.ivFriendProfilePic);
        }
    }
}
