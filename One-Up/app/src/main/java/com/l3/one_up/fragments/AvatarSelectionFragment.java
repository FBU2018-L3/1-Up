package com.l3.one_up.fragments;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.l3.one_up.R;
import com.l3.one_up.adapters.AvatarSelectionAdapter;

public class AvatarSelectionFragment extends Fragment implements AvatarSelectionAdapter.OnAvatarSelectListener {

    TypedArray avatarIDs;
    AvatarSelectionAdapter avatarSelectionAdapter;
    RecyclerView rvAvatarPicker;

    TextView tvAvatarName;
    ImageView ivAvatar;

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
        ivAvatar = getActivity().findViewById(R.id.ivAvatar);
        rvAvatarPicker = getActivity().findViewById(R.id.rvAvatarPicker);

        avatarIDs = getResources().obtainTypedArray(R.array.avatar_id);
        avatarSelectionAdapter = new AvatarSelectionAdapter(avatarIDs, this);

        rvAvatarPicker.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvAvatarPicker.setAdapter(avatarSelectionAdapter);

        tvAvatarName.setText("avatar1");
    }

    @Override
    public void onAvatarClicked(int position) {
        ivAvatar.setImageResource(avatarIDs.getResourceId(position, R.mipmap.ic_launcher));
    }
}
