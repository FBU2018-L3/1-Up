package com.l3.one_up.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.l3.one_up.Objective;
import com.l3.one_up.R;
import com.l3.one_up.model.Activity;
import com.l3.one_up.model.Event;
import com.l3.one_up.model.Goal;
import com.l3.one_up.model.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class InputFragment extends DialogFragment {

    private static final String TAG = "DialogFragment";
    private static final String KEY_ACTIVITY = "activity";
    private static final String KEY_OBJECTIVE = "objective";

    private Unbinder unbinder;
    private Activity activity;
    private Objective objective;
    private String inputType;
    private List<Goal> activeGoals;

    @BindView(R.id.spInputType) Spinner spInputType;
    @BindView(R.id.etValue) EditText etValue;
    @BindView(R.id.cbIsPrivate) CheckBox cbIsPrivate;
    @BindView(R.id.tvGoalWarning) TextView tvGoalWarning;

    // Empty constructor required
    public InputFragment(){}

    public static InputFragment newInstance(Activity activity, Objective objective) {
        InputFragment frag = new InputFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_ACTIVITY, activity);
        args.putInt(KEY_OBJECTIVE, objective.ordinal());
        Log.d(TAG, "objective is " + objective.ordinal() + ": " + objective.toString());
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = getArguments().getParcelable(KEY_ACTIVITY);

        int objectiveIndex = getArguments().getInt(KEY_OBJECTIVE);
        objective = Objective.values()[objectiveIndex];

        tvGoalWarning.setVisibility(View.GONE);

        List<String> spinnerArray =  new ArrayList<String>();
        for(int i =0; i< activity.getInputType().names().length(); i++)
        {
            try {
                spinnerArray.add(activity.getInputType().names().getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spInputType.setAdapter(adapter);

        getExistingGoals();

    }

    private void getExistingGoals() {
        Goal.Query existingGoalQuery = new Goal.Query();
        existingGoalQuery.onlyThisWeek()
                        .mostRecentFirst()
                        .byUser(ParseUser.getCurrentUser())
                        .ofActivity(activity)
                        .incomplete();
        existingGoalQuery.findInBackground(new FindCallback<Goal>() {
            @Override
            public void done(List<Goal> objects, ParseException e) {
                if (objects.size() == 0) {
                    // user does not have any active goals for this activity type
                    Toast.makeText(getContext(), "No existing goals this activity", Toast.LENGTH_LONG).show();
                } else {
                    // warn user that they already have a goal for this
                    Toast.makeText(getContext(), "Activity has a goal in progress", Toast.LENGTH_LONG).show();
                    tvGoalWarning.setVisibility(View.VISIBLE);
                    tvGoalWarning.setText(R.string.goal_already_exists_warning);
                    activeGoals = objects;
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btnSubmit)
    public void submit(){
        try {
            inputType = (String)spInputType.getSelectedItem();
            Integer basePoints = activity.getInputType().getInt(inputType);
            int exp = basePoints * Integer.parseInt(etValue.getText().toString());

            // Obtaining the user
            User current = User.getCurrentUser();

            if (objective == Objective.EVENT) {
                saveEvent(exp, current);
            } else if (objective == Objective.GOAL) {
                saveGoal(current);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveEvent(final int exp, final User currentUser) throws JSONException{
        // Event
        Event event = new Event();
        event.setActivity(activity);
        event.setTotalXP(exp);
        event.setUser(currentUser);
        event.setInputType(new JSONObject().put((String)spInputType.getSelectedItem(), etValue.getText().toString()));
        event.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                {
                    updateUser(currentUser, exp);
                    updateActiveGoals(activeGoals);
                }
                else{
                    Toast.makeText(getContext(), "There was an error, please try again later", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

    private void saveGoal(final User currentUser) throws JSONException {
        // Event
        Goal goal = new Goal();
        goal.setActivity(activity);
        goal.setUser(currentUser);
        String inputKey = (String)spInputType.getSelectedItem();
        int inputValue = Integer.parseInt(etValue.getText().toString());
        goal.setInputType(new JSONObject().put(inputKey, inputValue));
        goal.setProgress(new JSONObject().put(inputKey, 0));
        goal.setIsCompleted(false);
        goal.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                {
                    Toast.makeText(getContext(), "You've created a new goal!", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
                else{
                    Toast.makeText(getContext(), "There was an error in saving your goal, please try again later", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateUser(User current, int gainedExp){
        final Integer startXp = current.getCurrentXpFromLevel();
        final Integer startLvl = current.getLevel();

        // Updating xp and lvl
        current.updateExperiencePoints(gainedExp, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    int i = User.getCurrentUser().getCurrentXpFromLevel();
                    int x = User.getCurrentUser().getExperiencePoints();
                    FragmentManager fm = getParentFragmentManager();
                    InputConfirmationFragment icf = InputConfirmationFragment.newInstance(startXp, startLvl);
                    icf.show(fm, "icf");

                    dismiss();
                }
                else{
                    Toast.makeText(getContext(), "There was an error, please try again later", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateActiveGoals(List<Goal> activeGoals) {
        if (activeGoals != null && activeGoals.size() > 0) {
            for (final Goal goal : activeGoals) {
                // increment goal progress
                try {
                    Log.d("InputFragment", goal.getProgress().toString());
                    Log.d("InputFragment", inputType);
                    int oldProgress = goal.getProgress().getInt(inputType);
                    final int newProgress = oldProgress + Integer.parseInt(etValue.getText().toString());
                    goal.setProgress(new JSONObject().put(inputType, newProgress));

                    final int numericalGoal = goal.getInputType().getInt(inputType);
                    if (newProgress >= numericalGoal) {
                        goal.setIsCompleted(true);
                    }

                    goal.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Log.d("InputFragment", "Goal was updated!");
                            if (newProgress >= numericalGoal) {
                                FragmentManager fm = getParentFragmentManager();
                                GoalCompletedFragment gcf = GoalCompletedFragment.newInstance(goal);
                                gcf.show(fm, "icf");
                            }
                        }
                    });

                } catch (JSONException e) {
                    Log.d("InputFragment", "Unable to update goal");
                    e.printStackTrace();
                }
            }
        }
    }

    private FragmentManager getParentFragmentManager() {
        Fragment parentFragment = getParentFragment();
        FragmentManager fm;
        if (parentFragment == null) {
            fm = getActivity().getSupportFragmentManager();
        } else {
            fm = parentFragment.getChildFragmentManager();
        }
        return fm;
    }

}
