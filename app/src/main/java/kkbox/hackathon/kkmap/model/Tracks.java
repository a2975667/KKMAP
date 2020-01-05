package kkbox.hackathon.kkmap.model;

import androidx.annotation.Keep;
import java.util.ArrayList;

/**
 * Created by Broccoli.Huang on 2018/1/5.
 */

@Keep
public class Tracks {
    public String id;
    public String duration;
    public Albums album;
    public String explicitness;
    public String name;
    public ArrayList<String> available_territories;
    public String track_number;
    public String url;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getDuration ()
    {
        return duration;
    }

    public void setDuration (String duration)
    {
        this.duration = duration;
    }

    public Albums getAlbum()
    {
        return album;
    }

    public void setAlbum(Albums album)
    {
        this.album = album;
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

    public ArrayList<String> getAvailable_territories ()
    {
        return available_territories;
    }

    public void setAvailable_territories (ArrayList<String> available_territories)
    {
        this.available_territories = available_territories;
    }

    public String getTrack_number ()
    {
        return track_number;
    }

    public void setTrack_number (String track_number)
    {
        this.track_number = track_number;
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
        return "ClassPojo [id = "+id+", duration = "+duration+", album = "+ album +", explicitness = "+explicitness+", name = "+name+", available_territories = "+available_territories+", track_number = "+track_number+", url = "+url+"]";
    }

}