package com.l3.one_up;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.l3.one_up.model.Activity;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;


public class ActivitySelectionFragment extends Fragment {
    public String tag = "ActivitySelectionFragment";
    /* set up views and recycler */
    public RecyclerView rvActivityView;
    /* our data set */
    private ArrayList<Activity> myActivities;
    /* our "context" */
    private FragmentActivity fragAct;
    /* adapter */
    private ActivityItemAdapter itemAdapter;
    /* our command string that tells us which activities to load */
    public String category;

    private static String KEY_CATEGORY = "keyCategory";

    public ActivitySelectionFragment() {
        // Required empty public constructor
    }

    public static ActivitySelectionFragment newInstance(String categoryName) {

        Bundle args = new Bundle();
        args.putString(KEY_CATEGORY, categoryName);
        ActivitySelectionFragment fragment = new ActivitySelectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(tag, "In our activity selection fragment");

        Bundle args = getArguments();
        category = args.getString(KEY_CATEGORY);

        /* set up our context */
        fragAct = (FragmentActivity) getActivity();
        /* set up recycler view */
        rvActivityView = fragAct.findViewById(R.id.rvActivityView);
        /* init data set */
        myActivities = new ArrayList<>();
        /* init adapter */
        itemAdapter = new ActivityItemAdapter(myActivities);
        rvActivityView.setLayoutManager(new LinearLayoutManager(fragAct));
        /* set up adapter */
        rvActivityView.setAdapter(itemAdapter);
        /* populate with activities */
        loadActivities(category);
    }

    private void loadActivities(String category) {
        final Activity.Query activityQuery = new Activity.Query();
        /* get specified category activities */
        activityQuery.getCategoryAct(category);
        activityQuery.findInBackground(new FindCallback<Activity>() {
            @Override
            public void done(List<Activity> objects, ParseException e) {
                if(e == null){
                    Log.d(tag, "Activities loaded successfully");
                    Log.d(tag, "Objects list size: " + objects.size());
                    for(int i = 0; i < objects.size(); i++){
                        myActivities.add(objects.get(i));
                        Log.d(tag, "Name: " + myActivities.get(i).getName());
                        itemAdapter.notifyItemInserted(myActivities.size() - 1);
                    }
                }
                else{
                    Log.d(tag, "Failed to load activies :(");
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity_selection, container, false);
    }

}
