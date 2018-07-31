package com.l3.one_up.fragments;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.l3.one_up.R;
import com.l3.one_up.model.PowerUp;
import com.parse.ParseException;
import com.parse.SaveCallback;

public class SendPowerUpFragment extends DialogFragment {

    private PowerUp newPowerUp;
    // Button for collecting message
    private Button btSubmit;
    // textview for message
    private EditText etMessage;
    // placeholder name textview
    private TextView tvUsername;
    // set up our "context"
    FragmentActivity fragAct;

    public SendPowerUpFragment() {
        // Required empty public constructor
    }

    public static SendPowerUpFragment newInstance(PowerUp newPowerUp) {
        SendPowerUpFragment fragment = new SendPowerUpFragment();
        fragment.newPowerUp = newPowerUp;
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
       super.onViewCreated(view, savedInstanceState);
       fragAct = (FragmentActivity) getActivity();

       btSubmit = view.findViewById(R.id.btSendPowerUp);
       etMessage = view.findViewById(R.id.etSendMessage);
       tvUsername = view.findViewById(R.id.tvSendToName);

       tvUsername.setText(newPowerUp.getSentToUser().getUsername());

       btSubmit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String message  = etMessage.getText().toString();
               newPowerUp.setMessage(message);
               newPowerUp.saveInBackground(new SaveCallback() {
                   @Override
                   public void done(ParseException e) {
                       if(e == null){
                           Toast.makeText(getContext(), "You sent a power up to " + newPowerUp.getSentToUser().getUsername(), Toast.LENGTH_LONG).show();
                           SendPowerUpFragment.super.dismiss();
                       }
                       else {
                            Toast.makeText(getContext(), "Failed to send power up", Toast.LENGTH_LONG).show();
                       }
                   }
               });

           }
       });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_send_power_up, container, false);
    }


}
