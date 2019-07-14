package com.parse.starter;

import android.graphics.Bitmap;

public class Report {
    private String id;
    private Bitmap imageUrl;
    private String date;
    private String location;
    private String goal;
    private String activities;
    private Double latitude;
    private Double longitude;


    public Report(String id, Bitmap imageUrl, String date, String location, String goal, String activities, Double latitude, Double longitude) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.date = date;
        this.location = location;
        this.goal = goal;
        this.activities = activities;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Bitmap getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Bitmap imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
