package com.l3.one_up.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jinatonic.confetti.CommonConfetti;
import com.l3.one_up.R;
import com.l3.one_up.animations.ProgressBarAnimation;
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
    @BindView(R.id.rlImgLvl) RelativeLayout rlImgLvl;
    @BindView(R.id.tvCongrats) TextView tvCongrats;

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
        ParseUser user = ParseUser.getCurrentUser();

        int startXp = getArguments().getInt("startXp")%100;
        int endXp = user.getInt("experiencePoints") % 100;
        int startLvl = getArguments().getInt("startLvl");

        tvUserName.setText(user.getUsername());
        //pbExperiencePoints.setProgress(user.getInt("experiencePoints")%100);
        tvUserLvl.setText(user.getInt("level")+"");


        // Animation:
        ProgressBarAnimation anim;
        if(startXp >= endXp) {
            anim = new ProgressBarAnimation(pbExperiencePoints, startXp % 100,  100);
            anim.setDuration(1000);
            pbExperiencePoints.startAnimation(anim);
            anim = new ProgressBarAnimation(pbExperiencePoints, 0,  user.getInt("experiencePoints") % 100);
            anim.setDuration(1000);
            pbExperiencePoints.startAnimation(anim);
        }
        else
        {
            anim = new ProgressBarAnimation(pbExperiencePoints, startXp % 100, user.getInt("experiencePoints") % 100);
            anim.setDuration(1000);
            pbExperiencePoints.startAnimation(anim);
        }

        if(user.getInt("level")>startLvl){
            final ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
            tvCongrats.setText(tvCongrats.getText().toString()+"\nYou leveled up!");
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        CommonConfetti.rainingConfetti(rlImgLvl, new int[] { Color.RED, Color.YELLOW })
                                .stream(100);
                    }
                });
            }

        }

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
