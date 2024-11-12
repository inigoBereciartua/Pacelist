package com.ibereciartua.app.controller;

import com.ibereciartua.app.domain.NewPlaylistRequest;
import com.ibereciartua.app.domain.PlaylistProposalResponse;
import com.ibereciartua.app.service.PlaylistService;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/playlist")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping
    public ResponseEntity<PlaylistProposalResponse> getPlaylist(@RequestParam("pace") @NonNull Float pace,
                                                                @RequestParam("distance") @NonNull Float distance,
                                                                @RequestParam("height") @NonNull Float height) {
        return ResponseEntity.ok(playlistService.getPlaylistProposal(pace, distance, height));
    }

    @PostMapping
    public ResponseEntity<Void> createPlaylist(@RequestBody NewPlaylistRequest request) {
        playlistService.createPlaylist(request);
        return ResponseEntity.ok().build();
    }
}
