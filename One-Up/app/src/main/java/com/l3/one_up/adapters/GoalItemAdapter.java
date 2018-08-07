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
import org.w3c.dom.Text;

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
        String goalName = myGoal.getActivity().getName();
        Log.d(tag, "Current goal for activity: "+ goalName);
        JSONObject keys = myGoal.getInputType();
        String goalInputType = "";
        String ultimateGoal = "";
        for(int i = 0; i < keys.names().length(); i++){
            try {
                goalInputType = keys.names().getString(i);
                ultimateGoal = keys.getString(keys.names().getString(i));
                Log.d(tag, "input type: " + goalInputType + " value: " + ultimateGoal);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String currentProgress = "";
        JSONObject progress = myGoal.getProgress();
        for(int i = 0; i < progress.names().length(); i++){
            try {
                currentProgress = progress.getString(progress.names().getString(i));
                Log.d(tag, "Progress input type: " + progress.names().getString(i) + " value: " + currentProgress);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        boolean isComplete = myGoal.getIsCompleted();
        Log.d(tag, "Is the goal complete: " + isComplete);
        /* Display the data accordingly */
        // first completion status
        if(isComplete) holder.tvIsComplete.setText("Complete");
        else holder.tvIsComplete.setText("In Progress");
        // display activity name
        holder.tvGoalName.setText(myGoal.getActivity().getName());
        // display current progress versus ultimate progress
        holder.tvCurrGoalProgress.setText("Current Progress: " + currentProgress + " " + goalInputType);
        holder.tvUltimateGoal.setText("Goal: " + ultimateGoal + " " + goalInputType);
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvGoalName;
        public TextView tvCurrGoalProgress;
        public TextView tvIsComplete;
        public TextView tvUltimateGoal;

        public ViewHolder(View itemView) {
            super(itemView);

            tvGoalName = (TextView) itemView.findViewById(R.id.tvGoalName);
            tvCurrGoalProgress = (TextView) itemView.findViewById(R.id.tvCurrentGoalProgress);
            tvIsComplete = (TextView) itemView.findViewById(R.id.tvisGoalComplete);
            tvUltimateGoal = (TextView) itemView.findViewById(R.id.tvUltimateGoal);
        }
    }
}
