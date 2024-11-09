package com.ibereciartua.domain;

import com.ibereciartua.commons.domain.Playlist;

import java.util.List;

public class SpotifyPlaylist extends Playlist {
    private final boolean visible = false;
    private final boolean collaborative = false;

    public SpotifyPlaylist(final String name, final List<String> songIds) {
        super(name, songIds);
    }
}
