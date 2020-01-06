package kkbox.hackathon.kkmap.model.Search;

import androidx.annotation.Keep;

import java.util.ArrayList;

import kkbox.hackathon.kkmap.model.Artists;
import kkbox.hackathon.kkmap.model.KKUsers;

/**
 * Created by Broccoli.Huang on 2018/1/7.
 */

@Keep
public class SearchMe {
    public SearchSummary summary;
    public KKUsers me;
    public SearchPaging paging;

    public SearchSummary getSummary ()
    {
        return summary;
    }

    public void setSummary (SearchSummary summary)
    {
        this.summary = summary;
    }

    public KKUsers getData ()
    {
        return me;
    }

    public void setData (KKUsers data)
    {
        this.me = data;
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
        return "ClassPojo [summary = "+summary+", data = "+me+", paging = "+paging+"]";
    }
}