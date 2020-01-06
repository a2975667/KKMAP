package kkbox.hackathon.kkmap.ui.profile;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import net.openid.appauth.AuthState;

import org.json.JSONException;


/**
 * Created by Hadi on 6/25/2017.
 */

public class SharedPreferencesRepository{

    private Context mContext;

    public SharedPreferencesRepository(Context context){
        mContext = context;
    }


    public void saveAuthState(AuthState authState) {
        PreferenceManager.getDefaultSharedPreferences(mContext).edit().putString("AuthState",authState.jsonSerializeString()).apply();
    }

    public AuthState getAuthState() {
        String authStateString =  PreferenceManager.getDefaultSharedPreferences(mContext).getString("AuthState",null);
        if(authStateString != null){
            try {
                return AuthState.jsonDeserialize(authStateString);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;

    }


}