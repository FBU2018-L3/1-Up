package com.l3.one_up.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.l3.one_up.R;

import java.util.GregorianCalendar;

public class CalendarFragment extends Fragment {

    public CalendarView cvTimeline;
    public FeedFragment timelineFeed;

    public CalendarFragment() {
        // Required empty public constructor
    }

    public static CalendarFragment newInstance() {
        CalendarFragment fragment = new CalendarFragment();
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
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onCreate(savedInstanceState);
        cvTimeline = (CalendarView) getActivity().findViewById(R.id.cvTimeline);
        cvTimeline.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                onDayChange(view, year, month, dayOfMonth);
            }
        });

        boolean isTimeline = true; // for clarity's sake
        timelineFeed = FeedFragment.newInstance(isTimeline);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.timelineHolder, timelineFeed);
        ft.commit();
    }

    private void onDayChange(CalendarView v, int y, int m, int d) {
        Log.d("CalendarFragment", ("Selected day is " + m + "/" + d + "/" + y));
        v.setDate(new GregorianCalendar(y, m, d).getTimeInMillis());
        timelineFeed.setDate(y, m, d);
    }
}
