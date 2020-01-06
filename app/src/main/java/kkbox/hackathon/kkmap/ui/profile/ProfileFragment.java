package kkbox.hackathon.kkmap.ui.profile;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import kkbox.hackathon.kkmap.APIClient;
import kkbox.hackathon.kkmap.APIInterface;
import kkbox.hackathon.kkmap.MainActivity;
import kkbox.hackathon.kkmap.R;
import kkbox.hackathon.kkmap.model.Tracks;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private SharedPreferencesRepository mSharedPrefRep;
    public static String Token;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        profileViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        final Button loginButton = root.findViewById(R.id.login_kkbox_btn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        final Button fakeButton = root.findViewById(R.id.fake_btn);
        fakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getToken();
            }
        });

        //doAuthorization();
        return root;
    }

    public void Login() {
        AuthManager authManager = AuthManager.getInstance(this.getActivity());
        AuthorizationService authService = authManager.getAuthService();
        Auth auth = authManager.getAuth();

        AuthorizationRequest authRequest = new AuthorizationRequest
                .Builder(
                authManager.getAuthConfig(),
                auth.getClientId(),
                auth.getResponseType(),
                Uri.parse(auth.getRedirectUri())).build();

        Intent authIntent = new Intent(this.getActivity(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.getActivity(), authRequest.hashCode(), authIntent, 0);

        authService.performAuthorizationRequest(
                authRequest,
                pendingIntent);


    }


    private final OkHttpClient client = new OkHttpClient();

    public void getToken(){
        mSharedPrefRep = new SharedPreferencesRepository(this.getActivity());
        AuthState authState = mSharedPrefRep.getAuthState();

        if (authState != null){
            Token = authState.getIdToken();
            Log.d("Token:", authState.getAccessToken());

            Request request = new Request.Builder()
                    .url("https://api.kkbox.com/v1.1/me")
                    //This adds the token to the header.
                    .addHeader("Authorization", "Bearer " + Token)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                Log.d("Server: ",response.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
