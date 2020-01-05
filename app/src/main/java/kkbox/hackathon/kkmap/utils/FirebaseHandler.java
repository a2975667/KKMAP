package kkbox.hackathon.kkmap.utils;

import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import kkbox.hackathon.kkmap.model.Song;

public class FirebaseHandler {

    private static FirebaseHandler single_instance = null;
    private static FirebaseDatabase firebase;
    private static final String ITEM_LOCATIONS = "ITEM_LOCATIONS";
    private static final String ITEM_INFOS = "ITEM_INFOS";

    FirebaseHandler() {
        firebase = FirebaseDatabase.getInstance();
    }

    public static FirebaseHandler getInstance()
    {
        if (single_instance == null){
            single_instance = new FirebaseHandler();
        }

        return single_instance;
    }

    public void pinSongOnLocation(Song song, LatLng location) {
        FirebaseAuth authRef = FirebaseAuth.getInstance();
        DatabaseReference ref = firebase.getReference(ITEM_LOCATIONS);
        GeoFire geoFire = new GeoFire(ref);
        GeoLocation geoLocation = new GeoLocation(location.latitude, location.longitude);
        String currentUserId = authRef.getCurrentUser().getUid();
        String keyCode = currentUserId +":"+song.toString();
        geoFire.setLocation(keyCode, geoLocation, new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                Log.d("test", String.valueOf(key));
                DatabaseReference itemInfo = firebase.getReference(ITEM_INFOS+"/"+key);
                itemInfo.child("userID").setValue(currentUserId);
                itemInfo.child("artist").setValue(song.getArtistName());
                itemInfo.child("song").setValue(song.getSongName());
            }
        });

    }
}
