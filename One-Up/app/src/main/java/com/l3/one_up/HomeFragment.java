package com.l3.one_up;

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

import com.parse.ParseUser;


public class HomeFragment extends Fragment {

    private ImageView ivProfile;
    private TextView tvWelcome;
    private TextView tvLevelNum;
    private TextView tvXPNum;

    private Button btnCategorySelect;
    private OnFragmentInteractionListener mListener;

    private ParseUser user;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivProfile = (ImageView) getActivity().findViewById(R.id.ivProfile);
        tvWelcome = (TextView) getActivity().findViewById(R.id.tvWelcome);
        tvLevelNum = (TextView) getActivity().findViewById(R.id.tvLevelNum);
        tvXPNum = (TextView) getActivity().findViewById(R.id.tvXPNum);

        btnCategorySelect = (Button) getActivity().findViewById(R.id.btnCategorySelect);
        btnCategorySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed();
            }
        });

        user = ParseUser.getCurrentUser();

        if (user != null) {
            tvWelcome.setText("Welcome, " + user.getUsername());
            tvLevelNum.setText(""+ user.getInt("level"));
            tvXPNum.setText("" + user.getInt("experiencePoints"));
        }

    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onCategoryClick("fitness");
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
        void onCategoryClick(String categoryName);
    }
}
