package com.l3.one_up.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.l3.one_up.R;
import com.l3.one_up.adapters.FeedItemAdapter;
import com.l3.one_up.interfaces.CalendarCallback;
import com.l3.one_up.model.Event;
import com.l3.one_up.model.User;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment implements CalendarCallback {

    public static final String TAG = "FeedFragment";
    RecyclerView rvFeed;
    ArrayList<Event> recentEvents;
    FeedItemAdapter feedItemAdapter;
    /* our "context" */
    FragmentActivity fragAct;
    /* key for retrieving our flag */
    private static final String KEY_IS_TIMELINE = "isTimeline";
    private static final String KEY_HAS_FOOTER = "hasFooter";
    private boolean isTimeline;
    private boolean hasFooter;
    private View.OnClickListener footerOnClickListener;

    Toolbar tbFeedBar;


    public FeedFragment() {
        // Required empty public constructor
    }

    /* instantiate bundle things here, pass in flag */
    public static FeedFragment newInstance(boolean isTimeline, boolean hasFooter, View.OnClickListener footerOnClickListener) {
        Bundle args = new Bundle();
        FeedFragment fragment = new FeedFragment();
        args.putBoolean(KEY_IS_TIMELINE, isTimeline);
        args.putBoolean(KEY_HAS_FOOTER, hasFooter);
        fragment.footerOnClickListener = footerOnClickListener;
        fragment.setArguments(args);
        return fragment;
    }

    public static FeedFragment newInstance(boolean isTimeline) {
        return newInstance(isTimeline, false, null);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "In our feed fragment");
        // obtaining flags
        isTimeline = getArguments().getBoolean(KEY_IS_TIMELINE);
        hasFooter = getArguments().getBoolean(KEY_HAS_FOOTER);
        // set up our context
        fragAct = (FragmentActivity) getActivity();
        // set up recycler view
        rvFeed = fragAct.findViewById(R.id.rvEventView);
        rvFeed.setLayoutManager(new LinearLayoutManager(fragAct));
        // init data set
        recentEvents = new ArrayList<>();
        // set up adapter
        if(hasFooter)
            feedItemAdapter = new FeedItemAdapter(recentEvents, true, "You've reached the end of the weekly feed!", "Go to timeline!", footerOnClickListener);
        else
            feedItemAdapter = new FeedItemAdapter(recentEvents, true);
        // set as adapter and more!
        rvFeed.setAdapter(feedItemAdapter);
        // call functions to populate  our feed/timeline
        loadEvents(-1, -1, -1);

        // configuring fab
        if(isTimeline)
        {
            FloatingActionButton fabCalendar = fragAct.findViewById(R.id.fabCalendar);
            fabCalendar.setVisibility(View.VISIBLE);
            fabCalendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager fm = getFragmentManager();
                    CalendarFragment calendarFragment = CalendarFragment.newInstance(FeedFragment.this);
                    calendarFragment.show(fm, "calendarFragment");
                }
            });
            tbFeedBar = getActivity().findViewById(R.id.tbProfileBar);
            ((AppCompatActivity)fragAct).setSupportActionBar(tbFeedBar);
            ((AppCompatActivity)fragAct).getSupportActionBar().setTitle("Your Timeline");
            tbFeedBar.setTitleTextColor(fragAct.getColor(android.R.color.white));

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    public void loadEvents(int year, int month, int day){
        final Event.Query eventQuery = new Event.Query();
        /* if else statements to check which screen we are populating */
        eventQuery.includeActivity().byUser(User.getCurrentUser()).mostRecentFirst();
        if (!this.isTimeline){
            eventQuery.onlyThisWeek();
        } else if (month != -1) {
            eventQuery.onlyOnDay(year, month, day);
        } else{
            Log.d(TAG, "Is timeline variable is never initialized");
            loadAllTimeline();
            return;
        }

        loadTimeline(eventQuery);
    }

    public void loadAllTimeline(){
        final Event.Query eventQuery = new Event.Query();
        eventQuery.includeActivity().byUser(User.getCurrentUser()).mostRecentFirst();
        loadTimeline(eventQuery);
    }

    private void loadTimeline(Event.Query query){
        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> objects, ParseException e) {
                if(e == null){
                    Log.d(TAG, "This is the size: " + objects.size());
                    feedItemAdapter.clear();
                    recentEvents.addAll(objects);
                    feedItemAdapter.notifyDataSetChanged();
                }
                else{
                    Log.d(TAG, "Failed to get events :'(");
                    Toast.makeText(fragAct, "Got no event :(", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

    }

    private void setDate(int year, int month, int day) {
        loadEvents(year, month, day);
    }

    @Override
    public void onDateClicked(int year, int month, int day) {
        ((AppCompatActivity)fragAct).getSupportActionBar().setTitle(String.format(getString(R.string.dateFormat),month,day,year));
        setDate(year, month, day);
    }

    @Override
    public void onDateCancelled() {
        ((AppCompatActivity)fragAct).getSupportActionBar().setTitle("Your timeline");
        setDate(-1,-1,-1);
    }
}
