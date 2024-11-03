package com.ibereciartua.pacelist.domain;

import java.time.LocalDateTime;

public class Song {

    private String id;
    private String title;
    private String artist;
    private String album;
    private String picture;
    private LocalDateTime playedDate;
    private int duration;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public LocalDateTime getPlayedDate() {
        return playedDate;
    }

    public void setPlayedDate(LocalDateTime playedDate) {
        this.playedDate = playedDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
