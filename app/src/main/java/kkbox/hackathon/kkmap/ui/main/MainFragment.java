package kkbox.hackathon.kkmap.ui.main;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.button.MaterialButton;

import kkbox.hackathon.kkmap.R;
import kkbox.hackathon.kkmap.ui.map.MapFragment;

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

public class MainFragment extends Fragment implements LocationListener {

    private MainViewModel mainViewModel;
    private LocationManager locationManager;
    private TextView artistTextView;
    private TextView songTextView;
    private LatLng currentLocation = new LatLng(0,0);
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mainViewModel =
                ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        songTextView = root.findViewById(R.id.sont_text_main);
        artistTextView = root.findViewById(R.id.artist_text_main);
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
        getLocation();
        return root;
    }
    private void getLocation(){
        if (this.getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && this.getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            this.getActivity().requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MapFragment.REQUEST);
        }
        locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = locationManager.getBestProvider(criteria, true);

        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            this.onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(bestProvider, 1000, 20, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        artistTextView.setText(currentLocation.toString());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}