package com.l3.one_up.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.l3.one_up.R;

public class StatsFragment extends Fragment
        implements GoalSummaryFragment.OnFragmentInteractionListener,
                    CategorySelectionFragment.OnCategorySelectedListener {

    private Button btnNewGoal;

    public StatsFragment() {
        // Required empty public constructor
    }

    public static StatsFragment newInstance() {
        StatsFragment fragment = new StatsFragment();
        Bundle args = new Bundle();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        GoalSummaryFragment goalSummaryFragment = GoalSummaryFragment.newInstance();
        startFragment(goalSummaryFragment);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onNewGoalClicked() {
        Log.d("StatsFragment", "Creating new goal");
        CategorySelectionFragment goalsCategorySelectionFragment = CategorySelectionFragment.newInstance();
        startFragment(goalsCategorySelectionFragment);
    }

    private void startFragment(Fragment fragment) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.goalsContainer, fragment);
        ft.addToBackStack(fragment.getClass().getSimpleName()).commit();
    }

    @Override
    public void onCategoryClick(String categoryName) {
        Toast.makeText(this.getContext(), "StatsFragment: category was clicked", Toast.LENGTH_SHORT);
        ActivitySelectionFragment activitySelectionFragment = ActivitySelectionFragment.newInstance(categoryName);
        startFragment(activitySelectionFragment);
    }
}
