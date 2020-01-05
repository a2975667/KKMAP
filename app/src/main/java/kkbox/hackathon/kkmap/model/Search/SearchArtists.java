package kkbox.hackathon.kkmap.model.Search;

import androidx.annotation.Keep;

import java.util.ArrayList;

import kkbox.hackathon.kkmap.model.Artists;

/**
 * Created by Broccoli.Huang on 2018/1/7.
 */

@Keep
public class SearchArtists {
    public SearchSummary summary;
    public ArrayList<Artists> data;
    public SearchPaging paging;

    public SearchSummary getSummary ()
    {
        return summary;
    }

    public void setSummary (SearchSummary summary)
    {
        this.summary = summary;
    }

    public ArrayList<Artists> getData ()
    {
        return data;
    }

    public void setData (ArrayList<Artists> data)
    {
        this.data = data;
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
        return "ClassPojo [summary = "+summary+", data = "+data+", paging = "+paging+"]";
    }
}