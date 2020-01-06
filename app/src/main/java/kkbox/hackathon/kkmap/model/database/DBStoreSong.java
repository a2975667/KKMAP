package kkbox.hackathon.kkmap.model.database;

import androidx.annotation.NonNull;

import kkbox.hackathon.kkmap.model.MusicObject;
import kkbox.hackathon.kkmap.model.Song;

public class DBStoreSong implements MusicObject {

    private String userId;
    private Song song;

    public DBStoreSong(String songData) {
        String[] data = songData.split(":");
        this.song = new Song(data[1], data[2]);
        this.userId = data[0];
    }

    public DBStoreSong(String songName, String artistName, String userId) {
        this.song = new Song(songName, artistName);
        this.userId = userId;
    }

    public DBStoreSong(Song song, String userId) {
        this.song = new Song(song.getSongName(), song.getArtistName());
        this.userId = userId;
    }

    public Song getSong() {
        return song;
    }

    public String getUserId() {return userId;}

    @NonNull
    @Override
    public String toString() {
        return userId+ ":" + song  ;
    }

    public DBStoreSong fromString(String songData) {
        return new DBStoreSong(songData);
    }

    @Override
    public String getId() {
        return String.valueOf(this.toString().hashCode());
    }
}
