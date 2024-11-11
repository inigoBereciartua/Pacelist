package com.ibereciartua.connector.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ibereciartua.commons.domain.Playlist;

import java.util.List;

public record SpotifyPlaylist (
    String name,
    List<String> songIds,
    @JsonProperty("public")
    boolean visible,
    boolean collaborative
) {
    public static SpotifyPlaylist from(Playlist playlist) {
        return new SpotifyPlaylist(playlist.name(), playlist.songIds(), false, false);
    }
}
