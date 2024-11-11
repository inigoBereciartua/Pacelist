package com.ibereciartua.connector.soundcloud;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibereciartua.commons.domain.Playlist;
import com.ibereciartua.commons.domain.Song;
import com.ibereciartua.connector.MusicConnector;
import com.ibereciartua.connector.exceptions.MusicConnectorException;
import com.ibereciartua.connector.exceptions.PlaylistCreationException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SoundCloudConnector implements MusicConnector {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Logger logger = Logger.getLogger(SoundCloudConnector.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Song> getUserTracks(String accessToken, int offset) throws MusicConnectorException {
        try {
            SoundCloudTrackResponse likedTracksResponse = fetchLikedTracks(accessToken, offset);
            return mapToSongs(likedTracksResponse.collection());
        } catch (Exception e) {
            logger.warning("Failed to fetch user tracks.");
            throw new MusicConnectorException("Failed to fetch user tracks.", e);
        }
    }

    private SoundCloudTrackResponse fetchLikedTracks(String accessToken, int offset) throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.soundcloud.com/me/likes/tracks?limit=50&offset=" + offset))
                .header("Authorization", "OAuth " + accessToken)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            logger.warning("Failed to fetch liked tracks. Status code: " + response.statusCode());
            return new SoundCloudTrackResponse(List.of());
        }

        return objectMapper.readValue(response.body(), SoundCloudTrackResponse.class);
    }

    private List<Song> mapToSongs(List<SoundCloudTrackResponse.Track> tracks) {
        return tracks.stream().map(track -> {
            String trackId = track.id();
            String title = track.title();
            String artist = track.user().username();
            String picture = track.artwork_url() != null ? track.artwork_url() : "";
            int duration = track.duration() / 1000;
            int bpm = track.bpm() != null ? track.bpm() : -1;

            return new Song(trackId, title, artist, "", picture, 0, LocalDateTime.now(), duration);
        }).collect(Collectors.toList());
    }

    public void createPlaylist(final String accessToken, final String userId, final Playlist newPlaylist) throws PlaylistCreationException {
        try {
            SoundCloudPlaylist playlist = SoundCloudPlaylist.from(newPlaylist);
            logger.info("Creating a new playlist on SoundCloud.");

            HttpRequest request = buildCreatePlaylistRequest(accessToken, playlist);
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            checkCreatePlaylistResponse(response);

            JsonNode createdPlaylist = objectMapper.readTree(response.body());
            String playlistId = createdPlaylist.get("id").asText();
            logger.info("Successfully created playlist with ID " + playlistId);

        } catch (Exception e) {
            logger.severe("Error creating playlist: " + e.getMessage());
            throw new PlaylistCreationException("Error creating playlist", e);
        }
    }

    private HttpRequest buildCreatePlaylistRequest(String accessToken, SoundCloudPlaylist playlist) throws URISyntaxException, IOException {
        return HttpRequest.newBuilder()
                .uri(new URI("https://api.soundcloud.com/playlists"))
                .header("Authorization", "OAuth " + accessToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(playlist)))
                .build();
    }

    private void checkCreatePlaylistResponse(HttpResponse<String> response) throws PlaylistCreationException {
        if (response.statusCode() != 201) {
            logger.warning("Failed to create playlist. Status code: " + response.statusCode());
            throw new PlaylistCreationException("Failed to create playlist. Status code: " + response.statusCode());
        }
    }
}

