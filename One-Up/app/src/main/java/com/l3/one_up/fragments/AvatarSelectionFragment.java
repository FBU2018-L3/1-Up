package com.l3.one_up.fragments;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.l3.one_up.R;
import com.l3.one_up.model.User;
import com.parse.ParseException;
import com.parse.SaveCallback;

public class AvatarSelectionFragment extends Fragment {

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

        tvAvatarName.setText("avatar1");
        tvAvatarName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AvatarSelection", "text was clicked");
                User user = User.getCurrentUser();
                user.setAvatar(1);
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.d("AvatarSelection", "done updating user's avatar in Parse");
                        TypedArray avatarIds = getResources().obtainTypedArray(R.array.avatar_id);
                        int avatarResId = avatarIds.getResourceId(0, R.mipmap.ic_launcher);
                        ivAvatar.setImageResource(avatarResId);
                        Log.d("AvatarSelection", "the ID of the resources is " + avatarResId);
                    }
                });
            }
        });
    }

}
