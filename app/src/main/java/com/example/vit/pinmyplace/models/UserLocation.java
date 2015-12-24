package com.example.vit.pinmyplace.models;

import com.orm.SugarRecord;

public class UserLocation extends SugarRecord {

    String facebookId;
    String locationTitle;
    String locationDescription;
    double lat;
    double lng;
    long createdAt;

    //Sugar ORM required empty constructor
    public UserLocation(){
    }

    public UserLocation(String facebookId){
        createdAt = System.currentTimeMillis();
        this.facebookId = facebookId;
    }

    public UserLocation(String title, double lat, double lng){
        this.locationTitle = title;
        this.lat = lat;
        this.lng = lng;
    }

    public void setFacebookId(String id){
        this.facebookId = id;
    }

    public void setLocationTitle(String title){
        this.locationTitle = title;
    }

    public void setLocationDescription(String description){
        this.locationDescription = description;
    }

    public void setLat(double lat){
        this.lat = lat;
    }

    public void setLng(double lng){
        this.lng = lng;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public String getLocationTitle(){
        return locationTitle;
    }

    public String getLocationDescription(){
        return locationDescription;
    }

    public double getLat(){
        return this.lat;
    }

    public double getLng(){
        return this.lng;
    }

    public long getCreatedAt(){
        return this.createdAt;
    }

    @Override
    public String toString() {
        return facebookId + " - " + locationTitle + " [" + locationDescription +"] " + lat + ", " + lng;
    }
}
