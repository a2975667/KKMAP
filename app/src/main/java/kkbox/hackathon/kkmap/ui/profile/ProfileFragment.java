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
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.JsonObject;
import com.koushikdutta.ion.future.ResponseFuture;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;

import java.io.IOException;

import kkbox.hackathon.kkmap.Authenticator;
import kkbox.hackathon.kkmap.MainActivity;
import kkbox.hackathon.kkmap.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

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

        final Button loginButton = (Button) root.findViewById(R.id.login_kkbox_btn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
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
                Uri.parse(auth.getRedirectUri()))
                .setScope(auth.getScope())
                .build();

        Intent authIntent = new Intent(this.getActivity(), LoginAuthActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.getActivity(), authRequest.hashCode(), authIntent, 0);

        authService.performAuthorizationRequest(
                authRequest,
                pendingIntent);

        mSharedPrefRep = new SharedPreferencesRepository(this.getActivity());
        AuthState authState = mSharedPrefRep.getAuthState();

        if (authState != null){
            Token = authState.getIdToken();
            Log.d("Token:", Token);
        }
    }

    public void logToken(){
        Log.d("Token", ProfileFragment.Token);
    }
//
//    private AuthorizationRequest authenticateKKBOX(){
//        AuthorizationServiceConfiguration serviceConfig =
//                new AuthorizationServiceConfiguration(
//                        Uri.parse("https://account.kkbox.com/oauth2/authorize"), // authorization endpoint
//                        Uri.parse("https://account.kkbox.com/oauth2/token")); // token endpoint
//
//        AuthorizationRequest.Builder authRequestBuilder =
//                new AuthorizationRequest.Builder(
//                        serviceConfig, // the authorization service configuration
//                        "bd5b3a9a2fd7ae2167f9b51630711ce2", // the client ID, typically pre-registered and static
//                        ResponseTypeValues.CODE, // the response_type value: we want a code
//                        Uri.parse("https://kkmap-2020.firebaseapp.com/")).setState("1234"); // the redirect URI to which the auth response is sent
//
//        AuthorizationRequest authRequest = authRequestBuilder.build();
//        return authRequest;
//    }
//
//
//    private void doAuthorization() {
//        AuthorizationRequest authRequest = authenticateKKBOX();
//        AuthorizationService authService = new AuthorizationService(this.getActivity());
//
//
//        Intent authIntent = authService.getAuthorizationRequestIntent(authRequest);
//        startActivityForResult(authIntent, 100);
//
//        AuthorizationService service = new AuthorizationService(this.getActivity());
//        PendingIntent pendingIntent = PendingIntent.getActivity(this.getActivity(), authRequest.hashCode(), authIntent, 0);
//        service.performAuthorizationRequest(authRequest, pendingIntent);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == 100) {
//            AuthorizationResponse resp = AuthorizationResponse.fromIntent(data);
//            AuthorizationException ex = AuthorizationException.fromIntent(data);
//        } else {
//            // ...
//        }
//    }
}