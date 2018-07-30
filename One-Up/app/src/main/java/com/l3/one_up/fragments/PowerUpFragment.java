package com.l3.one_up.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.l3.one_up.R;
import com.l3.one_up.adapters.FeedItemAdapter;
import com.l3.one_up.adapters.PowerUpAdapter;
import com.l3.one_up.model.PowerUp;

import java.util.ArrayList;

public class PowerUpFragment extends Fragment {
    private static String tag = "PowerUpFragment";
    /* our primary data set */
    ArrayList<PowerUp> userPowerUps;
    /* our "context" */
    FragmentActivity fragAct;
    /* our adapter */
    PowerUpAdapter powerUpAdapter;
    /* actual recycler view */
    RecyclerView rvPowerUpList;

    public PowerUpFragment() {
        // Required empty public constructor
    }

    public static PowerUpFragment newInstance(String param1, String param2) {
        PowerUpFragment fragment = new PowerUpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(tag, "In our Power Up fragment");
        /* do all work in here */
        userPowerUps = new ArrayList<>();
        /* set  up "context" */
        fragAct = (FragmentActivity) getActivity();
        /* set up recycler view */
        rvPowerUpList = fragAct.findViewById(R.id.rvPowerUpList);
        rvPowerUpList.setLayoutManager(new LinearLayoutManager(fragAct));
        /* set up adapter */
        powerUpAdapter = new PowerUpAdapter(userPowerUps);
        /* time to call functions to populate our power up feed */
        loadPowerUps();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_power_up, container, false);
    }

    public void loadPowerUps() {

    }
}
