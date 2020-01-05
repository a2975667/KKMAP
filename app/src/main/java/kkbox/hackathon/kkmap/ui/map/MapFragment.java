package kkbox.hackathon.kkmap.ui.map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import kkbox.hackathon.kkmap.R;


public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener{

    private MapView mapView;
    private GoogleMap gmap;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    public static final int REQUEST = 112;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng ny;

    @Override
    public void onLocationChanged(Location loc) {
        ny = new LatLng(loc.getLatitude(),loc.getLongitude());
        Log.d("TEST", "onLocationChanged: "+ny.toString());
        Log.d("TEST", String.valueOf(gmap));
        if(gmap != null){
            gmap.clear();
            gmap.addMarker(new MarkerOptions()
                    .position(ny)
                    .title("Current Location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.standing_man)));
            gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
            gmap.moveCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
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

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        locationManager = (LocationManager)
                this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        getLocation();

        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        return root;
    }

    public void getLocation() {
        if (this.getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && this.getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            this.getActivity().requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST);
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

        return;
    }
}