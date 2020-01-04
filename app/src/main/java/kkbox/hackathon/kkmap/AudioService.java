package kkbox.hackathon.kkmap;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.List;

import static android.content.ContentValues.TAG;

public class AudioService extends NotificationListenerService {
    public AudioService() {
    }
    public final static String AUDIOSERVICE_KEY = "AUDIOSERVICE_KEY";
    public final static String AUDIOSERVICE_ARTIST_KEY = "AUDIOSERVICE_ARTIST_KEY";
    public final static String AUDIOSERVICE_SONG_KEY = "AUDIOSERVICE_SONG_KEY";

    MediaSessionManager mediaSessionManager;
    ComponentName componentName;
    MediaController controller;
    public String currentArtist = "";
    public String currentSong = "";
    MediaSessionManager.OnActiveSessionsChangedListener sessionsChangedListener = new MediaSessionManager.OnActiveSessionsChangedListener() {
        @Override
        public void onActiveSessionsChanged(@Nullable List<MediaController> controllers) {
            Log.d(TAG, "onActiveSessionsChanged: session is changed");
            for (MediaController controller : controllers) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Log.d(TAG, "onActiveSessionsChanged: controller = " + controller.getPackageName());
                    MediaMetadata meta = controller.getMetadata();
                    currentArtist = meta.getString(MediaMetadata.METADATA_KEY_ARTIST);
                    currentSong =  meta.getString(MediaMetadata.METADATA_KEY_TITLE);

                    Log.d(TAG, "onCreate: artist = " + currentArtist);
                    Log.d(TAG, "onCreate: song = " + currentSong);
                    sendMessageToActivity();
                }
            }
        }
    };

    private void sendMessageToActivity() {
        Intent intent = new Intent(this.AUDIOSERVICE_KEY);
        intent.putExtra(this.AUDIOSERVICE_ARTIST_KEY, this.currentArtist);
        intent.putExtra(this.AUDIOSERVICE_SONG_KEY, this.currentSong);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        // Implement what you want here
        Log.d("TEST", sbn.getPackageName());
        List<MediaController> controllers = mediaSessionManager.getActiveSessions(componentName);
        Log.d(TAG, "onCreate listener: controllers size = " + controllers.size());
        for (MediaController mediaController : controllers) {
            controller = mediaController;
            Log.d(TAG, "onSession: controller = " + controller.getPackageName());
            MediaMetadata meta = controller.getMetadata();
            currentArtist = meta.getString(MediaMetadata.METADATA_KEY_ARTIST);
            currentSong =  meta.getString(MediaMetadata.METADATA_KEY_TITLE);

            Log.d(TAG, "onSession: artist = " + currentArtist);
            Log.d(TAG, "onSession: song = " + currentSong);
            sendMessageToActivity();

        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn){
        // Implement what you want here
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "TEST");

        componentName = new ComponentName(this, AudioService.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            mediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
            mediaSessionManager.addOnActiveSessionsChangedListener(sessionsChangedListener, componentName);

            List<MediaController> controllers = mediaSessionManager.getActiveSessions(componentName);
            Log.d(TAG, "onCreate listener: controllers size = " + controllers.size());
            for (MediaController mediaController : controllers) {
                controller = mediaController;
                Log.d(TAG, "onSession: controller = " + controller.getPackageName());
                MediaMetadata meta = controller.getMetadata();
                currentArtist = meta.getString(MediaMetadata.METADATA_KEY_ARTIST);
                currentSong =  meta.getString(MediaMetadata.METADATA_KEY_TITLE);

                Log.d(TAG, "onSession: artist = " + currentArtist);
                Log.d(TAG, "onSession: song = " + currentSong);
                sendMessageToActivity();

            }
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }
}
