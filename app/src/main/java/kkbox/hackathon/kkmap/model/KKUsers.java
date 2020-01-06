package kkbox.hackathon.kkmap.model;

import androidx.annotation.Keep;

import java.util.ArrayList;

@Keep
public class KKUsers {
    public String id;
    public String name;
    public Image profilePic;
    public String profileDescription;
    public String url;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getProfileDescription ()
    {
        return profileDescription;
    }

    public void setProfileDescription (String text)
    {
        this.profileDescription = text;
    }

    public Image getImages ()
    {
        return profilePic;
    }

    public void setImages (Image images)
    {
        this.profilePic = images;
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
        return "ClassPojo [id = "+id+", name = "+name+", images = "+profilePic+", url = "+url+"]";
    }

}