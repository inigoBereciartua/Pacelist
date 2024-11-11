package com.ibereciartua.domain;

import com.ibereciartua.commons.domain.Playlist;

import java.util.List;

public record SpotifyPlaylist (
    String name,
    List<String> songIds,
    boolean visible,
    boolean collaborative
) {
    public static SpotifyPlaylist from(Playlist playlist) {
        return new SpotifyPlaylist(playlist.name(), playlist.songIds(), true, false);
    }
}
