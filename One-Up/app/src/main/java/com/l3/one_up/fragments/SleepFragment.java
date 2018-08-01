package com.l3.one_up.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.l3.one_up.R;
import com.l3.one_up.interfaces.BackIsClickable;
import com.l3.one_up.listeners.OnUserTogglesSleepListener;
import com.l3.one_up.model.Activity;
import com.l3.one_up.model.Event;
import com.l3.one_up.model.User;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SleepFragment extends Fragment implements BackIsClickable {

    private OnUserTogglesSleepListener sleepListener;

    private Unbinder unbinder;
    @BindView(R.id.tbSleepSwitch) ToggleButton tbSleepSwitch;

    public SleepFragment(){}

    public static SleepFragment newInstance(OnUserTogglesSleepListener sleepListener){
        SleepFragment sf = new SleepFragment();
        sf.sleepListener = sleepListener;
        return sf;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sleep, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tbSleepSwitch.setChecked(true);
    }

    @OnClick(R.id.tbSleepSwitch)
    public void onToggleButtonClicked(){
        // Getting the current timestamp
        long endTime = System.currentTimeMillis();

        // Retrieving when the user went to sleep
        SharedPreferences sharedPref = getActivity().getPreferences(getContext().MODE_PRIVATE);
        long startTime = sharedPref.getLong("sleepTime", -1);

        // If there is no sleep time then it ends
        if(startTime == -1)
        {
            displayErrorMsg("There was an error retrieving your sleep time, sorry :c");
            sleepListener.toggleSleep(false);
            return;
        }

        Double totalTimeHours = (endTime-startTime)/3600000.0;
        final int xp = 5*totalTimeHours.intValue();
        final Event event = new Event();
        try {
            event.setInputType(new JSONObject().put("hours", totalTimeHours));
            event.setTotalXP(xp);
            event.setUser(User.getCurrentUser());
            Activity.Query query = new Activity.Query();

            // Search the local database for the sleep activity
            query.fromLocalDatastore().getInBackground("v3IoWcJGy9", new GetCallback<Activity>() {
                @Override
                public void done(Activity object, ParseException e) {
                    if(e!=null){
                        // Obtaining the sleep activity
                        new Activity.Query().getInBackground("v3IoWcJGy9", new GetCallback<com.l3.one_up.model.Activity>() {
                            @Override
                            public void done(com.l3.one_up.model.Activity sleepActivity, ParseException e) {
                                saveEvent(event, sleepActivity, xp);
                                sleepActivity.pinInBackground();
                            }
                        });
                    }
                    else
                    {
                        saveEvent(event, object, xp);
                    }
                }
            });
        } catch (JSONException e) {
            displayErrorMsg("Sorry, try again please :c", e);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        //((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        //((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    private void displayErrorMsg(String msg, Exception e){
        if(e!=null){
            e.printStackTrace();
        }
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void displayErrorMsg(String msg){
        displayErrorMsg(msg,null);
    }

    private void saveEvent(Event event, Activity activity, final int xp){
        event.setActivity(activity);
        event.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    User user = User.getCurrentUser();
                    sleepListener.toggleSleep(false);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    int startLvl = user.getLevel();
                    InputConfirmationFragment inputFragment = InputConfirmationFragment.newInstance(user.updateExperiencePoints(xp), startLvl);
                    inputFragment.show(fragmentManager, "tagz");
                }
                else{
                    displayErrorMsg("Sorry, try again please :c", e);
                }
            }
        });

    }

    public boolean allowBackPressed(){
        return !User.getCurrentUser().isAsleep();
    }


}
