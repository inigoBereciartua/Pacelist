package com.ibereciartua.connector.soundcloud;

import com.ibereciartua.commons.domain.Playlist;

import java.util.List;
import java.util.stream.Collectors;

public record SoundCloudPlaylist(PlaylistData playlist) {

    public static SoundCloudPlaylist from(Playlist playlist) {
        String title = playlist.name();
        String description = "Created with Pacelist";
        String sharing = "private";
        List<Track> tracks = playlist.songIds().stream()
                .map(id -> new Track(Integer.parseInt(id)))
                .collect(Collectors.toList());

        return new SoundCloudPlaylist(new PlaylistData(title, description, sharing, tracks));
    }

    public record PlaylistData(String title, String description, String sharing, List<Track> tracks) {}

    public record Track(int id) {}
}
