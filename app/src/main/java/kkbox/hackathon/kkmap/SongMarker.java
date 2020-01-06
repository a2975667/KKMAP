package kkbox.hackathon.kkmap;

import com.google.android.gms.maps.model.LatLng;

import kkbox.hackathon.kkmap.model.Song;

public class SongMarker {

    private LatLng location;
    private Song song;

    public SongMarker(LatLng location, Song song){
        this.song = song;
        this.location = location;
    }
    public LatLng getLocation(){
        return location;
    };
    public Song getSong(){
        return song;
    }

}
