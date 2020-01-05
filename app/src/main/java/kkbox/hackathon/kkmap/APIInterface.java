package kkbox.hackathon.kkmap;

import java.util.Map;

import kkbox.hackathon.kkmap.model.Albums;
import kkbox.hackathon.kkmap.model.Artists;
import kkbox.hackathon.kkmap.model.Search.Search;
import kkbox.hackathon.kkmap.model.Tracks;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface APIInterface {
    @GET
    Call<Search> getKKBOXSearch(@Url String url, @QueryMap Map<String, String> params);

    @GET
    Call<Tracks> getKKBOXTracks(@Url String url, @QueryMap Map<String, String> params);

    @GET
    Call<Albums> getKKBOXAlbums(@Url String url, @QueryMap Map<String, String> params);

    @GET
    Call<Artists> getKKBOXArtists(@Url String url, @QueryMap Map<String, String> params);
}