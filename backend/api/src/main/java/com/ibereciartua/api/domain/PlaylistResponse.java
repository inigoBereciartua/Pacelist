package com.ibereciartua.api.domain;

import com.ibereciartua.commons.domain.Song;

import java.util.List;

public class PlaylistResponse {

    private String name;
    private int bpm;
    private int neededDurationInSeconds;
    private List<Song> songs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public int getNeededDurationInSeconds() {
        return neededDurationInSeconds;
    }

    public void setNeededDurationInSeconds(int neededDurationInSeconds) {
        this.neededDurationInSeconds = neededDurationInSeconds;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
}
