package kkbox.hackathon.kkmap.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import kkbox.hackathon.kkmap.model.Song;

public class MainViewModel extends ViewModel {

    private MutableLiveData<Song> mSong;
    public MainViewModel() {
        mSong = new MutableLiveData<>();
        mSong.setValue(new Song("nothing", "no one"));
    }
    public void setSong(Song song) {
        mSong.setValue(song);
    }
    public LiveData<Song> getSong() {
        return mSong;
    }

}