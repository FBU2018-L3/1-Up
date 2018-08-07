package com.l3.one_up.adapters;

import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.l3.one_up.R;

public class AvatarSelectionAdapter extends RecyclerView.Adapter<AvatarSelectionAdapter.ViewHolder> {

    TypedArray avatarIds;

    public AvatarSelectionAdapter(TypedArray IDs) {
        this.avatarIds = IDs;
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
        holder.ivAvatarItem.setImageResource(avatarIds.getResourceId(position, placeholder));
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
}
