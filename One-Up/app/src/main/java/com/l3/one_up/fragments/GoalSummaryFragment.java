package com.l3.one_up.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.l3.one_up.R;
import com.l3.one_up.adapters.GoalItemAdapter;
import com.l3.one_up.model.Goal;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class GoalSummaryFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private Button btnNewGoal;

    private RecyclerView rvGoals;
    private GoalItemAdapter goalAdapter;
    private ArrayList<Goal> goals;

    public GoalSummaryFragment() {
        // Required empty public constructor
    }

    public static GoalSummaryFragment newInstance() {
        GoalSummaryFragment fragment = new GoalSummaryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_goal_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnNewGoal = getActivity().findViewById(R.id.btnNewGoal);
        btnNewGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed();
            }
        });

        goals = new ArrayList<Goal>();
        goalAdapter = new GoalItemAdapter(goals);
        rvGoals = (RecyclerView) getActivity().findViewById(R.id.rvGoals);
        rvGoals.setLayoutManager(new LinearLayoutManager(getContext()));
        rvGoals.setAdapter(goalAdapter);

        getActiveGoals();
    }

    public void getActiveGoals() {
        Goal.Query activeGoalQuery = new Goal.Query();
        activeGoalQuery.byUser(ParseUser.getCurrentUser())
                .includeActivity()
                .mostRecentFirst()
                .onlyThisWeek();
        activeGoalQuery.findInBackground(new FindCallback<Goal>() {
            @Override
            public void done(List<Goal> objects, ParseException e) {
                if (e == null) {
                    Log.d("GoalSummaryFragment", objects.toString());
                    for (int i = 0; i < objects.size(); i++) {
                        goals.add(objects.get(i));
                        goalAdapter.notifyItemInserted(goals.size() - 1);
                    }
                } else {
                    Log.d("GoalSummaryFragment", "Failed to retrieve goals");
                    e.printStackTrace();
                }
            }
        });
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onNewGoalClicked();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) getParentFragment();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onNewGoalClicked();
    }
}
