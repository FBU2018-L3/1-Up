package com.l3.one_up.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.l3.one_up.R;
import com.l3.one_up.formatters.XAxisDateValueFormatter;
import com.l3.one_up.model.Activity;
import com.l3.one_up.model.Event;
import com.l3.one_up.model.User;
import com.parse.FindCallback;
import com.parse.ParseException;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ChartFragment extends Fragment{

    public final static int LINE_CHART = 0;
    public final static int BAR_CHART = 1;

    private Activity activity;
    private Unbinder unbinder;
    private Chart<?> chart;
    private int chartType;
    private List<BarEntry> entries = new ArrayList<>();
    private ArrayList<Date> dates = new ArrayList<>();
    @BindView(R.id.spInputType) Spinner spInputType;
    @BindView(R.id.rlChartFragment) RelativeLayout rlChartFragment;

    public static ChartFragment newInstance(Activity activity, int chartType){
        ChartFragment newChart = new ChartFragment();
        newChart.activity = activity;
        newChart.chartType = chartType;
        return newChart;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switch(chartType){
            case LINE_CHART:
                chart = new LineChart(getContext());
                break;
            case BAR_CHART:
                chart = new BarChart(getContext());
                break;
        }

        RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.BELOW, R.id.spInputType);
        rlChartFragment.addView(chart, 1, params);

        final List<String> spinnerArray =  new ArrayList<>();
        for(int i =0; i< activity.getInputType().names().length(); i++)
        {
            try {
                spinnerArray.add(activity.getInputType().names().getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(activity.getInputType().length()==1){
            spInputType.setBackground(null);
            spInputType.setEnabled(false);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInputType.setAdapter(adapter);


        configureChart();

        spInputType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String item = spinnerArray.get(position);
                Event.Query query = new Event.Query();
                query.oldestFirst()
                     .onlyThisWeek()
                     .byUser(User.getCurrentUser())
                     .ofActivity(activity)
                     .findInBackground(new FindCallback<Event>() {
                            @Override
                            public void done(List<Event> objects, ParseException e) {
                                if(e==null) {
                                    chart.clear();
                                    if (objects.size() > 0) {
                                        if(chart instanceof LineChart)
                                            ((LineChart)chart).setData(getLinearData(objects, item));
                                        else if(chart instanceof BarChart)
                                            ((BarChart)chart).setData(getBarData(objects, item));

                                    }
                                    else{
                                        Toast.makeText(getContext(),
                                                "Nothing found",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    Toast.makeText(getContext(),
                                            "There was a problem, please try again later",
                                            Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                     });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDetach() {
        unbinder.unbind();
        super.onDetach();
    }

    private LineData getLinearData(List<Event> events, String item){

        List<Entry> entries = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();

        for(int i = 0; i < events.size(); i++){
            Event event = events.get(i);
            if(event.getInputType().has(item)){
                try {
                    entries.add(new Entry(i + 1, event.getInputType().getInt(item)));
                    dates.add(event.getCreatedAt().toString());
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        }

        LineDataSet eventsDataSet = new LineDataSet(entries, activity.getName() + " in " + item);
        eventsDataSet.setLineWidth(2);
        eventsDataSet.setCircleSize(5);
        eventsDataSet.setFillColor(R.color.colorAccent);

        LineData lineData = new LineData(eventsDataSet);
        lineData.setValueTextSize(18);

        return lineData;
    }

    private BarData getBarData(List<Event> events, String item){
        LinkedHashMap<Date, Integer> map = new LinkedHashMap<>();


        for(int i = 0; i < events.size(); i++){
            Event event = events.get(i);
            if(event.getInputType().has(item)){
                try {

                    Integer value = event.getInputType().getInt(item);
                    Date date = parseDate(event.getCreatedAt().toString());

                    if(map.containsKey(date)){
                        map.put(date, map.get(date)+value);
                    }
                    else{
                        map.put(date, value);
                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        Iterator iterator = map.entrySet().iterator();

        for(int i = 0;iterator.hasNext(); i++){
            Map.Entry e = (Map.Entry) iterator.next();
            entries.add(new BarEntry(i, (Integer)e.getValue()));
            dates.add((Date)e.getKey());
            Log.d("CHARTS", e.getKey().toString() + ":  " + e.getValue().toString());
        }


        BarDataSet eventsDataSet = new BarDataSet(entries, activity.getName() + " in " + item);

        BarData barData = new BarData(eventsDataSet);
        barData.setValueTextSize(16);


        return barData;
    }

    private void configureChart(){
        chart.getDescription().setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        YAxis leftAxis = new YAxis();
        YAxis rightAxis = new YAxis();

        if(chart instanceof LineChart) {
            leftAxis = ((LineChart) chart).getAxisLeft();
            rightAxis = ((LineChart) chart).getAxisRight();
        }
        else if(chart instanceof BarChart){
            leftAxis = ((BarChart) chart).getAxisLeft();
            rightAxis = ((BarChart) chart).getAxisRight();

        }


        // configure axis
        xAxis.setValueFormatter(new XAxisDateValueFormatter(dates));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularityEnabled(true);
        xAxis.setTextSize(14);

        leftAxis.setAxisMinimum(0);
        leftAxis.setTextSize(16);
        rightAxis.setDrawAxisLine(false); // no axis line
        rightAxis.setDrawGridLines(false); // no grid lines
        rightAxis.setEnabled(false);

        // configuring chart
        //chart.setPinchZoom(true);
    }


    private Date parseDate(String dateString) throws java.text.ParseException {
        SimpleDateFormat parser=new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        Date date = parser.parse(dateString);
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        return date;
    }
}
