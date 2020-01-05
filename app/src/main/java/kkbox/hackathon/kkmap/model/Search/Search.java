package kkbox.hackathon.kkmap.model.Search;

import androidx.annotation.Keep;
/**
 * Created by Broccoli.Huang on 2018/1/6.
 */

@Keep
public class Search {
    public SearchSummary summary;
    public SearchTracks tracks;
    public SearchArtists artists;
    public SearchAlbums albums;
    public SearchPaging paging;

    public SearchSummary getSummary ()
    {
        return summary;
    }

    public void setSummary (SearchSummary summary)
    {
        this.summary = summary;
    }

    public SearchTracks getTracks ()
    {
        return tracks;
    }

    public void setTracks (SearchTracks tracks)
    {
        this.tracks = tracks;
    }

    public SearchArtists getArtists ()
    {
        return artists;
    }

    public void setArtists (SearchArtists artists)
    {
        this.artists = artists;
    }

    public SearchAlbums getAlbums ()
    {
        return albums;
    }

    public void setAlbums (SearchAlbums albums)
    {
        this.albums = albums;
    }

    public SearchPaging getPaging ()
    {
        return paging;
    }

    public void setPaging (SearchPaging paging)
    {
        this.paging = paging;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [summary = "+summary+", tracks = "+tracks+", paging = "+paging+"]";
    }
}