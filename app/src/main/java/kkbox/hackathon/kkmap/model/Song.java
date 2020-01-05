package kkbox.hackathon.kkmap.model;

import androidx.annotation.NonNull;

public class Song {

    private String kkid;
    private String songName;
    private String artistName;

    public Song(String songData) {
        String[] data = songData.split(":");
        this.songName = data[0];
        this.artistName = data[1];
    }
    public Song (String songName, String artistName) {
        this.songName = songName;
        this.artistName = artistName;
    }

    public String getSongName() {
        return songName;
    }

    public String getArtistName() {
        return artistName;
    }

    @NonNull
    @Override
    public String toString() {
        return artistName + ":" + songName;
    }

    public Song fromString(String songData) {
        return new Song(songData);
    }
}
