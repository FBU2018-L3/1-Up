package com.l3.one_up.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import com.l3.one_up.R;
import com.l3.one_up.interfaces.CalendarCallback;

import java.util.GregorianCalendar;

public class CalendarFragment extends DialogFragment {

    public CalendarView cvTimeline;
    public Button btnRemoveFilter;
    public CalendarCallback calendarCallback;

    public CalendarFragment() {
        // Required empty public constructor
    }

    public static CalendarFragment newInstance(CalendarCallback calendarCallback) {
        CalendarFragment fragment = new CalendarFragment();
        fragment.calendarCallback = calendarCallback;
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
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onCreate(savedInstanceState);
        // Finding items
        cvTimeline = view.findViewById(R.id.cvTimeline);
        btnRemoveFilter = view.findViewById(R.id.btnRemoveFilter);

        // Assigning listeners
        cvTimeline.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                onDayChange(year, month, dayOfMonth);
                CalendarFragment.this.dismiss();
            }
        });
        btnRemoveFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarCallback.onDateCancelled();
                CalendarFragment.this.dismiss();
            }
        });

    }

    private void onDayChange(int y, int m, int d) {
        Log.d("CalendarFragment", ("Selected day is " + m + "/" + d + "/" + y));
        calendarCallback.onDateClicked(y,m,d);
    }

}
