package com.l3.one_up.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.l3.one_up.R;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {

    private ImageView ivProfile;
    private TextView tvWelcome;
    private TextView tvLevelNum;
    private TextView tvXPNum;
    private Button btnLogOut;

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

        user = ParseUser.getCurrentUser();

        // populate text fields w user info
        if (user != null) {
            tvWelcome.setText(user.getUsername());
            tvLevelNum.setText(String.valueOf(user.getInt("level")));
            tvXPNum.setText(String.valueOf(user.getInt("experiencePoints")));
        }

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
