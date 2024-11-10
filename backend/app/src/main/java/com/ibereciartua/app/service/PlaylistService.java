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

    private static final double EXTRA_MINUTES_COEFFICIENT = 2.4;
    private static final double STRIDE_LENGTH_COEFFICIENT = 0.413;
    private static final int BPM_THRESHOLD = 10;

    public PlaylistService(SpotifyService spotifyService, AuthService authService) {
        this.spotifyService = spotifyService;
        this.authService = authService;
    }

    public PlaylistResponse getPlaylistProposal(Float paceInMinPerKm, Float distance, Float height) {
        int bpm = calculateBPM(paceInMinPerKm, height) / 2; // Half of the cadence so beats are realistic for running
        float durationInSeconds = paceInMinPerKm * distance * 60;
        String token = authService.getAccessToken();
        int cumulativeDuration = 0;
        int offset = 0;
        int neededDurationInSeconds = (int) (durationInSeconds * EXTRA_MINUTES_COEFFICIENT);
        List<Song> songs = new ArrayList<>();

        while (cumulativeDuration < neededDurationInSeconds) {
            List<Song> newFetchedSongs = spotifyService.getSongs(token, offset);
            if (newFetchedSongs.isEmpty()) {
                break;
            }
            offset += newFetchedSongs.size();
            newFetchedSongs.removeIf(song -> song.getBpm() < bpm - BPM_THRESHOLD || song.getBpm() > bpm + BPM_THRESHOLD);
            cumulativeDuration += newFetchedSongs.stream().mapToInt(Song::getDuration).sum();
            songs.addAll(newFetchedSongs);
        }
        if (songs.isEmpty()) {
            throw new RuntimeException("No songs found");
        }

        String name = "Running Session - %skm - %smin/km - %s BPM ".formatted(distance, paceInMinPerKm, bpm);
        PlaylistResponse response = new PlaylistResponse();
        response.setName(name);
        response.setBpm(bpm);
        response.setNeededDurationInSeconds((int) durationInSeconds);
        response.setSongs(songs);

        return response;
    }

    public static int calculateBPM(double paceInMinPerKm, double height) {

        double metersPerMinute = 1000 / paceInMinPerKm;
        double strideLength = STRIDE_LENGTH_COEFFICIENT * (height / 100);
        double cadence = metersPerMinute / strideLength;

        return (int) Math.round(cadence);
    }

    public void addPlaylist(NewPlaylistRequest request) {
        Playlist playlist = new Playlist();
        playlist.setName(request.getName());
        playlist.setSongIds(request.getSongIds());
        spotifyService.addPlaylist(playlist);
    }
}
