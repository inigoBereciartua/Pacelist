package com.ibereciartua.app.service;

import com.ibereciartua.commons.domain.Playlist;
import com.ibereciartua.commons.domain.Song;
import com.ibereciartua.app.domain.exception.SongSearchException;
import com.ibereciartua.connector.spotify.SpotifyConnector;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpotifyService {

    private final AuthService authService;
    private final SpotifyConnector spotifyConnector;


    public SpotifyService(AuthService authService) {
        this.authService = authService;
        this.spotifyConnector = new SpotifyConnector();
    }

    public List<Song> getSongs(final String accessToken, final int offset) {
        try {
            return spotifyConnector.getUserTracks(accessToken, offset);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SongSearchException("Error getting songs from Spotify");
        }

    }

    public void addPlaylist(Playlist request) {
        String userId = authService.getName();
        try {
            spotifyConnector.createPlaylist(authService.getAccessToken(), userId, request);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating playlist");
        }
    }
}
