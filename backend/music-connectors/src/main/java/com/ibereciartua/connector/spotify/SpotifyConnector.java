package com.ibereciartua.connector.spotify;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibereciartua.commons.domain.Song;
import com.ibereciartua.commons.domain.Playlist;
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
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Connector for Spotify.
 * This class is responsible for interacting with the Spotify API and fetching the user's tracks.
 * It also allows creating a new playlist on Spotify with the selected tracks.
 * @see MusicConnector MusicConnector
 * @see <a href="https://developer.spotify.com/documentation/web-api/">Spotify API documentation</a>
 */
public class SpotifyConnector implements MusicConnector {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Logger logger = Logger.getLogger(SpotifyConnector.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final DateTimeFormatter playedAtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");

    private static final int PAGE_SIZE = 50;

    /**
     * Fetch the user's tracks from Spotify.
     * @param accessToken The user's access token.
     * @param offset The offset for the tracks.
     * @return The user's tracks.
     * @throws MusicConnectorException If an error occurs while fetching the user's tracks.
     * @see Song Song
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/tracks/get-several-audio-features/">Spotify API documentation</a>
     */
    public List<Song> getUserTracks(String accessToken, int offset) throws MusicConnectorException {
        try {
            List<JsonNode> allTracks = fetchTracks(accessToken, offset);
            if (allTracks.isEmpty()) {
                logger.info("No tracks found.");
                return Collections.emptyList();
            }

            List<JsonNode> audioFeatures = fetchAudioFeatures(accessToken, allTracks);
            return mapToSongs(allTracks, audioFeatures);
        } catch (Exception e) {
            logger.warning("Failed to fetch user tracks.");
            throw new MusicConnectorException("Failed to fetch user tracks.", e);
        }
    }

    /**
     * Fetch the user's tracks from Spotify.
     * @param accessToken The user's access token.
     * @param offset The offset for the tracks.
     * @return The user's tracks.
     * @throws IOException If an error occurs while fetching the user's tracks.
     * @throws InterruptedException If an error occurs while fetching the user's tracks.
     * @throws URISyntaxException If an error occurs while fetching the user's tracks.
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/library/get-users-saved-tracks/">Spotify API documentation</a>
     */
    private List<JsonNode> fetchTracks(String accessToken, int offset) throws IOException, InterruptedException, URISyntaxException {
        List<JsonNode> allTracks = new ArrayList<>();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.spotify.com/v1/me/tracks?limit=" + PAGE_SIZE + "&offset=" + offset))
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            logger.warning("Failed to fetch user tracks. Status code: " + response.statusCode());
            return Collections.emptyList();
        }

        JsonNode items = objectMapper.readTree(response.body()).get("items");

        for (JsonNode item : items) {
            JsonNode track = item.get("track");
            if (track != null && track.has("id")) {
                allTracks.add(item);
            } else {
                logger.warning("Track data missing from item. Skipping this item.");
            }
        }
        return allTracks;
    }

    /**
     * Fetch the audio features for the user's tracks.
     * @param accessToken  The user's access token.
     * @param allTracks The user's tracks.
     * @return The audio features for the user's tracks.
     * @throws IOException If an error occurs while fetching the audio features.
     * @throws InterruptedException If an error occurs while fetching the audio features.
     * @throws URISyntaxException If an error occurs while fetching the audio features.
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/tracks/get-several-audio-features/">Spotify API documentation</a>
     */
    private List<JsonNode> fetchAudioFeatures(String accessToken, List<JsonNode> allTracks) throws IOException, InterruptedException, URISyntaxException {
        List<String> trackIds = allTracks.stream()
                .map(track -> track.path("track").path("id").asText())
                .collect(Collectors.toList());

        List<JsonNode> audioFeatures = new ArrayList<>();
        for (int i = 0; i < trackIds.size(); i += 100) {
            List<String> batchTrackIds = trackIds.subList(i, Math.min(i + 100, trackIds.size()));
            String idsString = String.join(",", batchTrackIds);

            HttpRequest audioFeaturesRequest = HttpRequest.newBuilder()
                    .uri(new URI("https://api.spotify.com/v1/audio-features?ids=" + idsString))
                    .header("Authorization", "Bearer " + accessToken)
                    .GET()
                    .build();

            HttpResponse<String> audioFeaturesResponse = httpClient.send(audioFeaturesRequest, HttpResponse.BodyHandlers.ofString());
            if (audioFeaturesResponse.statusCode() != 200) {
                logger.warning("Failed to fetch audio features for batch. Status code: " + audioFeaturesResponse.statusCode());
                continue;
            }

            JsonNode audioFeaturesArray = objectMapper.readTree(audioFeaturesResponse.body()).get("audio_features");
            audioFeaturesArray.forEach(audioFeatures::add);
        }
        return audioFeatures;
    }

