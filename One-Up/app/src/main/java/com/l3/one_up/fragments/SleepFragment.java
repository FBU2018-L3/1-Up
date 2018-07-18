package com.l3.one_up.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.l3.one_up.HomeFragment;
import com.l3.one_up.R;
import com.l3.one_up.listeners.OnUserTogglesSleepListener;
import com.parse.ParseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SleepFragment extends Fragment {

    private OnUserTogglesSleepListener sleepListener;

    private Unbinder unbinder;
    @BindView(R.id.tbSleepSwitch) ToggleButton tbSleepSwitch;

    public SleepFragment(){}

    public static SleepFragment newInstance(OnUserTogglesSleepListener sleepListener){
        SleepFragment sf = new SleepFragment();
        sf.sleepListener = sleepListener;
        return sf;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sleep, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tbSleepSwitch.setChecked(true);
    }

    @OnClick(R.id.tbSleepSwitch)
    public void switchSleep(){
        sleepListener.switchSleep(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }
}
