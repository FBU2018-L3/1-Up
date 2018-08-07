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
import com.l3.one_up.model.Goal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GoalItemAdapter extends RecyclerView.Adapter<GoalItemAdapter.ViewHolder> {
    private static String tag = "GoalItemAdapter";

    private ArrayList<Goal> goals;

    private Context context;

    public GoalItemAdapter(ArrayList<Goal> goalList) {
        super();
        this.goals = goalList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View activityViewer = inflater.inflate(R.layout.item_goal, parent, false);
        ViewHolder viewHolder = new ViewHolder(activityViewer);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Goal myGoal = goals.get(position);
        Log.d(tag, "Displaying stats");
        String goalName = myGoal.getActivity().getName();
        Log.d(tag, "Current goal for activity: "+ goalName);
        JSONObject keys = myGoal.getInputType();
        for(int i = 0; i < keys.names().length(); i++){
            try {
                Log.d(tag, "input type: " + keys.names().getString(i) + " value: " + keys.get(keys.names().getString(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        JSONObject progress = myGoal.getProgress();
        for(int i = 0; i < progress.names().length(); i++){
            try {
                Log.d(tag, "Progress input type: " + progress.names().getString(i) + " value: " + progress.get(progress.names().getString(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d(tag, "Is the goal complete: " + myGoal.getIsCompleted());
        holder.tvGoalId.setText(myGoal.getObjectId());
        holder.tvGoalName.setText(myGoal.getActivity().getName());
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvGoalId;
        public TextView tvGoalName;

        public ViewHolder(View itemView) {
            super(itemView);

            tvGoalId = (TextView) itemView.findViewById(R.id.tvGoalID);
            tvGoalName = (TextView) itemView.findViewById(R.id.tvGoalName);
        }
    }
}
