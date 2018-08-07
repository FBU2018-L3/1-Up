package com.l3.one_up.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.l3.one_up.listeners.OnRedeemedPowerUpRefresh;
import com.l3.one_up.model.User;
import com.l3.one_up.services.DeepLinkingActivity;
import com.l3.one_up.R;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {
    private final String tag = "ProfileFragment";
    private ImageView ivProfile;
    private TextView tvWelcome;
    private TextView tvLevelNum;
    private TextView tvXPNum;
    private Button btnLogOut;
    /* NEW: FB login button */
    private Button btfbLogin;
    /* view power up button */
    private Button btPowerUps;
    private static final int REQUEST_CODE = 1;

    private OnFragmentInteractionListener mListener;

    private ParseUser user;
    private Activity activity;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        activity = getActivity();

        super.onViewCreated(view, savedInstanceState);
        ivProfile = (ImageView) activity.findViewById(R.id.ivProfile);
        tvWelcome = (TextView) activity.findViewById(R.id.tvWelcome);
        tvLevelNum = (TextView) activity.findViewById(R.id.tvLevelNum);
        tvXPNum = (TextView) activity.findViewById(R.id.tvXPNum);

        btnLogOut = (Button) activity.findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onLogoutClicked();
                }
            }
        });
        /* NEW: trying to launch our login activity */
        /* Note to self: Ask Lucie where shes implemented the listener functions because consistency */
        btfbLogin = (Button) activity.findViewById(R.id.btfbLogin);
        btfbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchFBlogin = new Intent(activity.getApplicationContext(), DeepLinkingActivity.class);
                startActivityForResult(launchFBlogin, REQUEST_CODE);
            }
        });

        user = ParseUser.getCurrentUser();

        // populate text fields w user info
        if (user != null) {
            tvWelcome.setText(user.getUsername());
            tvLevelNum.setText(String.valueOf(user.getInt("level")));
            tvXPNum.setText(String.valueOf(user.getInt("experiencePoints")));
        }

        /* launch the feed fragment from here */
        boolean isTimeline = false;
        FeedFragment feedFragment = FeedFragment.newInstance(isTimeline);
        FragmentManager fragmentManager = getFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.feedContainer, feedFragment).commit();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onLogoutClicked();
    }
}
