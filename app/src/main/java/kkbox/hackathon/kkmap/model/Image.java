package kkbox.hackathon.kkmap.model;

import androidx.annotation.Keep;

/**
 * Created by Broccoli.Huang on 2018/1/5.
 */

@Keep
public class Image {
    public String height;
    public String width;
    public String url;

    public String getHeight ()
    {
        return height;
    }

    public void setHeight (String height)
    {
        this.height = height;
    }

    public String getWidth ()
    {
        return width;
    }

    public void setWidth (String width)
    {
        this.width = width;
    }

    public String getUrl ()
    {
        return url;
    }

    public void setUrl (String url)
    {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [height = "+height+", width = "+width+", url = "+url+"]";
    }

}