package com.ibereciartua.api.service;

import com.ibereciartua.commons.domain.Playlist;
import com.ibereciartua.commons.domain.Song;
import com.ibereciartua.api.domain.exception.SongSearchException;
import com.ibereciartua.connector.SpotifyConnector;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpotifyService {

    private final AuthService authService;
    private final SpotifyConnector spotifyConnector;

    private static final int LIMIT = 300;

    public SpotifyService(AuthService authService, SpotifyConnector spotifyConnector) {
        this.authService = authService;
        this.spotifyConnector = spotifyConnector;
    }

    public List<Song> getSongs(int bpmTarget, int bpmThreshold) {
        String accessToken = authService.getAccessToken();
        try {
            return spotifyConnector.getUserTracksByBpm(accessToken, bpmTarget, bpmThreshold, LIMIT);
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