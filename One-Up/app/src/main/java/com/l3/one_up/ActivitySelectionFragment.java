package com.l3.one_up;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.l3.one_up.fragments.InputFragment;
import com.l3.one_up.model.Activity;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;


public class ActivitySelectionFragment extends Fragment implements ActivityItemAdapter.Callback {
    public String tag = "ActivitySelectionFragment";
    /* set up views and recycler */
    public RecyclerView rvActivityView;
    /* our data set */
    private ArrayList<Activity> myActivities;
    /* our "context" */
    private FragmentActivity fragAct;
    /* adapter */
    private ActivityItemAdapter itemAdapter;
    /* our command string that tells us which activities to load */
    public String category;

    private static String KEY_CATEGORY = "keyCategory";

    public ActivitySelectionFragment() {
        // Required empty public constructor
    }

    public static ActivitySelectionFragment newInstance(String categoryName) {

        Bundle args = new Bundle();
        args.putString(KEY_CATEGORY, categoryName);
        ActivitySelectionFragment fragment = new ActivitySelectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(tag, "In our activity selection fragment");

        Bundle args = getArguments();
        category = args.getString(KEY_CATEGORY);

        /* set up our context */
        fragAct = (FragmentActivity) getActivity();
        /* set up recycler view */
        rvActivityView = fragAct.findViewById(R.id.rvActivityView);
        /* init data set */
        myActivities = new ArrayList<>();
        /* init adapter */
        itemAdapter = new ActivityItemAdapter(myActivities, this);
        rvActivityView.setLayoutManager(new LinearLayoutManager(fragAct));
        /* set up adapter */
        rvActivityView.setAdapter(itemAdapter);
        /* populate with activities */
        loadActivities(category);
        /* by this point our data set should be populated so now we can perform searches */
        setHasOptionsMenu(true);
    }


    /* credit to this mans: https://blog.aimanbaharum.com/2015/01/29/android-development-8-implementing-searchview-within-a-fragment/ */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /* mplementing ActionBar Search inside a fragment */
        MenuItem item = menu.add("Search");
        item.setIcon(R.drawable.search_icon);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        SearchView searchView = new SearchView(getActivity());

        /* modifying the text inside edittext component */
        int id = searchView.getContext().getResources().getIdentifier("android:id/activitySearchBar", null, null);
        TextView textView = (AutoCompleteTextView) searchView.findViewById(R.id.search_src_text);
        Log.d(tag, "Id: " + id);
        textView.setHint("Search activities");
        textView.setHintTextColor(getResources().getColor(R.color.testColor));
        textView.setTextColor(getResources().getColor(R.color.writeColor));
//
        /* implement the listener */
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                /* once the user hits the submit bar */
                Log.d(tag, "submitted text");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                /* every time the user changes their search */
                Log.d(tag, "changed text");
                return false;
            }
        });
        item.setActionView(searchView);
    }

    private void loadActivities(String category) {
        final Activity.Query activityQuery = new Activity.Query();
        /* get specified category activities */
        activityQuery.getCategoryAct(category);
        activityQuery.findInBackground(new FindCallback<Activity>() {
            @Override
            public void done(List<Activity> objects, ParseException e) {
                if(e == null){
                    Log.d(tag, "Activities loaded successfully");
                    Log.d(tag, "Objects list size: " + objects.size());
                    for(int i = 0; i < objects.size(); i++){
                        myActivities.add(objects.get(i));
                        Log.d(tag, "Name: " + myActivities.get(i).getName());
                        itemAdapter.notifyItemInserted(myActivities.size() - 1);
                    }
                }
                else{
                    Log.d(tag, "Failed to load activies :(");
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activity_selection, container, false);
    }

    @Override
    public void passActivity(Activity activity) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        InputFragment inputFragment = InputFragment.newInstance(activity);
        inputFragment.show(fragmentManager, "tagz");
    }
}
