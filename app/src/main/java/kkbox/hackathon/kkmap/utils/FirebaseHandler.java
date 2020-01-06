package kkbox.hackathon.kkmap.utils;

import android.telecom.Call;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import kkbox.hackathon.kkmap.SongMarker;
import kkbox.hackathon.kkmap.model.Song;
import kkbox.hackathon.kkmap.model.database.DBStoreSong;

public class FirebaseHandler {
    public interface Callback {
        void onSuccess(Object obj);
        void onFailure(Object obj);

    }
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

    private void getDataOnce(String dbRefPath, Callback callback) {
        DatabaseReference ref = firebase.getReference(dbRefPath);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                callback.onSuccess(dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void pinSongOnLocation(Song song, LatLng location) {
        FirebaseAuth authRef = FirebaseAuth.getInstance();
        DatabaseReference ref = firebase.getReference(ITEM_LOCATIONS);
        GeoFire geoFire = new GeoFire(ref);
        GeoLocation geoLocation = new GeoLocation(location.latitude, location.longitude);
        String currentUserId = authRef.getCurrentUser().getUid();
        String keyCode = currentUserId +":"+song.toString();
        geoFire.setLocation(String.valueOf(keyCode.hashCode()), geoLocation, new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                Log.d("test", String.valueOf(key));
                DatabaseReference itemInfo = firebase.getReference(ITEM_INFOS+"/"+key);
                itemInfo.setValue(
                        new DBStoreSong(song.getSongName(), song.getArtistName(), currentUserId)
                );
            }
        });
    }

    public void querySongWithKey(String key, Callback callback) {
        String dbPath = ITEM_INFOS+"/"+key;
        getDataOnce(dbPath, callback);
    }


    public GeoQuery querySongMarkerWithLocation(LatLng location, Callback callback) {
        DatabaseReference ref = firebase.getReference(ITEM_LOCATIONS);
        GeoFire geoFire = new GeoFire(ref);
        GeoLocation geoLocation = new GeoLocation(location.latitude, location.longitude);
        GeoQuery query = geoFire.queryAtLocation(geoLocation, 20);
        HashMap<String, SongMarker> songMarkers = new HashMap<String, SongMarker>();
        query.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {

                getDataOnce(ITEM_INFOS+"/"+key, new Callback() {
                    @Override
                    public void onSuccess(Object obj) {
                        HashMap<String, String> data = (HashMap<String, String>) obj;
                        SongMarker newSongMarker = new SongMarker(
                                new LatLng(location.latitude, location.longitude),
                                new Song(data.get("songName"), data.get("artistName"))
                                );
                        Pair<String, SongMarker> returnPair = new Pair<>(key, newSongMarker);
                        callback.onSuccess(returnPair);
                    }

                    @Override
                    public void onFailure(Object e) {

                    }
                });
            }

            @Override
            public void onKeyExited(String key) {
                callback.onFailure(key);
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
        return query;
    }

}
