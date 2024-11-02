package com.ibereciartua.pacelist.controller;

import com.ibereciartua.pacelist.domain.NewPlaylist;
import com.ibereciartua.pacelist.domain.PlaylistResponse;
import com.ibereciartua.pacelist.service.PlaylistService;
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
        return ResponseEntity.ok(playlistService.getPlaylist(pace, distance, height));
    }

    @PostMapping
    public ResponseEntity<Void> createPlaylist(@RequestBody NewPlaylist request) {
        playlistService.addPlaylist(request);
        return ResponseEntity.ok().build();
    }
}
