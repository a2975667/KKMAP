package kkbox.hackathon.kkmap;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static kkbox.hackathon.kkmap.APIClient.createApiClient;
import static kkbox.hackathon.kkmap.APIClient.getKKBOXBearerToken;
import static kkbox.hackathon.kkmap.APIClient.getKKBOXToken;
import com.kkbox.openapideveloper.api.Api;

import kkbox.hackathon.kkmap.model.Search.Search;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //Api api = new Api(getKKBOXToken(this), "TW", this);
        Integer now_page = 0;

        APIClient.getKKBOXSearch(this, "五月天 溫柔", APIClient.KKBOXSearchType.TRACK, "TW", now_page, 20, null, new APIClient.Callback() {
            @Override
            public void onSuccess(@Nullable Object obj) {
                Log.d("TAG", obj.toString());
            }

            @Override
            public void onUnSuccess(int stateCode, String reason) {
                finish();
            }

            @Override
            public void onFailed() {
                finish();
            }
        });
    }
}
