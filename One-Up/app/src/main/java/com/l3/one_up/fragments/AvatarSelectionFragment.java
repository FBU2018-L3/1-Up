package com.l3.one_up.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.l3.one_up.R;
import com.l3.one_up.model.User;

public class AvatarSelectionFragment extends Fragment {

    TextView tvAvatarName;

    public AvatarSelectionFragment() {
        // Required empty public constructor
    }

    public static AvatarSelectionFragment newInstance() {
        AvatarSelectionFragment fragment = new AvatarSelectionFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_avatar_selection, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvAvatarName = getActivity().findViewById(R.id.tvAvatarName);
        tvAvatarName.setText("avatar1");
        tvAvatarName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = User.getCurrentUser();
                user.setAvatar("avatar2");
                user.saveInBackground();
            }
        });
    }

}
