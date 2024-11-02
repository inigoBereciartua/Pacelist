package com.ibereciartua.pacelist.driver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibereciartua.pacelist.domain.NewPlaylist;
import com.ibereciartua.pacelist.domain.Song;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import org.springframework.stereotype.Service;

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
import java.util.List;
import java.util.logging.Logger;

@Service
public class SpotifyConnector {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Logger logger = Logger.getLogger(SpotifyConnector.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final int PAGE_SIZE = 50;

    public List<Song> getUserTracksByBpm(String accessToken, int bpmTarget, int bpmThreshold, int limit) throws IOException, InterruptedException, URISyntaxException {
        logger.info("Fetching user tracks with BPM between " + (bpmTarget - bpmThreshold) + " and " + (bpmTarget + bpmThreshold) + ", Total limit: " + limit);

        List<JsonNode> allTracks = new ArrayList<>();  // Store original track data
        List<String> allTrackIds = new ArrayList<>();
        int offset = 0;
        int pageSize = Math.min(limit, PAGE_SIZE);  // Ensure page size is within the allowed range
        int fetchedItems = 0;

        // Fetch paginated tracks
        while (fetchedItems < limit) {
            logger.info("Fetching tracks with limit " + pageSize + " and offset " + offset);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.spotify.com/v1/me/tracks?limit=" + pageSize + "&offset=" + offset))
                    .header("Authorization", "Bearer " + accessToken)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                logger.warning("Failed to fetch user tracks. Status code: " + response.statusCode());
                return null;
            }

            JsonNode items = objectMapper.readTree(response.body()).get("items");
            logger.info("Fetched " + items.size() + " tracks with offset " + offset);

            if (items.isEmpty()) {
                logger.info("No more tracks to evaluate.");
                break;
            }

            // Collect track IDs and store original track data
            for (JsonNode item : items) {
                JsonNode track = item.get("track");
                if (track != null && track.has("id")) {
                    String trackId = track.get("id").asText();
                    allTrackIds.add(trackId);
                    allTracks.add(item);
                } else {
                    logger.warning("Track data missing from item. Skipping this item.");
                }
            }

            fetchedItems += items.size();
            offset += pageSize;

            if (fetchedItems >= limit) {
                logger.info("Reached the total limit of " + limit + " items.");
                break;
            }
        }

        // Fetch audio features in batches (max 100 IDs per batch)
        List<JsonNode> tracksWithBpm = new ArrayList<>();
        for (int i = 0; i < allTrackIds.size(); i += 100) {
            List<String> batchTrackIds = allTrackIds.subList(i, Math.min(i + 100, allTrackIds.size()));
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
            for (JsonNode audioFeature : audioFeaturesArray) {
                double tempo = audioFeature.path("tempo").asDouble();
                if (tempo >= bpmTarget - bpmThreshold && tempo <= bpmTarget + bpmThreshold) {
                    tracksWithBpm.add(audioFeature);
                }
            }
        }

        List<Song> matchingTracks = new ArrayList<>();
        for (JsonNode audioFeature : tracksWithBpm) {
            String trackId = audioFeature.get("id").asText();
            JsonNode matchingTrackItem = allTracks.stream()
                    .filter(trackItem -> trackItem.path("track").path("id").asText().equals(trackId))
                    .findFirst()
                    .orElse(null);

            if (matchingTrackItem != null) {
                JsonNode matchingTrack = matchingTrackItem.get("track");

                Song song = new Song();
                song.setId(trackId);
                song.setName(matchingTrack.get("name").asText());
                song.setArtist(matchingTrack.get("artists").get(0).get("name").asText());
                song.setAlbum(matchingTrack.get("album").get("name").asText());
                song.setPicture(matchingTrack.get("album").get("images").get(0).get("url").asText());
                String playedAt = matchingTrackItem.get("added_at").asText();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");
                LocalDateTime playedDate = LocalDateTime.parse(playedAt, formatter.withZone(ZoneOffset.UTC));
                song.setPlayedDate(playedDate);
                song.setDuration(matchingTrack.get("duration_ms").asInt() / 1000);

                matchingTracks.add(song);
            } else {
                logger.warning("Track with ID " + trackId + " not found in original data.");
            }
        }

        logger.info("Returning " + matchingTracks.size() + " tracks after BPM filtering.");
        return matchingTracks;
    }

    public List<Song> getRecentlyPlayedTracks(String accessToken, int limit) throws Exception {
        logger.info("Fetching recently played tracks with limit " + limit);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.spotify.com/v1/me/player/recently-played?limit=" + limit))
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            logger.warning("Failed to fetch recently played tracks. Status code: " + response.statusCode());
            return null;
        }

        JsonNode items = objectMapper.readTree(response.body()).get("items");
        return objectMapper.convertValue(items, new TypeReference<List<Song>>() {});
    }

    public String createPlaylist(String accessToken, String userId, NewPlaylist newPlaylist) throws Exception {
        logger.info("Creating a new playlist on Spotify.");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://api.spotify.com/v1/users/" + userId + "/playlists"))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(newPlaylist)))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 201) {
            logger.warning("Failed to create playlist. Status code: " + response.statusCode());
            return null;
        }

        JsonNode createdPlaylist = objectMapper.readTree(response.body());
        String playlistId = createdPlaylist.get("id").asText();
        logger.info("Successfully created playlist with ID " + playlistId);

        addTracksToPlaylist(accessToken, playlistId, newPlaylist.getSongIds());
        return playlistId;
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

    static class TrackUris {
        private List<String> uris;

        public TrackUris(List<String> uris) {
            this.uris = uris;
        }

        public List<String> getUris() {
            return uris;
        }
    }
}
