package com.ibereciartua.pacelist.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class NewPlaylist {
    private String name;
    private boolean visible;
    private boolean collaborative;
    private List<String> songIds;

    public NewPlaylist() {}

    public NewPlaylist(String name, boolean visible, boolean collaborative, List<String> songIds) {
        this.name = name;
        this.visible = visible;
        this.collaborative = collaborative;
        this.songIds = songIds;
    }

    public String getName() { return name; }
    public boolean isVisible() { return visible; }
    public boolean isCollaborative() { return collaborative; }
    public List<String> getSongIds() { return songIds; }

    public void setName(String name) {
        this.name = name;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setCollaborative(boolean collaborative) {
        this.collaborative = collaborative;
    }

    public void setSongIds(List<String> songIds) {
        this.songIds = songIds;
    }
}
