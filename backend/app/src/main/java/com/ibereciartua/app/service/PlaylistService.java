package com.ibereciartua.app.service;

import com.ibereciartua.app.domain.NewPlaylistRequest;
import com.ibereciartua.app.domain.exception.SongSearchException;
import com.ibereciartua.app.domain.exception.UserCredentialsNotFoundException;
import com.ibereciartua.app.factory.MusicConnectorFactory;
import com.ibereciartua.commons.domain.Playlist;
import com.ibereciartua.app.domain.PlaylistProposalResponse;
import com.ibereciartua.commons.domain.Song;
import com.ibereciartua.connector.MusicConnector;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {
    private final AuthService authService;
    private final MusicConnectorFactory musicConnectorFactory;
    private static final double PLAYLIST_EXTRA_MINUTES_COEFFICIENT = 2.4;
    private static final double STRIDE_LENGTH_COEFFICIENT = 0.413;
    private static final int BPM_THRESHOLD = 10;

    public PlaylistService(final MusicConnectorFactory musicConnectorFactory, final AuthService authService) {
        this.authService = authService;
        this.musicConnectorFactory = musicConnectorFactory;
    }

    /**
     * Get a playlist proposal based on the user's running pace, distance and height.
     * @param paceInMinPerKm The user's running pace in min/km.
     *                       Must be a positive number.
     * @param distance The user's running distance in km.
     *                 Must be a positive number.
     * @param height The user's height in cm.
     *               Must be a positive number.
     * @return The playlist proposal.
     * @throws IllegalArgumentException If any of the parameters is not a positive number.
     */
    public PlaylistProposalResponse getPlaylistProposal(float paceInMinPerKm, float distance, float height) {
        if (paceInMinPerKm <= 0 || distance <= 0 || height <= 0) {
            throw new IllegalArgumentException("Invalid parameters");
        }
        int bpm = calculateBPM(paceInMinPerKm, height);
        float durationInSeconds = paceInMinPerKm * distance * 60;

        int cumulativeDuration = 0;
        int offset = 0;
        int neededDurationInSeconds = (int) (durationInSeconds * PLAYLIST_EXTRA_MINUTES_COEFFICIENT);
        List<Song> songs = new ArrayList<>();
        Optional<String> token = authService.getAccessToken();
        if (token.isEmpty()) {
            throw new UserCredentialsNotFoundException("No access token found");
        }
        Optional<String> authenticatorProvider = authService.getAuthenticatorProvider();
        if (authenticatorProvider.isEmpty()) {
            throw new UserCredentialsNotFoundException("No authenticator provider found");
        }
        MusicConnector musicConnector = musicConnectorFactory.getMusicConnector(authenticatorProvider.get());

        while (cumulativeDuration < neededDurationInSeconds) {
            List<Song> newFetchedSongs = musicConnector.getUserTracks(token.get(), offset);
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
            throw new SongSearchException("No songs found");
        }

        String name = "Running Session - %skm - %smin/km - %s BPM".formatted(distance, paceInMinPerKm, bpm);

        return new PlaylistProposalResponse(name, bpm, (int) durationInSeconds, songs);
    }

    /**
     * Calculate the BPM based on the user's running pace and height.
     * @param paceInMinPerKm The user's running pace in min/km.
     * @param height The user's height in cm.
     * @return The calculated BPM.
     */
    public static int calculateBPM(double paceInMinPerKm, double height) {
        if (paceInMinPerKm <= 0 || height <= 0) {
            throw new IllegalArgumentException("Invalid parameters");
        }
        double metersPerMinute = 1000 / paceInMinPerKm;
        double strideLength = STRIDE_LENGTH_COEFFICIENT * (height / 100);
        double cadence = metersPerMinute / strideLength;
        return (int) Math.round(cadence / 2);
    }

    /**
     * Create a new playlist based on the user's preferences.
     * @param request The request containing the playlist preferences.
     */
    public void createPlaylist(NewPlaylistRequest request) {
        Optional<String> authenticatorProvider = authService.getAuthenticatorProvider();
        if (authenticatorProvider.isEmpty()) {
            throw new UserCredentialsNotFoundException("No authenticator provider found");
        }
        Playlist playlist = new Playlist(request.name(), request.songIds());
        MusicConnector musicConnector = musicConnectorFactory.getMusicConnector(authenticatorProvider.get());
        Optional<String> accessToken = authService.getAccessToken();
        if (accessToken.isEmpty()) {
            throw new UserCredentialsNotFoundException("No access token found");
        }
        Optional<String> name = authService.getName();
        if (name.isEmpty()) {
            throw new UserCredentialsNotFoundException("No user name found");
        }
        musicConnector.createPlaylist(accessToken.get(), name.get(), playlist);
    }
}
