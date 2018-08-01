package com.l3.one_up;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.l3.one_up.model.Goal;
import com.l3.one_up.model.User;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class GoalCompletedFragment extends DialogFragment {

    private static final String ARG_GOAL = "goal";
    private Goal goal;

    private Unbinder unbinder;

    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.tvUserLvl) TextView tvUserLvl;
    @BindView(R.id.rlImgLvl) RelativeLayout rlImgLvl;
    @BindView(R.id.tvCongrats) TextView tvCongrats;

    public GoalCompletedFragment() {
        // Required empty public constructor
    }

    public static GoalCompletedFragment newInstance(Goal goal) {
        GoalCompletedFragment fragment = new GoalCompletedFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_GOAL, goal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            goal = getArguments().getParcelable(ARG_GOAL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_goal_completed, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        User user = User.getCurrentUser();

        tvUserName.setText(user.getUsername());
        //pbExperiencePoints.setProgress(user.getInt("experiencePoints")%100);
        tvUserLvl.setText(String.valueOf(user.getLevel()));

        tvCongrats.setText(makeCongratsMessage(goal));
    }

    private String makeCongratsMessage(Goal goal) {
        StringBuilder sb = new StringBuilder("Congrats!");
        sb.append("You completed ");
        try {
            if (goal.getInputType().keys().hasNext()) {
                String inputType = goal.getInputType().keys().next();
                sb.append(goal.getInputType().getInt(inputType));
                sb.append(" ");
                sb.append(inputType);
                sb.append(" of ");
            }
        } catch (JSONException jsone) {
            jsone.printStackTrace();
        }
        sb.append(goal.getActivity().getName());
        return sb.toString();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btnOk)
    public void dismiss(){
        super.dismiss();
    }

}
