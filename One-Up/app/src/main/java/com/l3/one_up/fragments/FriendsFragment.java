package com.l3.one_up.fragments;

import android.content.Context;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.l3.one_up.R;
import com.l3.one_up.adapters.FeedItemAdapter;
import com.l3.one_up.adapters.FriendsAdapter;
import com.l3.one_up.interfaces.FacebookCallComplete;
import com.l3.one_up.model.FacebookQuery;

import java.util.ArrayList;

public class FriendsFragment extends Fragment implements FacebookCallComplete {
    final static String tag = "FriendsFragment";
    /* our "context" */
    FragmentActivity fragAct;
    /* our data set */
    ArrayList<FacebookQuery.FacebookUser> friendsList;
    /* our recycler view */
    private RecyclerView rvFriendList;
    /* our adapter */
    private FriendsAdapter friendsAdapter;

    public FriendsFragment() {
        // Required empty public constructor
    }

    public static FriendsFragment newInstance() {
        
        Bundle args = new Bundle();
        
        FriendsFragment fragment = new FriendsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(tag, "In our friends fragment");
        /* set up our context */
        fragAct = (FragmentActivity) getActivity();
        /* set up recycler view */
        rvFriendList = fragAct.findViewById(R.id.rvFriendList);
        /* Set up our layout Manager to be cool */
        GridLayoutManager coolLayout = new GridLayoutManager(fragAct, 1, GridLayoutManager.HORIZONTAL, false);
        rvFriendList.setLayoutManager(coolLayout);
        /* init data set */
        friendsList = new ArrayList<>();
        /* set as adapter */
        friendsAdapter = new FriendsAdapter(friendsList);
        rvFriendList.setAdapter(friendsAdapter);
        /* Time to do our query and then update the adapter in the callbacks */
        FacebookQuery query = new FacebookQuery();
        query.getFriends(this);
    }

    /* NOTE: KEEP ALL DATA PROCESSING WITHIN THE CALLBACKS */
    @Override
    public ArrayList<FacebookQuery.FacebookUser> notifyCompleteList(ArrayList<FacebookQuery.FacebookUser> list, ArrayList<String> friendIds) {
        for(int i = 0; i < list.size(); i++){
            FacebookQuery.FacebookUser user = list.get(i);
        }
        friendsList.clear();
        friendsList.addAll(list);
        friendsAdapter.notifyDataSetChanged();
        return list;
    }
}
