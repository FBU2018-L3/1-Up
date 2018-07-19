package com.l3.one_up.fragments;

import android.content.Context;
import android.net.Uri;
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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

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

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(tag, "In our feed fragment");
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
        /* call functions to populate  our feed */
        loadFeed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    public void loadFeed() {
        final Event.Query eventQuery = new Event.Query();
        eventQuery.byUser(ParseUser.getCurrentUser()).mostRecentFirst().onlyThisWeek();
        eventQuery.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> objects, ParseException e) {
                if(e == null){
                    Toast.makeText(fragAct, "Got events :)", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(fragAct, "Got no event :(", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
