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

/**
 * The SoundCloudConnector class is responsible for handling the SoundCloud API requests.
 * @see MusicConnector
 * @see <a href="https://developers.soundcloud.com/docs/api/reference">SoundCloud API Reference</a>
 *
 */
public class SoundCloudConnector implements MusicConnector {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Logger logger = Logger.getLogger(SoundCloudConnector.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Get the user tracks.
     * @param accessToken the access token
     * @param offset the offset
     * @return The user tracks
     * @throws MusicConnectorException if an error occurs while fetching the user tracks
     */
    public List<Song> getUserTracks(String accessToken, int offset) throws MusicConnectorException {
        try {
            SoundCloudTrackResponse likedTracksResponse = fetchLikedTracks(accessToken, offset);
            return mapToSongs(likedTracksResponse.collection());
        } catch (Exception e) {
            logger.warning("Failed to fetch user tracks.");
            throw new MusicConnectorException("Failed to fetch user tracks.", e);
        }
    }

    /**
     * Fetch the liked tracks.
     * @param accessToken the access token
     * @param offset the offset
     * @return The liked tracks
     * @throws IOException if an error occurs while fetching the liked tracks
     * @throws InterruptedException if an error occurs while fetching the liked tracks
     * @throws URISyntaxException if an error occurs while fetching the liked tracks
     * @see SoundCloudTrackResponse
     * @see <a href="https://developers.soundcloud.com/docs/api/reference">SoundCloud API Reference</a>
     */
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

    /**
     * Map the SoundCloud tracks to the Song domain.
     * @param tracks the SoundCloud tracks
     * @return The songs
     */
    private List<Song> mapToSongs(List<SoundCloudTrackResponse.Track> tracks) {
        return tracks.stream().map(track -> {
            String trackId = track.id();
            String title = track.title();
            String artist = track.user().username();
            String picture = track.artwork_url() != null ? track.artwork_url() : "";
            int duration = track.duration() / 1000;
            int bpm = track.bpm() != null ? track.bpm() : -1;

            return new Song(trackId, title, artist, "", picture, "", 0, LocalDateTime.now(), duration);
        }).collect(Collectors.toList());
    }

    /**
     * Create a new playlist on SoundCloud.
     * @param accessToken the access token
     * @param userId the user ID
     * @param newPlaylist the new playlist
     * @throws PlaylistCreationException if an error occurs while creating the playlist
     * @see Playlist
     * @see SoundCloudPlaylist
     * @see <a href="https://developers.soundcloud.com/docs/api/reference">SoundCloud API Reference</a>
     */
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

    /**
     * Build the create playlist request.
     * @param accessToken the access token
     * @param playlist the playlist
     * @return The create playlist request
     * @throws URISyntaxException if an error occurs while building the create playlist request
     * @throws IOException if an error occurs while building the create playlist request
     * @see SoundCloudPlaylist
     * @see <a href="https://developers.soundcloud.com/docs/api/reference">SoundCloud API Reference</a>
     */
    private HttpRequest buildCreatePlaylistRequest(String accessToken, SoundCloudPlaylist playlist) throws URISyntaxException, IOException {
        return HttpRequest.newBuilder()
                .uri(new URI("https://api.soundcloud.com/playlists"))
                .header("Authorization", "OAuth " + accessToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(playlist)))
                .build();
    }

    /**
     * Check the creation of playlist's response.
     * @param response the response
     * @throws PlaylistCreationException if an error occurs while checking the creation of playlist's response
     */
    private void checkCreatePlaylistResponse(HttpResponse<String> response) throws PlaylistCreationException {
        if (response.statusCode() != 201) {
            logger.warning("Failed to create playlist. Status code: " + response.statusCode());
            throw new PlaylistCreationException("Failed to create playlist. Status code: " + response.statusCode());
        }
    }
}

