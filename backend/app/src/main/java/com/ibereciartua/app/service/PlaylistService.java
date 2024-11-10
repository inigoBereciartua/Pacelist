package com.ibereciartua.app.service;

import com.ibereciartua.app.domain.NewPlaylistRequest;
import com.ibereciartua.commons.domain.Playlist;
import com.ibereciartua.app.domain.PlaylistResponse;
import com.ibereciartua.commons.domain.Song;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PlaylistService {

    private final SpotifyService spotifyService;
    private final Logger logger = Logger.getLogger(PlaylistService.class.getName());

    private static final double EXTRA_MINUTES_COEFFICIENT = 2.4;
    private static final double STRIDE_LENGTH_COEFFICIENT = 0.413;
    private static final int BPM_THRESHOLD = 10;

    public PlaylistService(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    public PlaylistResponse getPlaylistProposal(float paceInMinPerKm, float distance, float height) {
        if (paceInMinPerKm <= 0 || distance <= 0 || height <= 0) {
            throw new IllegalArgumentException("Invalid parameters");
        }
        int bpm = calculateBPM(paceInMinPerKm, height) / 2; // Half of the cadence so beats are realistic for running
        float durationInSeconds = paceInMinPerKm * distance * 60;
        List<Song> songs = spotifyService.getSongs(bpm, BPM_THRESHOLD);
        if (songs.isEmpty()) {
            throw new RuntimeException("No songs found");
        }

        int neededDurationInSeconds = (int) (durationInSeconds * EXTRA_MINUTES_COEFFICIENT);
        int totalDuration = songs.stream().reduce(0, (acc, song) -> acc + song.getDuration(), Integer::sum);

        while (totalDuration > neededDurationInSeconds) {
            int indexToRemove = songs.size() - 1;
            totalDuration = totalDuration - songs.get(indexToRemove).getDuration();
            logger.info("Removing track: %s, Duration: %s".formatted(songs.get(indexToRemove).getTitle(), songs.get(indexToRemove).getDuration()));
            songs.remove(indexToRemove);
        }
        logger.info("Total duration: %s. Duration needed: %s. Duration with extra needed: %s.".formatted(totalDuration, durationInSeconds, neededDurationInSeconds));
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
