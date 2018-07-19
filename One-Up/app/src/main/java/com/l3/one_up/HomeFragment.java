package com.l3.one_up;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.ToggleButton;

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

    private ListView lvCategories;
    private ArrayAdapter categoriesAdapter;
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
            tvWelcome.setText("Welcome, " + user.getUsername());
            tvLevelNum.setText(""+ user.getInt("level"));
            tvXPNum.setText("" + user.getInt("experiencePoints"));
        }

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
