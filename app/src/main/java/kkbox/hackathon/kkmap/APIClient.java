package kkbox.hackathon.kkmap;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;

import com.kkbox.openapideveloper.auth.Auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import kkbox.hackathon.kkmap.model.Albums;
import kkbox.hackathon.kkmap.model.Artists;
import kkbox.hackathon.kkmap.model.Search.Search;
import kkbox.hackathon.kkmap.model.Tracks;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static final int CONNECTION_TIMEOUT = 60000;
    private static final int SOCKET_TIMEOUT = 60000;

    public static class KKBOXSearchType{
        public static final String TRACK    = "track";
        public static final String ALBUM    = "album";
        public static final String ARTIST   = "artist";
        public static final String PLAYLIST = "playlist";
    }

    public static class KKBOXSearchTerritory{
        public static final String TW = "TW";
        public static final String HK = "HK";
        public static final String SG = "SG";
        public static final String MY = "MY";
        public static final String JP = "JP";
    }

    public static APIInterface createApiClient() {
        return createApiClient(null);
    }

    public static APIInterface createApiClient(final String authorization) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(SOCKET_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(SOCKET_TIMEOUT, TimeUnit.SECONDS);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request request;
                if(TextUtils.isEmpty(authorization)) {
                    request = original.newBuilder()
                            .method(original.method(), original.body())
                            .build();
                }else{
                    request = original.newBuilder()
                            .header("Authorization", authorization)
                            .method(original.method(), original.body())
                            .build();
                }

                return chain.proceed(request);
            }
        });
        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.kkbox.com/v1.1/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(APIInterface.class);
    }

    public static String getKKBOXBearerToken(Context context) {
        try {
            String access_token = new Auth(context.getString(R.string.kkbox_id),
                    context.getString(R.string.kkbox_secret),
                    context).getClientCredentialsFlow()
                    .fetchAccessToken().get().get("access_token").getAsString();
            return "Bearer " + access_token;
        }catch (ExecutionException ee){
        }catch (InterruptedException ie){
        }
        return "";
    }

    public static String getKKBOXToken(Context context) {
        try {
            String access_token = new Auth(context.getString(R.string.kkbox_id),
                    context.getString(R.string.kkbox_secret),
                    context).getClientCredentialsFlow()
                    .fetchAccessToken().get().get("access_token").getAsString();
            return access_token;
        }catch (ExecutionException ee){
        }catch (InterruptedException ie){
        }
        return "";
    }

    public static void getKKBOXSearch(Context context, String keyword, String type, String territory, int offset, int limit, @Nullable ContentLoadingProgressBar progressBar, Callback callback) {
        if(progressBar!=null) progressBar.show();
        APIInterface apiService = createApiClient(getKKBOXBearerToken(context));

        Map<String, String> params = new HashMap();
        params.put("q", keyword);
        params.put("type", type);
        params.put("territory", territory);
        params.put("offset", String.valueOf(offset*limit));
        params.put("limit", String.valueOf(limit));

        Call<Search> call = apiService.getKKBOXSearch("search", params);
        call.enqueue(new retrofit2.Callback<Search>() {
            @Override
            public void onResponse(Call<Search> call, retrofit2.Response<Search> response) {
                if(progressBar!=null) progressBar.hide();
                if (response == null || response.body() == null) return;
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Search> call, Throwable t) {
                if(progressBar!=null) progressBar.hide();
                callback.onFailed();
            }
        });
    }

    public static void getKKBOXTracks(Context context, String trackId, String territory, Callback callback) {
        APIInterface apiService = createApiClient(getKKBOXBearerToken(context));

        Map<String, String> params = new HashMap();
        params.put("territory", territory);

        Call<Tracks> call = apiService.getKKBOXTracks("tracks/"+trackId, params);
        call.enqueue(new retrofit2.Callback<Tracks>() {
            @Override
            public void onResponse(Call<Tracks> call, retrofit2.Response<Tracks> response) {
                if (response == null || response.body() == null) return;
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Tracks> call, Throwable t) {
                callback.onFailed();
            }
        });
    }

    public static void getKKBOXAlbums(Context context, String albumId, String territory, Callback callback) {
        APIInterface apiService = createApiClient(getKKBOXBearerToken(context));

        Map<String, String> params = new HashMap();
        params.put("territory", territory);

        Call<Albums> call = apiService.getKKBOXAlbums("albums/"+albumId, params);
        call.enqueue(new retrofit2.Callback<Albums>() {
            @Override
            public void onResponse(Call<Albums> call, retrofit2.Response<Albums> response) {
                if (response == null || response.body() == null) return;
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Albums> call, Throwable t) {
                callback.onFailed();
            }
        });
    }

    public static void getKKBOXArtists(Context context, String artistId, String territory, Callback callback) {
        APIInterface apiService = createApiClient(getKKBOXBearerToken(context));

        Map<String, String> params = new HashMap();
        params.put("territory", territory);

        Call<Artists> call = apiService.getKKBOXArtists("artists/"+artistId, params);
        call.enqueue(new retrofit2.Callback<Artists>() {
            @Override
            public void onResponse(Call<Artists> call, retrofit2.Response<Artists> response) {
                if (response == null || response.body() == null) return;
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Artists> call, Throwable t) {
                callback.onFailed();
            }
        });
    }

    public interface Callback {
        void onSuccess(@Nullable Object obj);

        void onUnSuccess(int stateCode, String reason);

        void onFailed();
    }
}
