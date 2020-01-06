package kkbox.hackathon.kkmap;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.ClientSecretPost;
import net.openid.appauth.TokenRequest;
import net.openid.appauth.TokenResponse;

import java.util.Set;

import kkbox.hackathon.kkmap.model.Song;
import kkbox.hackathon.kkmap.ui.main.MainViewModel;
import kkbox.hackathon.kkmap.ui.profile.AuthManager;
import kkbox.hackathon.kkmap.ui.profile.ProfileFragment;
import kkbox.hackathon.kkmap.ui.profile.TokenService;

import static kkbox.hackathon.kkmap.ui.map.MapFragment.REQUEST;

public class MainActivity extends AppCompatActivity {
    private String PACKAGE_NAME = "kkbox.hackathon.kkmap";
    private AppCompatActivity activity = this;
    private AuthorizationService mAuthService;
    private void askForNotificationPermission() {
        String alertTitle = "Permission required";
        String alertMessage = "Please granted this app notification access.";
        AlertDialog permissionAlert = new AlertDialog.Builder(this)
                .setTitle(alertTitle)
                .setMessage(alertMessage)
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_main, R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter(AudioService.AUDIOSERVICE_KEY));
        Integer now_page = 0;

        final AuthorizationResponse resp = AuthorizationResponse.fromIntent(getIntent());

        if(resp != null) {
            Log.d("testing", "here");
            AuthorizationException ex = AuthorizationException.fromIntent(getIntent());

            final AuthManager authManager = AuthManager.getInstance(this);
            authManager.setAuthState(resp,ex);

            if (resp != null) {

                ClientSecretPost clientSecretPost = new ClientSecretPost(authManager.getAuth().getClientSecret());
                TokenRequest tokenRequest = new TokenRequest
                        .Builder(authManager.getAuthConfig(), authManager.getAuth().getClientId())
                        .setAuthorizationCode(resp.authorizationCode)
                        .setRedirectUri(Uri.parse(authManager.getAuth().getRedirectUri()))
                        .build();

                mAuthService = authManager.getAuthService();

                mAuthService.performTokenRequest(tokenRequest, clientSecretPost, new AuthorizationService.TokenResponseCallback() {
                    @Override public void onTokenRequestCompleted(@Nullable TokenResponse response, @Nullable AuthorizationException ex) {
                        if(ex == null) {
                            authManager.updateAuthState(response,ex);
                            ProfileFragment.Token = authManager.getAuthState().getIdToken();
                            startService(new Intent(MainActivity.this, TokenService.class));
                            Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                        }
                        else{
                            Intent loginIntent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(loginIntent);
                            finish();
                        }
                    }
                });

                // authorization completed
            } else {
                // authorization failed, check ex for more details
                Intent loginIntent = new Intent(MainActivity.this,  MainActivity.class);
                startActivity(loginIntent);
                finish();
            }
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent openMainActivity= new Intent(MainActivity.this, MainActivity.class);
                    openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(openMainActivity);
                } else {
                    Toast.makeText(this, "The app was not allowed to access your location", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            Log.d("TAG", "GET DATA");
            String song = intent.getStringExtra(AudioService.AUDIOSERVICE_SONG_KEY);
            String artist = intent.getStringExtra(AudioService.AUDIOSERVICE_ARTIST_KEY);
            Song newSong = new Song(song, artist);
            MainViewModel model = ViewModelProviders.of(activity).get(MainViewModel.class);
            model.setSong(newSong);
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        Set<String> notificationPackageNames = NotificationManagerCompat.getEnabledListenerPackages(this);
        Log.d("THIS", notificationPackageNames.toString());
        if (notificationPackageNames.contains(PACKAGE_NAME)) {
            Intent intent = new Intent(this, AudioService.class);
            this.startService(intent);
        } else {
            askForNotificationPermission();
        }
    }
}