    /**
     * Map the user's tracks and audio features to Song objects.
     * @param allTracks The user's tracks.
     * @param audioFeatures The audio features for the user's tracks.
     * @return The user's tracks as Song objects.
     */
    private List<Song> mapToSongs(List<JsonNode> allTracks, List<JsonNode> audioFeatures) {
        List<Song> songs = new ArrayList<>();
        for (JsonNode audioFeature : audioFeatures) {
            String trackId = audioFeature.get("id").asText();
            int bpm = (int) audioFeature.path("tempo").asDouble();

            JsonNode matchingTrackItem = allTracks.stream()
                    .filter(trackItem -> trackItem.path("track").path("id").asText().equals(trackId))
                    .findFirst()
                    .orElse(null);

            if (matchingTrackItem != null) {
                JsonNode matchingTrack = matchingTrackItem.get("track");

                String title = matchingTrack.get("name").asText();
                String artist = matchingTrack.get("artists").get(0).get("name").asText();
                String album = matchingTrack.get("album").get("name").asText();
                String picture = matchingTrack.get("album").get("images").get(0).get("url").asText();
                LocalDateTime playedDate = LocalDateTime.parse(matchingTrackItem.get("added_at").asText(), playedAtFormatter.withZone(ZoneOffset.UTC));
                int duration = matchingTrack.get("duration_ms").asInt() / 1000;

                Song song = new Song(trackId, title, artist, album, picture, bpm, playedDate, duration);

                songs.add(song);
            } else {
                logger.warning("Track with ID " + trackId + " not found in original data.");
            }
        }
        return songs;
    }

    /**
     * Create a new playlist on Spotify.
     * @param accessToken The user's access token.
     * @param userId The user's ID.
     * @param newPlaylist The new playlist to create.
     * @throws PlaylistCreationException If an error occurs while creating the playlist.
     * @see Playlist Playlist
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/playlists/create-playlist/">Spotify API documentation</a>
     */
    public void createPlaylist(final String accessToken, final String userId, final Playlist newPlaylist) throws PlaylistCreationException {
        try {
            SpotifyPlaylist playlist = SpotifyPlaylist.from(newPlaylist);
            logger.info("Creating a new playlist on Spotify.");

            HttpRequest request = buildCreatePlaylistRequest(accessToken, userId, playlist);
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            checkCreatePlaylistResponse(response);

            JsonNode createdPlaylist = objectMapper.readTree(response.body());
            String playlistId = createdPlaylist.get("id").asText();
            logger.info("Successfully created playlist with ID " + playlistId);

            addTracksToPlaylist(accessToken, playlistId, playlist.songIds());
        } catch (Exception e) {
            logger.severe("Error creating playlist: " + e.getMessage());
            throw new PlaylistCreationException("Error creating playlist", e);
        }
    }

    /**
     * Build the request to create a new playlist on Spotify.
     * @param accessToken The user's access token.
     * @param userId The user's ID.
     * @param playlist The new playlist to create.
     * @return The request to create a new playlist on Spotify.
     * @throws URISyntaxException If an error occurs while building the request.
     * @throws IOException If an error occurs while building the request.
     * @see Playlist Playlist
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/playlists/create-playlist/">Spotify API documentation</a>
     */
    private HttpRequest buildCreatePlaylistRequest(String accessToken, String userId, SpotifyPlaylist playlist) throws URISyntaxException, IOException {
        return HttpRequest.newBuilder()
                .uri(new URI("https://api.spotify.com/v1/users/" + userId + "/playlists"))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(playlist)))
                .build();
    }

    /**
     * Check the response from creating a new playlist on Spotify.
     * @param response The response from creating a new playlist on Spotify.
     * @throws PlaylistCreationException If the response indicates an error.
     */
    private void checkCreatePlaylistResponse(HttpResponse<String> response) throws PlaylistCreationException {
        if (response.statusCode() != 201) {
            logger.warning("Failed to create playlist. Status code: " + response.statusCode());
            throw new PlaylistCreationException("Failed to create playlist. Status code: " + response.statusCode());
        }
    }

    /**
     * Add tracks to a playlist on Spotify.
     * @param accessToken The user's access token.
     * @param playlistId The ID of the playlist to add tracks to.
     * @param songIds The IDs of the songs to add to the playlist.
     * @throws Exception If an error occurs while adding tracks to the playlist.
     * @see <a href="https://developer.spotify.com/documentation/web-api/reference/playlists/add-tracks-to-playlist/">Spotify API documentation</a>
     */
    private void addTracksToPlaylist(String accessToken, String playlistId, List<String> songIds) throws Exception {
        List<String> trackUris = songIds.stream().map(id -> "spotify:track:" + id).toList();
        String uriJson = objectMapper.writeValueAsString(new SpotifyTrackUris(trackUris));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.spotify.com/v1/playlists/" + playlistId + "/tracks"))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(uriJson))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 201) {
            logger.warning("Failed to add tracks to playlist. Status code: " + response.statusCode());
        } else {
            logger.info("Successfully added tracks to playlist with ID " + playlistId);
        }
    }
}