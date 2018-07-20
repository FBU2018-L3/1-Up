package com.l3.one_up.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.l3.one_up.R;
import com.l3.one_up.adapters.CategoryAdapter;
import com.l3.one_up.listeners.OnUserTogglesSleepListener;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.Unbinder;


public class HomeFragment extends Fragment {

    private ImageView ivProfile;
    private TextView tvWelcome;
    private TextView tvLevelNum;
    private TextView tvXPNum;

    private RecyclerView rvCategories;
    private CategoryAdapter categoriesAdapter;
    private TypedArray categoryIcons;
    private String[] categories;

    private OnFragmentInteractionListener mListener;

    private ParseUser user;

    private OnUserTogglesSleepListener sleepListener;

    private Unbinder unbinder;
    @BindView(R.id.tbSleepSwitch) ToggleButton tbSleepSwitch;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(OnUserTogglesSleepListener sleepListener) {
        HomeFragment fragment = new HomeFragment();
        fragment.sleepListener = sleepListener;
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
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
        categoryIcons = getResources().obtainTypedArray(R.array.your_array_name);

        categoriesAdapter = new CategoryAdapter(categories, categoryIcons);

        rvCategories = (RecyclerView) getActivity().findViewById(R.id.rvCategories);
        rvCategories.setAdapter(categoriesAdapter);


        tbSleepSwitch.setChecked(user.getBoolean("isAsleep"));
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
        unbinder.unbind();
    }

    public interface OnFragmentInteractionListener {
        void onProfilePictureClick();
        void onCategoryClick(String categoryName);
    }

    @OnCheckedChanged(R.id.tbSleepSwitch)
    public void onToggleButtonClicked(){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Getting the current timestamp
        long time = System.currentTimeMillis();

        // Save into shared preferences
        editor.putLong("sleepTime", time).apply();
        sleepListener.toggleSleep(true);
    }


}
