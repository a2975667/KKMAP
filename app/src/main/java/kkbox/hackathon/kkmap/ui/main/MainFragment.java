package kkbox.hackathon.kkmap.ui.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;
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


import java.util.function.Function;

import kkbox.hackathon.kkmap.R;
import kkbox.hackathon.kkmap.model.Song;
import kkbox.hackathon.kkmap.ui.map.MapFragment;
import kkbox.hackathon.kkmap.utils.FirebaseHandler;

public class MainFragment extends Fragment implements LocationListener {

    private MainViewModel mainViewModel;
    private LocationManager locationManager;
    private Song currentSong;
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
        setViewModelObservr();
        final Button pinButton = root.findViewById(R.id.pin_button);
        pinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinLocation();
            }
        });
        getLocation();
        return root;
    }

    private void setViewModelObservr() {
        mainViewModel.getSong().observe(getActivity(), new Observer<Song>() {
            @Override
            public void onChanged(@Nullable Song s) {
                currentSong = s;
                songTextView.setText(s.getSongName());
                artistTextView.setText(s.getArtistName());
            }
        });
    }

    private void getLocation() {
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

    private void pinLocation() {
        FirebaseHandler fHandler = FirebaseHandler.getInstance();
        fHandler.pinSongOnLocation(currentSong, currentLocation);
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
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