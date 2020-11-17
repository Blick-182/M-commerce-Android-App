package com.example.blacklight.HomeStatic.model;

import java.util.ArrayList;

public class Shop {

    private String name;
    private String title;
    private String subtitle;
    private String coverURL;
    private String instagram;
    private String twitter;
    private String logoURL;
    private ArrayList<Pictures> pictures;

    public Shop() {
    }

    public Shop(String name, String title, String subtitle, String coverURL, String instagram, String twitter, String logoURL, ArrayList<Pictures> pictures) {
        this.name = name;
        this.title = title;
        this.subtitle = subtitle;
        this.coverURL = coverURL;
        this.instagram = instagram;
        this.twitter = twitter;
        this.logoURL = logoURL;
        this.pictures = pictures;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getCoverURL() {
        return coverURL;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public ArrayList<Pictures> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<Pictures> pictures) {
        this.pictures = pictures;
    }
}
