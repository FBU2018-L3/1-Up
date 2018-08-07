package com.l3.one_up.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.facebook.share.widget.ShareButton;
import com.l3.one_up.R;
import com.l3.one_up.interfaces.BackIsClickable;
import com.l3.one_up.listeners.OnUserTogglesSleepListener;
import com.l3.one_up.services.AvatarFinder;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.Unbinder;


public class HomeFragment extends Fragment implements CategorySelectionFragment.OnCategorySelectedListener, BackIsClickable {

    private ImageView ivProfile;
    private TextView tvWelcome;
    private TextView tvLevelNum;
    private TextView tvXPNum;
    /* Button for toggling */
    private Button btSeePowerUps;
    /* fragment flag */
    private boolean fragmentFlag;

    private ShareButton btnFacebookShare;

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
        fragmentFlag = true;

        ivProfile = (ImageView) getActivity().findViewById(R.id.ivProfile);
        tvWelcome = (TextView) getActivity().findViewById(R.id.tvWelcome);
        tvLevelNum = (TextView) getActivity().findViewById(R.id.tvLevelNum);
        tvXPNum = (TextView) getActivity().findViewById(R.id.tvXPNum);
        btSeePowerUps = (Button) getActivity().findViewById(R.id.btSeePowerUps);

        user = ParseUser.getCurrentUser();

        // populate text fields w user info
        if (user != null) {
            StringBuilder welcomeText = new StringBuilder("Welcome, ");
            welcomeText.append(user.getUsername());
            tvWelcome.setText(welcomeText.toString());
            tvLevelNum.setText(String.valueOf(user.getInt("level")));
            tvXPNum.setText(String.valueOf(user.getInt("experiencePoints")));
        }

        ivProfile.setImageResource(new AvatarFinder().getAvatarId(getContext()));
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onProfilePictureClick();
                }
            }
        });

        CategorySelectionFragment categorySelectionFragment = CategorySelectionFragment.newInstance();
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.categoryContainer, categorySelectionFragment);
        ft.addToBackStack("category").commit();

        tbSleepSwitch.setChecked(user.getBoolean("isAsleep"));

        btSeePowerUps.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(fragmentFlag){
                    btSeePowerUps.setText("Go back to categories");
                    fragmentFlag = false;
                    PowerUpFragment powerUpFragment = PowerUpFragment.newInstance();
                    FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                    ft.replace(R.id.categoryContainer, powerUpFragment);
                    ft.addToBackStack("category").commit();

                }
                else {
                    btSeePowerUps.setText("See Power Ups");
                    fragmentFlag = true;
                    CategorySelectionFragment categorySelectionFragment = CategorySelectionFragment.newInstance();
                    FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                    ft.replace(R.id.categoryContainer, categorySelectionFragment);
                    ft.addToBackStack("category").commit();
                }
            }
        });
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

    @Override
    public void onCategoryClick(String categoryName) {
        if (mListener != null) {
            // pass action to parent
            mListener.onCategoryInteraction(categoryName);
        }
    }

    public boolean allowBackPressed() {
        return false;
    }

    public interface OnFragmentInteractionListener {
        void onProfilePictureClick();
        void onCategoryInteraction(String categoryName);
    }

    @OnCheckedChanged(R.id.tbSleepSwitch)
    public void onToggleButtonClicked() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Getting the current timestamp
        long time = System.currentTimeMillis();

        // Save into shared preferences
        editor.putLong("sleepTime", time).apply();
        sleepListener.toggleSleep(true);
    }



}
