package com.merteroglu.unsplashandgooglemaps.Models;

/**
 * Created by Mert on 4.12.2017.
 */

public class Images {
    String id;
    String color;
    String description;
    Urls urls;
    Location location;
    String created_at;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getLatitude() {
        return location.getPosition().getLatitude();
    }
    public String getLongitude() {
        return location.getPosition().getLongitude();
    }

    public String getURL(){return urls.getSmall();}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Urls getUrls() {
        return urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}


