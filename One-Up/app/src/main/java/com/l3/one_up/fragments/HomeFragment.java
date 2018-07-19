package com.l3.one_up.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.l3.one_up.R;
import com.parse.ParseUser;


public class HomeFragment extends Fragment {

    private ImageView ivProfile;
    private TextView tvWelcome;
    private TextView tvLevelNum;
    private TextView tvXPNum;

    private ListView lvCategories;
    private ArrayAdapter categoriesAdapter;
    private String[] categories;

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

        user = ParseUser.getCurrentUser();

        // populate text fields w user info
        if (user != null) {
            StringBuilder welcomeText = new StringBuilder("Welcome, ");
            welcomeText.append(user.getUsername());
            tvWelcome.setText(welcomeText.toString());
            tvLevelNum.setText(String.valueOf(user.getInt("level")));
            tvXPNum.setText(String.valueOf(user.getInt("experiencePoints")));
        }

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onProfilePictureClick();
                }
            }
        });

        categories = getResources().getStringArray(R.array.categories);

        categoriesAdapter = new ArrayAdapter<>(getContext(), R.layout.item_category, categories);

        lvCategories = (ListView) getActivity().findViewById(R.id.lvCategories);
        lvCategories.setAdapter(categoriesAdapter);
        lvCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onButtonPressed(categories[position]);
            }
        });

    }

    public void onButtonPressed(String categoryName) {
        if (mListener != null) {
            mListener.onCategoryClick(categoryName);
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
        void onProfilePictureClick();
        void onCategoryClick(String categoryName);
    }
}
