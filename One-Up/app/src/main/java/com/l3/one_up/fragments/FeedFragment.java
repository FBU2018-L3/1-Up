package com.l3.one_up.fragments;

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
import android.widget.Toast;

import com.l3.one_up.R;
import com.l3.one_up.adapters.FeedItemAdapter;
import com.l3.one_up.model.Event;
import com.l3.one_up.model.User;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {
    public String tag = "FeedFragment";
    /* oiur recycler view */
    RecyclerView rvFeed;
    /* our data set */
    ArrayList<Event> recentEvents;
    /* oue adapter */
    FeedItemAdapter feedItemAdapter;
    /* our "context" */
    FragmentActivity fragAct;
    /* key for retrieving our flag */
    String KEY_FLAG = "isTimeline";
    /* boolean flag for telling us whether we are displaying to feed or timeline */
    boolean isTimline;

    public FeedFragment() {
        // Required empty public constructor
    }

    /* instantiate bundle things here, pass in flag */
    public static FeedFragment newInstance(boolean isTimeline) {
        Bundle args = new Bundle();
        FeedFragment fragment = new FeedFragment();
        args.putBoolean("isTimeline", isTimeline);
        fragment.setArguments(args);
        return fragment;
    }
    
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(tag, "In our feed fragment");
        /* get pur flag which will dictate how we initialize */
        isTimline = getArguments().getBoolean(KEY_FLAG);
        /* set up our context */
        fragAct = (FragmentActivity) getActivity();
        /* set up recycler view */
        rvFeed = fragAct.findViewById(R.id.rvEventView);
        rvFeed.setLayoutManager(new LinearLayoutManager(fragAct));
        /* init data set */
        recentEvents = new ArrayList<>();
        /* set up adapter */
        feedItemAdapter = new FeedItemAdapter(recentEvents);
        /* set as adapter and more! */
        rvFeed.setAdapter(feedItemAdapter);
        /* call functions to populate  our feed/timeline */
        loadEvents();
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    public void loadEvents(){
        final Event.Query eventQuery = new Event.Query();
        /* if else statements to check which screen we are populating */
        if(this.isTimline){
            eventQuery.includeActivity().byUser(User.getCurrentUser()).mostRecentFirst();
        }
        else if(this.isTimline == false){
            eventQuery.includeActivity().byUser(User.getCurrentUser()).mostRecentFirst().onlyThisWeek();
        }
        else{
            Log.d(tag, "Is timeline variable is ever initialized");
            return;
        }

        eventQuery.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> objects, ParseException e) {
                if(e == null){
                    Log.d(tag, "This is the size: " + objects.size());
                    Toast.makeText(fragAct, "Got events :)", Toast.LENGTH_LONG).show();
                    for(int i = 0; i < objects.size(); i++){
                        Event myEvent = objects.get(i);
                        recentEvents.add(myEvent);
                        feedItemAdapter.notifyItemInserted(recentEvents.size() - 1);
                    }
                }
                else{
                    Log.d(tag, "Failed to get events :'(");
                    Toast.makeText(fragAct, "Got no event :(", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }
}
