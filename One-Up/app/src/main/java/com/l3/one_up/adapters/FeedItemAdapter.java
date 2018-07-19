package com.l3.one_up.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.l3.one_up.R;
import com.l3.one_up.model.Event;

import java.util.ArrayList;

/**
 * Created by luzcamacho on 7/19/18.
 */

public class FeedItemAdapter extends RecyclerView.Adapter<FeedItemAdapter.ViewHolder> {
    public String tag = "FeedItemAdapter";
    /* our data set */
    private ArrayList<Event> recentEvents;
    /* our context */
    Context context;

    public FeedItemAdapter(ArrayList<Event> recentEvents){
        this.recentEvents = recentEvents;
    }



    @NonNull
    @Override
    public FeedItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View eventViewer = inflater.inflate(R.layout.item_event, parent, false);
        ViewHolder viewHolder = new ViewHolder(eventViewer);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeedItemAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvEventName;
        public TextView tvEventEXP;
        public TextView tvEventDate;

        public ViewHolder(View itemView) {
            super(itemView);
            tvEventName = itemView.findViewById(R.id.tvEventName);
            tvEventEXP = itemView.findViewById(R.id.tvEventEXP);
            tvEventDate = itemView.findViewById(R.id.tvEventDate);
        }
    }
}
