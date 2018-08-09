package com.l3.one_up.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.l3.one_up.R;
import com.l3.one_up.model.Activity;
import com.l3.one_up.model.Event;
import com.l3.one_up.services.ParseRelativeDate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by luzcamacho on 7/19/18.
 */

public class FeedItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "FeedItemAdapter";
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;

    private ArrayList<Event> recentEvents;
    private Context context;

    private boolean isCurrentUser;
    private boolean hasFooter;

    private String footerText;
    private String btnText;
    private View.OnClickListener footerOnClickListener;


    private FeedItemAdapter(ArrayList<Event> recentEvents, boolean isCurrentUser, boolean hasFooter){
        this.recentEvents = recentEvents;
        this.isCurrentUser = isCurrentUser;
        this.hasFooter = hasFooter;
        if(!hasFooter){
            footerText=null;
            footerOnClickListener=null;
        }
    }

    public FeedItemAdapter(ArrayList<Event> recentEvents, boolean isCurrentUser){
        this(recentEvents, isCurrentUser, false);
    }

    public FeedItemAdapter(ArrayList<Event> recentEvents, boolean isCurrentUser, String footerText, String btnText, View.OnClickListener footerOnClickListener){
        this(recentEvents, isCurrentUser, true);
        this.footerText=footerText;
        this.footerOnClickListener=footerOnClickListener;
        this.btnText = btnText;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType == TYPE_ITEM){
            View eventViewer = inflater.inflate(R.layout.item_event, parent, false);
            ViewHolder viewHolder = new ViewHolder(eventViewer);
            return viewHolder;
        }
        else if(viewType == TYPE_FOOTER){
            View eventViewer = inflater.inflate(R.layout.recyclerview_footer, parent, false);
            FooterViewHolder viewHolder = new FooterViewHolder(eventViewer);
            return viewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof ViewHolder && position < recentEvents.size()) {
            Event event = recentEvents.get(position);
            /* parse out the activity, draw its name */
            Activity activity = event.getActivity();
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.tvEventName.setText(activity.getName());
            /* get the parse input object */
            JSONObject inputTypes = event.getInputType();
            Iterator iter = inputTypes.keys();
            String key = "";
            String value = "";
            while (iter.hasNext()) {
                key = iter.next().toString();
                Log.d(TAG, key);
                try {
                    value = String.valueOf(inputTypes.getInt(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, value);
            }
            String start = isCurrentUser ? "You" : "Your friend";

            holder.tvEvent.setText(String.format("%s did %s %s", start, value, key));
            /* set date and exp points gained */
            String rawDate = event.getCreatedAt().toString();
            ParseRelativeDate thingy = new ParseRelativeDate();
            String relativeDate = thingy.getRelativeTimeAgo(rawDate);
            Log.d(TAG, "Time?: " + relativeDate);
            holder.tvEventDate.setText(relativeDate);
            if (isCurrentUser) {
                holder.tvEventEXP.setText(String.valueOf(String.format("You won %s xp", event.getTotalXP())));
            } else {
                holder.tvEventEXP.setVisibility(View.GONE);
            }
        }
        else if(viewHolder instanceof  FooterViewHolder){
            FooterViewHolder holder = (FooterViewHolder) viewHolder;
            holder.footerText.setText(footerText);
            holder.btnFooterButton.setOnClickListener(footerOnClickListener);
            holder.btnFooterButton.setText(btnText);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == recentEvents.size()) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        if(hasFooter)
            return recentEvents.size() + 1;
        return recentEvents.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void clear() {
        recentEvents.clear();
        notifyDataSetChanged();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvEventName;
        public TextView tvEventEXP;
        public TextView tvEventDate;
        public TextView tvEvent;

        public ViewHolder(View itemView) {
            super(itemView);
            tvEventName = itemView.findViewById(R.id.tvEventName);
            tvEventEXP = itemView.findViewById(R.id.tvEventEXP);
            tvEventDate = itemView.findViewById(R.id.tvEventDate);
            tvEvent = itemView.findViewById(R.id.tvEvent);
        }

    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView footerText;
        Button btnFooterButton;

        public FooterViewHolder(View view) {
            super(view);
            footerText = view.findViewById(R.id.tvFooterText);
            btnFooterButton = view.findViewById(R.id.btnFooterButton);
        }
    }
}
