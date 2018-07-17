package com.l3.one_up.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.l3.one_up.R;
import com.l3.one_up.model.Activity;
import com.parse.ParseUser;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class InputConfirmationFragment extends DialogFragment {

    private static final String TAG = "InputConfirmationFragment";
    private Unbinder unbinder;


    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.pbExperiencePoints) ProgressBar pbExperiencePoints;
    @BindView(R.id.tvUserLvl) TextView tvUserLvl;

    // Empty constructor required
    public InputConfirmationFragment(){}

    public static InputConfirmationFragment newInstance(int startXp, int startLvl) {
        InputConfirmationFragment frag = new InputConfirmationFragment();
        Bundle args = new Bundle();
        args.putInt("startXp", startXp);
        args.putInt("startLvl", startLvl);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.input_confirmation_fragment, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int startXp = getArguments().getInt("startXp");
        int startLvl = getArguments().getInt("startLvl");
        ParseUser user = ParseUser.getCurrentUser();
        tvUserName.setText(user.getUsername());
        pbExperiencePoints.setProgress(user.getInt("experiencePoints")%100);
        tvUserLvl.setText(user.getInt("level")+"");
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
