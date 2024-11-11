package com.ibereciartua.connector.soundcloud;

import java.util.List;

public record SoundCloudTrackResponse(List<Track> collection) {

    public record Track(
            String id,
            String title,
            User user,
            String artwork_url,
            int duration,
            Integer bpm
    ) {}

    public record User(String username) {}
}
