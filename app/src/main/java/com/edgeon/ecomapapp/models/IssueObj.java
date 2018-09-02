package com.edgeon.ecomapapp.models;

/**
 * Created by Osama on 12/15/2017.
 */

public class IssueObj  {

    public IssueObj(String description, String issueType, String issueArea, String lat, String lng, String user, String picture) {
        this.description = description;
        this.issueType = issueType;
        this.issueArea = issueArea;
        this.lat = lat;
        this.lng = lng;
        this.user = user;
        this.picture = picture;
    }

    public IssueObj() {

    }

    String description;
    String issueType;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    String picture;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    String user;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getIssueArea() {
        return issueArea;
    }

    public void setIssueArea(String issueArea) {
        this.issueArea = issueArea;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    String issueArea;
    String lat;
    String lng;

}
