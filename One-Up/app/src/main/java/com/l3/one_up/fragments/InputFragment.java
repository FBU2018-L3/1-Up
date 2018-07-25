package com.l3.one_up.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.l3.one_up.Objective;
import com.l3.one_up.R;
import com.l3.one_up.model.Activity;
import com.l3.one_up.model.Event;
import com.l3.one_up.model.User;
import com.parse.ParseException;
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

    @BindView(R.id.spInputType) Spinner spInputType;
    @BindView(R.id.etValue) EditText etValue;

    // Empty constructor required
    public InputFragment(){}

    public static InputFragment newInstance(Activity activity, Objective objective) {
        InputFragment frag = new InputFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_ACTIVITY, activity);
        args.putInt(KEY_OBJECTIVE, objective.ordinal());
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
            Integer basePoints = activity.getInputType().getInt((String)spInputType.getSelectedItem());
            int exp = basePoints * Integer.parseInt(etValue.getText().toString());

            // Obtaining the user
            User current = User.getCurrentUser();

            if (objective == Objective.EVENT) {
                saveEvent(exp, current);
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
                }
                else{
                    Toast.makeText(getContext(), "There was an error, please try again later", Toast.LENGTH_LONG).show();
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
                    Fragment parentFragment = getParentFragment();
                    FragmentManager fm;
                    if (parentFragment == null) {
                        fm = getActivity().getSupportFragmentManager();
                    } else {
                        fm = parentFragment.getChildFragmentManager();
                    }
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
}
