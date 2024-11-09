package com.ibereciartua.commons.domain;

import java.util.List;

public class Playlist {
    private String name;
    private List<String> songIds;

    public Playlist() {}

    public Playlist(String name, List<String> songIds) {
        this.name = name;
        this.songIds = songIds;
    }

    public String getName() { return name; }
    public List<String> getSongIds() { return songIds; }

    public void setName(String name) {
        this.name = name;
    }

    public void setSongIds(List<String> songIds) {
        this.songIds = songIds;
    }
}
