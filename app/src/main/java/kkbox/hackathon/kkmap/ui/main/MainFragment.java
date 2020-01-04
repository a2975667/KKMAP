package kkbox.hackathon.kkmap.ui.main;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;

import kkbox.hackathon.kkmap.R;

class PinButtonClickListener implements View.OnClickListener {
    Activity parent;
    PinButtonClickListener(Activity parent) {
        this.parent = parent;
    }
    @Override
    public void onClick (View v) {

        MediaSession mSession = new MediaSession(this.parent, "main");
        MediaController mController =  mSession.getController();
        Log.d("info", String.valueOf(mController.getPlaybackInfo()));
    }
}
public class MainFragment extends Fragment {

    private MainViewModel mainViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mainViewModel =
                ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        final TextView songTextView = root.findViewById(R.id.sont_text_main);
        final TextView artistTextView = root.findViewById(R.id.artist_text_main);
        mainViewModel.getSong().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                songTextView.setText(s);
            }
        });
        mainViewModel.getArtist().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                artistTextView.setText(s);
            }
        });


        final Button pinButton = root.findViewById(R.id.pin_button);
        PinButtonClickListener pinListenerObject = new PinButtonClickListener(this.getActivity());
        pinButton.setOnClickListener(pinListenerObject);
        return root;
    }
}