package kkbox.hackathon.kkmap.model.Search;

import androidx.annotation.Keep;

import java.util.ArrayList;

import kkbox.hackathon.kkmap.model.Tracks;

/**
 * Created by Broccoli.Huang on 2018/1/6.
 */

@Keep
public class SearchTracks {
    public SearchSummary summary;
    public ArrayList<Tracks> data;
    public SearchPaging paging;

    public SearchSummary getSummary()
    {
        return summary;
    }

    public void setSummary(SearchSummary summary)
    {
        this.summary = summary;
    }

    public ArrayList<Tracks> getData ()
    {
        return data;
    }

    public void setData (ArrayList<Tracks> data)
    {
        this.data = data;
    }

    public SearchPaging getPaging()
    {
        return paging;
    }

    public void setPaging(SearchPaging paging)
    {
        this.paging = paging;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [summary = "+ summary +", data = "+data+", paging = "+ paging +"]";
    }

}