package com.ibereciartua.app.service;

import com.ibereciartua.app.domain.NewPlaylistRequest;
import com.ibereciartua.commons.domain.Playlist;
import com.ibereciartua.app.domain.PlaylistResponse;
import com.ibereciartua.commons.domain.Song;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlaylistService {

    private final SpotifyService spotifyService;
    private final AuthService authService;

    private static final double PLAYLIST_EXTRA_MINUTES_COEFFICIENT = 2.4;
    private static final double STRIDE_LENGTH_COEFFICIENT = 0.413;
    private static final int BPM_THRESHOLD = 10;

    public PlaylistService(SpotifyService spotifyService, AuthService authService) {
        this.spotifyService = spotifyService;
        this.authService = authService;
    }

    public PlaylistResponse getPlaylistProposal(float paceInMinPerKm, float distance, float height) {
        if (paceInMinPerKm <= 0 || distance <= 0 || height <= 0) {
            throw new IllegalArgumentException("Invalid parameters");
        }
        int bpm = calculateBPM(paceInMinPerKm, height);
        float durationInSeconds = paceInMinPerKm * distance * 60;
        String token = authService.getAccessToken();
        int cumulativeDuration = 0;
        int offset = 0;
        int neededDurationInSeconds = (int) (durationInSeconds * PLAYLIST_EXTRA_MINUTES_COEFFICIENT);
        List<Song> songs = new ArrayList<>();

        while (cumulativeDuration < neededDurationInSeconds) {
            List<Song> newFetchedSongs = spotifyService.getSongs(token, offset);
            if (newFetchedSongs.isEmpty()) {
                break;
            }
            offset += newFetchedSongs.size();
            List<Song> bpmFilteredSongs = newFetchedSongs.stream().filter(song -> Math.abs(song.bpm() - bpm) <= BPM_THRESHOLD).toList();
            for (Song song : bpmFilteredSongs) {
                if (cumulativeDuration > neededDurationInSeconds) {
                    break;
                }
                cumulativeDuration += song.duration();
                songs.add(song);
            }
        }
        if (songs.isEmpty()) {
            throw new RuntimeException("No songs found");
        }

        String name = "Running Session - %skm - %smin/km - %s BPM".formatted(distance, paceInMinPerKm, bpm);
        PlaylistResponse response = new PlaylistResponse();
        response.setName(name);
        response.setBpm(bpm);
        response.setNeededDurationInSeconds((int) durationInSeconds);
        response.setSongs(songs);

        return response;
    }

    public static int calculateBPM(double paceInMinPerKm, double height) {
        if (paceInMinPerKm <= 0 || height <= 0) {
            throw new IllegalArgumentException("Invalid parameters");
        }
        double metersPerMinute = 1000 / paceInMinPerKm;
        double strideLength = STRIDE_LENGTH_COEFFICIENT * (height / 100);
        double cadence = metersPerMinute / strideLength;
        return (int) Math.round(cadence / 2);
    }

    public void createPlaylist(NewPlaylistRequest request) {
        Playlist playlist = new Playlist(request.getName(), request.getSongIds());
        spotifyService.addPlaylist(playlist);
    }
}
