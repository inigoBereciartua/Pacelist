package com.ibereciartua.app.factory;

import com.ibereciartua.connector.MusicConnector;
import com.ibereciartua.connector.soundcloud.SoundCloudConnector;
import com.ibereciartua.connector.spotify.SpotifyConnector;
import org.springframework.stereotype.Service;

/**
 * Factory for creating music connectors.
 * This factory is responsible for creating the music connectors based on the provided music connector type.
 * The music connectors are used to interact with the music services and retrieve the songs.
 */
@Service
public class MusicConnectorFactory {

    /**
     * Get the music connector based on the provided music connector type.
     * @param musicConnector The music connector type.
     *                       Must be either "spotify" or "soundcloud".
     * @return The music connector.
     */
    public MusicConnector getMusicConnector(String musicConnector) {
        return switch (musicConnector) {
            case "spotify" -> new SpotifyConnector();
            case "soundcloud" -> new SoundCloudConnector();
            default -> throw new IllegalArgumentException("Music connector not found: " + musicConnector);
        };
    }
}
