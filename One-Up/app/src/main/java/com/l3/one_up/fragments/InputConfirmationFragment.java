package com.l3.one_up.fragments;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.github.jinatonic.confetti.CommonConfetti;
import com.l3.one_up.R;
import com.l3.one_up.animations.ProgressBarAnimation;
import com.l3.one_up.model.User;

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
    @BindView(R.id.ivUserAvatar) ImageView ivUserAvatar;
    @BindView(R.id.fb_share_button) ShareButton btnFacebookShare;

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
        View view = inflater.inflate(R.layout.fragment_input_confirmation, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        User user = User.getCurrentUser();

        tvUserName.setText(user.getUsername());
        //pbExperiencePoints.setProgress(user.getInt("experiencePoints")%100);
        tvUserLvl.setText(String.valueOf(user.getLevel()));

        pbExperiencePoints.setMax(User.getCurrentUser().getNeededXpToLevelUp());

        setAnimation(view, user);

        setBtnFacebookShare();

    }

    private void setAnimation(View view, User user) {

        int startXp = getArguments().getInt("startXp");
        int endXp = user.getCurrentXpFromLevel();
        int startLvl = getArguments().getInt("startLvl");

        // Animation:
        ProgressBarAnimation anim;
        if(startXp >= endXp) {
            anim = new ProgressBarAnimation(pbExperiencePoints, startXp,  100);
            anim.setDuration(1000);
            pbExperiencePoints.startAnimation(anim);
            anim = new ProgressBarAnimation(pbExperiencePoints, 0, endXp);
            anim.setDuration(1000);
            pbExperiencePoints.startAnimation(anim);
        }
        else
        {
            anim = new ProgressBarAnimation(pbExperiencePoints, startXp, endXp);
            anim.setDuration(1000);
            pbExperiencePoints.startAnimation(anim);
        }

        if(user.getLevel()>startLvl){
            final ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();

            // Setting the gif
            Glide.with(getContext()).load(R.drawable.andy_celebrating).into(ivUserAvatar);
            tvCongrats.setText(tvCongrats.getText().toString()+"\nYou leveled up!");
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        CommonConfetti.rainingConfetti(rlImgLvl, new int[] { Color.RED, Color.YELLOW })
                                .stream(75);
                    }
                });
            }

        }
    }

    public void setBtnFacebookShare() {
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com/"))
                .setQuote("Move fast.")
                .build();
        btnFacebookShare.setShareContent(content);
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
