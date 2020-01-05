package kkbox.hackathon.kkmap.model.Search;

import androidx.annotation.Keep;

/**
 * Created by Broccoli.Huang on 2018/1/6.
 */

@Keep
public class SearchSummary {
    public String total;

    public String getTotal ()
    {
        return total;
    }

    public void setTotal (String total)
    {
        this.total = total;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [total = "+total+"]";
    }

}