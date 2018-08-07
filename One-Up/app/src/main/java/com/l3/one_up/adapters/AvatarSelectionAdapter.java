package com.l3.one_up.adapters;

import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.l3.one_up.R;
import com.l3.one_up.model.User;
import com.parse.ParseException;
import com.parse.SaveCallback;

public class AvatarSelectionAdapter extends RecyclerView.Adapter<AvatarSelectionAdapter.ViewHolder> {

    TypedArray avatarIds;
    OnAvatarSelectListener mListener;

    public AvatarSelectionAdapter(TypedArray IDs, OnAvatarSelectListener listener) {
        this.avatarIds = IDs;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View activityViewer = inflater.inflate(R.layout.item_avatar, parent, false);
        ViewHolder viewHolder = new ViewHolder(activityViewer);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int placeholder = R.mipmap.ic_launcher;
        final int index = position;
        holder.ivAvatarItem.setImageResource(avatarIds.getResourceId(position, placeholder));
        holder.ivAvatarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAvatarSelect(index);
            }
        });
    }

    @Override
    public int getItemCount() {
        return avatarIds.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAvatarItem;

        public ViewHolder(View itemView) {
            super(itemView);

            ivAvatarItem = itemView.findViewById(R.id.ivAvatarItem);
        }
    }

    public void onAvatarSelect(final int position) {
        Log.d("AvatarSelection", "text was clicked");
        User user = User.getCurrentUser();
        user.setAvatar(position);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.d("AvatarSelection", "done updating user's avatar in Parse");
                if (mListener != null) {
                    mListener.onAvatarClicked(position);
                }
            }
        });
    }

    public interface OnAvatarSelectListener {
        void onAvatarClicked(int position);
    }
}
