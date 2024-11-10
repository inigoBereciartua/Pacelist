package com.ibereciartua.app.controller;

import com.ibereciartua.app.domain.NewPlaylistRequest;
import com.ibereciartua.app.domain.PlaylistResponse;
import com.ibereciartua.app.service.PlaylistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/playlist")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping
    public ResponseEntity<PlaylistResponse> getPlaylist(@RequestParam("pace") Float pace, @RequestParam("distance") Float distance, @RequestParam("height") Float height) {
        return ResponseEntity.ok(playlistService.getPlaylistProposal(pace, distance, height));
    }

    @PostMapping
    public ResponseEntity<Void> createPlaylist(@RequestBody NewPlaylistRequest request) {
        playlistService.addPlaylist(request);
        return ResponseEntity.ok().build();
    }
}
