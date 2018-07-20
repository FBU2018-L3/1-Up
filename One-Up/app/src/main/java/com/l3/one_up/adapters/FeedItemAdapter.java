package com.l3.one_up.adapters;

import android.content.Context;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.l3.one_up.R;
import com.l3.one_up.model.Activity;
import com.l3.one_up.model.Event;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

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
        Event event = recentEvents.get(position);
        /* parse out the activity, draw its name */
        Activity activity = event.getActivity();
        holder.tvEventName.setText(activity.getName());
        /* get the parse input object */
        JSONObject inputTypes = event.getInputType();
        Iterator iter = inputTypes.keys();
        String key = "";
        String value = "";
        while (iter.hasNext()) {
            key = iter.next().toString();
            Log.d(tag, key);
            try {
                value = String.valueOf(inputTypes.getInt(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(tag, value);
        }
        holder.tvEventNum.setText(value);
        holder.tvEventNumType.setText(key);
        /* set date and exp points gained */
        holder.tvEventDate.setText(event.getCreatedAt().toString());
        holder.tvEventEXP.setText(String.valueOf(event.getTotalXP()));
    }

    @Override
    public int getItemCount() {
        return recentEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvEventName;
        public TextView tvEventEXP;
        public TextView tvEventDate;
        public TextView tvEventNum;
        public TextView tvEventNumType;

        public ViewHolder(View itemView) {
            super(itemView);
            tvEventName = itemView.findViewById(R.id.tvEventName);
            tvEventEXP = itemView.findViewById(R.id.tvEventEXP);
            tvEventDate = itemView.findViewById(R.id.tvEventDate);
            tvEventNum = itemView.findViewById(R.id.tvEventNum);
            tvEventNumType = itemView.findViewById(R.id.tvEventNumType);
        }
    }
}
