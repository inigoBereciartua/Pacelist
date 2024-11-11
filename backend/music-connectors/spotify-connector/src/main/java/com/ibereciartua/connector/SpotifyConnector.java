package com.ibereciartua.connector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibereciartua.commons.domain.Song;
import com.ibereciartua.commons.domain.Playlist;
import com.ibereciartua.domain.SpotifyPlaylist;
import com.ibereciartua.domain.TrackUris;

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

public class SpotifyConnector {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Logger logger = Logger.getLogger(SpotifyConnector.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final DateTimeFormatter playedAtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");

    private static final int PAGE_SIZE = 50;

    public List<Song> getUserTracks(String accessToken, int offset) throws IOException, InterruptedException, URISyntaxException {
        List<JsonNode> allTracks = fetchTracks(accessToken, offset);
        if (allTracks.isEmpty()) {
            logger.info("No tracks found.");
            return Collections.emptyList();
        }

        List<JsonNode> audioFeatures = fetchAudioFeatures(accessToken, allTracks);
        return mapToSongs(allTracks, audioFeatures);
    }

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

    public void createPlaylist(final String accessToken, final String userId, final Playlist newPlaylist) throws Exception {
        SpotifyPlaylist playlist = SpotifyPlaylist.from(newPlaylist);
        logger.info("Creating a new playlist on Spotify.");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.spotify.com/v1/users/" + userId + "/playlists"))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(playlist)))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 201) {
            logger.warning("Failed to create playlist. Status code: " + response.statusCode());
            return;
        }

        JsonNode createdPlaylist = objectMapper.readTree(response.body());
        String playlistId = createdPlaylist.get("id").asText();
        logger.info("Successfully created playlist with ID " + playlistId);

        addTracksToPlaylist(accessToken, playlistId, playlist.songIds());
    }

    private void addTracksToPlaylist(String accessToken, String playlistId, List<String> songIds) throws Exception {
        List<String> trackUris = songIds.stream().map(id -> "spotify:track:" + id).toList();
        String uriJson = objectMapper.writeValueAsString(new TrackUris(trackUris));

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