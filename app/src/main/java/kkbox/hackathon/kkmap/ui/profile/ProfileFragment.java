package kkbox.hackathon.kkmap.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.JsonObject;
import com.kkbox.openapideveloper.auth.Auth;
import com.koushikdutta.ion.future.ResponseFuture;

import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;

import java.io.IOException;

import kkbox.hackathon.kkmap.Authenticator;
import kkbox.hackathon.kkmap.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    public OkHttpClient client;
    public static final MediaType CONTENT_TYPE = MediaType.get("application/x-www-form-urlencoded");
    String apUrl = "https://account.kkbox.com/oauth2/token/";       // replace host url through your oauth2 server.


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
        doAuthorization();
        return root;
    }

//    private void authenticateKKBOX() {
//        Auth auth = new Auth("bd5b3a9a2fd7ae2167f9b51630711ce2",
//                "8588e290e06128a4dad92d7ef350796f", this.getActivity());
//        ResponseFuture<JsonObject> accessToken = auth.getClientCredentialsFlow().fetchAccessToken();
//        try{
//            String token = accessToken.get().get("access_token").toString();
//            Log.d("TAG", token);
//        } catch(Exception e){
//
//        }
//    }


    private AuthorizationRequest authenticateKKBOX(){
        AuthorizationServiceConfiguration serviceConfig =
                new AuthorizationServiceConfiguration(
                        Uri.parse("https://account.kkbox.com/oauth2/authorize"), // authorization endpoint
                        Uri.parse("https://account.kkbox.com/oauth2/token")); // token endpoint

        AuthorizationRequest.Builder authRequestBuilder =
                new AuthorizationRequest.Builder(
                        serviceConfig, // the authorization service configuration
                        "bd5b3a9a2fd7ae2167f9b51630711ce2", // the client ID, typically pre-registered and static
                        ResponseTypeValues.CODE, // the response_type value: we want a code
                        Uri.parse("http://com.example.app")); // the redirect URI to which the auth response is sent

        AuthorizationRequest authRequest = authRequestBuilder.build();
        return authRequest;
    }


    private void doAuthorization() {
        AuthorizationRequest authRequest = authenticateKKBOX();
        AuthorizationService authService = new AuthorizationService(this.getActivity());
        Intent authIntent = authService.getAuthorizationRequestIntent(authRequest);
        startActivityForResult(authIntent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            AuthorizationResponse resp = AuthorizationResponse.fromIntent(data);
            AuthorizationException ex = AuthorizationException.fromIntent(data);
            // ... process the response or exception ...
        } else {
            // ...
        }
    }
}