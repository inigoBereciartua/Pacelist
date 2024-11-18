package com.ibereciartua.app.controller;

import com.ibereciartua.app.domain.NewPlaylistRequest;
import com.ibereciartua.app.domain.PlaylistProposalResponse;
import com.ibereciartua.app.service.PlaylistService;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling playlist requests.
 * This controller is responsible for handling requests related to the playlist creation and retrieval.
 */
@RestController
@RequestMapping("/api/playlist")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    /**
     * Get a playlist proposal based on the user's running pace, distance and height.
     * @param pace The user's running pace in min/km.
     * @param distance The user's running distance in km.
     * @param height The user's height in cm.
     * @return The playlist proposal.
     */
    @GetMapping
    public ResponseEntity<PlaylistProposalResponse> getPlaylist(@RequestParam("pace") @NonNull Float pace,
                                                                @RequestParam("distance") @NonNull Float distance,
                                                                @RequestParam("height") @NonNull Float height) {
        return ResponseEntity.ok(playlistService.getPlaylistProposal(pace, distance, height));
    }

    /**
     * Create a new playlist based on the user's preferences.
     * @param request The request containing the playlist preferences.
     * @return A response entity with no content.
     */
    @PostMapping
    public ResponseEntity<Void> createPlaylist(@RequestBody NewPlaylistRequest request) {
        playlistService.createPlaylist(request);
        return ResponseEntity.ok().build();
    }
}
