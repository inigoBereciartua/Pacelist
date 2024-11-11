package com.ibereciartua.app.factory;

import com.ibereciartua.connector.MusicConnector;
import com.ibereciartua.connector.soundcloud.SoundCloudConnector;
import com.ibereciartua.connector.spotify.SpotifyConnector;
import org.springframework.stereotype.Service;

@Service
public class MusicConnectorFactory {

    public MusicConnector getMusicConnector(String musicConnector) {
        return switch (musicConnector) {
            case "spotify" -> new SpotifyConnector();
            case "soundcloud" -> new SoundCloudConnector();
            default -> throw new IllegalArgumentException("Music connector not found: " + musicConnector);
        };
    }
}
