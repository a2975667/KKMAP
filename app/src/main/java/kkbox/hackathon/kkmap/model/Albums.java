package kkbox.hackathon.kkmap.model;

import androidx.annotation.Keep;

import java.util.ArrayList;

/**
 * Created by Broccoli.Huang on 2018/1/5.
 */

@Keep
public class Albums {
    public String id;
    public String explicitness;
    public String name;
    public String release_date;
    public ArrayList<Images> images;
    public ArrayList<String> available_territories;
    public Artists artist;
    public String url;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getExplicitness ()
    {
        return explicitness;
    }

    public void setExplicitness (String explicitness)
    {
        this.explicitness = explicitness;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getRelease_date ()
    {
        return release_date;
    }

    public void setRelease_date (String release_date)
    {
        this.release_date = release_date;
    }

    public ArrayList<Images> getImages ()
    {
        return images;
    }

    public void setImages (ArrayList<Images> images)
    {
        this.images = images;
    }

    public ArrayList<String> getAvailable_territories ()
    {
        return available_territories;
    }

    public void setAvailable_territories (ArrayList<String> available_territories)
    {
        this.available_territories = available_territories;
    }

    public Artists getArtist()
    {
        return artist;
    }

    public void setArtist(Artists artist)
    {
        this.artist = artist;
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
        return "ClassPojo [id = "+id+", explicitness = "+explicitness+", name = "+name+", release_date = "+release_date+", images = "+images+", available_territories = "+available_territories+", artist = "+ artist +", url = "+url+"]";
    }

}