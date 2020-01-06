package kkbox.hackathon.kkmap.ui.profile;

import net.openid.appauth.AuthState;

import android.content.Context;
import android.net.Uri;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.BuildConfig;
import net.openid.appauth.TokenResponse;

public class AuthManager {
    private static AuthManager instance;
    private AuthState mAuthState;
    private Auth mAuth;
    private AuthorizationServiceConfiguration mAuthConfig;
    private SharedPreferencesRepository mSharedPrefRep;
    private AuthorizationService mAuthService;

    public static AuthManager getInstance(Context context) {
        if (instance == null) {
            instance = new AuthManager(context);
        }
        return instance;
    }

    private AuthManager(Context context){
        mSharedPrefRep = new SharedPreferencesRepository(context);
        setAuthData();
        mAuthConfig = new AuthorizationServiceConfiguration(
                Uri.parse(mAuth.getAuthorizationEndpointUri()),
                Uri.parse(mAuth.getTokenEndpointUri()),
                null);
        mAuthState = mSharedPrefRep.getAuthState();

        mAuthService = new AuthorizationService(context);
    }



    public AuthorizationServiceConfiguration getAuthConfig() {
        return mAuthConfig;
    }

    public Auth getAuth() {
        if(mAuth == null){
            setAuthData();
        }

        return mAuth;
    }

    public AuthState getAuthState(){
        return mAuthState;
    }

    public void updateAuthState(TokenResponse response, AuthorizationException ex){
        mAuthState.update(response,ex);
        mSharedPrefRep.saveAuthState(mAuthState);
    }

    public void setAuthState(AuthorizationResponse response, AuthorizationException ex){
        if(mAuthState == null)
            mAuthState = new AuthState(response,ex);

        mSharedPrefRep.saveAuthState(mAuthState);
    }

    public AuthorizationService getAuthService(){
        return mAuthService;
    }

    private void setAuthData(){
        mAuth = new Auth();
        mAuth.setClientId("bd5b3a9a2fd7ae2167f9b51630711ce2");
        mAuth.setAuthorizationEndpointUri("https://account.kkbox.com/oauth2/authorize");
        mAuth.setClientSecret("8588e290e06128a4dad92d7ef350796f");
        mAuth.setRedirectUri("https://kkmap-2020.firebaseapp.com");
        //mAuth.setScope(BuildConfig.SCOPE);
        mAuth.setTokenEndpointUri("https://account.kkbox.com/oauth2/token");
        mAuth.setResponseType("code");
    }
}