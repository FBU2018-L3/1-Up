package com.l3.one_up.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.l3.one_up.R;
import com.l3.one_up.model.Activity;

import java.util.ArrayList;

/**
 * Created by luzcamacho on 7/16/18.
 */

public class  ActivityItemAdapter extends RecyclerView.Adapter<ActivityItemAdapter.ViewHolder> {
    private String tag = "ActivityItemAdapter";
    private static ArrayList<Activity> categoryActivities;
    static Context context;
    private Callback callback;

    /* contructor that takes in our data set */
    public ActivityItemAdapter(ArrayList<Activity> categoryActivities, Callback callback) {
        this.categoryActivities = categoryActivities;
        this.callback = callback;
    }


    @NonNull
    @Override
    public ActivityItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        /* wire up our view */
        View activityViewer = inflater.inflate(R.layout.item_activity, parent, false);
        ViewHolder viewHolder = new ViewHolder(activityViewer);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityItemAdapter.ViewHolder holder, int position) {
        Activity myActivity = categoryActivities.get(position);
        /* set up in holder */
        String name = myActivity.getName();
        Log.d(tag, "Printing name: " + name);
        Integer baseEXP = myActivity.getBaseXP();

        holder.activityName.setText(name);
        holder.activityEXP.setText(baseEXP.toString());
    }

    @Override
    public int getItemCount() {
        return categoryActivities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /* viewholder thingssss */
        public TextView activityName;
        public TextView activityEXP;

        public ViewHolder(View itemView) {
            super(itemView);
            activityName = itemView.findViewById(R.id.tvActivityName);
            activityEXP = itemView.findViewById(R.id.tvBaseExp);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION){
                Activity myActivity = categoryActivities.get(position);
                callback.passActivity(myActivity);
            }
        }
    }

    public interface Callback {
        void passActivity(Activity activity);
    }
}
