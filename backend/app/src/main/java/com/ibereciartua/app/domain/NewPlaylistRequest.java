package com.ibereciartua.app.domain;

import java.util.List;

public class NewPlaylistRequest {
    private String name;
    private List<String> songIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSongIds() {
        return songIds;
    }

    public void setSongIds(List<String> songIds) {
        this.songIds = songIds;
    }
}
