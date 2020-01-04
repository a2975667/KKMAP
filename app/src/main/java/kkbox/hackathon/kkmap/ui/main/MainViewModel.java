package kkbox.hackathon.kkmap.ui.main;

import android.media.MediaPlayer;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private MutableLiveData<String> mSong;
    private MutableLiveData<String> mArtist;
    public MainViewModel() {
        mArtist = new MutableLiveData<>();
        mSong = new MutableLiveData<>();
        mArtist.setValue("Please play some music~");
        mSong.setValue("No One");
    }
    public void setSong(String str) {
        mSong.setValue(str);
    }
    public void setArtist(String str) {
        mArtist.setValue(str);
    }
    public LiveData<String> getSong() {
        return mSong;
    }
    public LiveData<String> getArtist() {
        return mArtist;
    }

}