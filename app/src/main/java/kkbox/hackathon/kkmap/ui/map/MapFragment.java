package kkbox.hackathon.kkmap.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

import kkbox.hackathon.kkmap.SongMarker;
import kkbox.hackathon.kkmap.ui.LocationEnabledFragment;
import kkbox.hackathon.kkmap.R;
import kkbox.hackathon.kkmap.utils.FirebaseHandler;


public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener, LocationEnabledFragment {

    private MapView mapView;
    private GoogleMap gmap;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    HashMap<String, SongMarker> currentSongMarkers;
    public static final int REQUEST = 112;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng ny;
    private GeoQuery songMarkerGeoQuery;
    private FirebaseHandler.Callback geoQueryCallback;
    private Boolean isCameraFollowUser = true;

    private void followUserOnMap() {
        if( gmap != null ) {
            gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
            gmap.moveCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
        }

    }

    private void updateGeoQueryLocation(double radius) {
        if( songMarkerGeoQuery != null){
            songMarkerGeoQuery.setLocation(new GeoLocation(ny.latitude, ny.longitude), radius);
        }
    }

    private void refreshMarkersOnMap() {
        if(gmap != null) {
            gmap.clear();
            gmap.addMarker(new MarkerOptions()
                    .position(ny)
                    .title("Current Location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.standing_man)));

            currentSongMarkers.forEach((key, songMarker)->{
                gmap.addMarker(
                        new MarkerOptions()
                                .position(songMarker.getLocation())
                                .title(songMarker.getSong().getSongName())
                );
            });

        }
    }

    @Override
    public void onLocationChanged(Location loc) {
        ny = new LatLng(loc.getLatitude(),loc.getLongitude());
        if(gmap != null){
            gmap.clear();
            if(isCameraFollowUser) {
                this.followUserOnMap();
            }
            updateGeoQueryLocation(10);
            refreshMarkersOnMap();
        }
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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = root.findViewById(R.id.mapView);
        currentSongMarkers = new HashMap<String, SongMarker>();
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        geoQueryCallback = new FirebaseHandler.Callback() {
            @Override
            public void onSuccess(Object obj) {
                Pair<String, SongMarker> castedObj = (Pair<String, SongMarker>) obj;
                currentSongMarkers.put(castedObj.first, castedObj.second);
                Log.d("onSucess", currentSongMarkers.toString());
                refreshMarkersOnMap();
            }

            @Override
            public void onFailure(Object obj) {
                Log.d("onFailure", currentSongMarkers.toString());
                currentSongMarkers.remove((String)obj);
                refreshMarkersOnMap();
            }
        };
        songMarkerGeoQuery = FirebaseHandler.getInstance().querySongMarkerWithLocation(new LatLng(0 , 0),
                geoQueryCallback);
        locationManager = (LocationManager)
                this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        getLocation();

        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        return root;
    }

    @Override
    public void getLocation() {
        if (this.getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && this.getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            this.getActivity().requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST);
        }
        else {
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

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        songMarkerGeoQuery.removeAllListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        getLocation();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        getLocation();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    public void onDestroy() {
        songMarkerGeoQuery.removeAllListeners();
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.getUiSettings().setAllGesturesEnabled(true);
        getLocation();
        GoogleMap.OnMapClickListener onMapClickListener = new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                isCameraFollowUser = true;
            }
        };
        gmap.setOnMapClickListener(onMapClickListener);
        GoogleMap.OnMarkerClickListener onMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                isCameraFollowUser = false;
                return false;
            }
        };
        gmap.setOnMarkerClickListener(onMarkerClickListener);
        return;
    }
}