package com.ibereciartua.connector;

import com.ibereciartua.commons.domain.Playlist;
import com.ibereciartua.commons.domain.Song;
import com.ibereciartua.connector.exceptions.MusicConnectorException;
import com.ibereciartua.connector.exceptions.PlaylistCreationException;

import java.util.List;

public interface MusicConnector {
    List<Song> getUserTracks(String accessToken, int offset) throws MusicConnectorException;
    void createPlaylist(final String accessToken, final String userId, final Playlist newPlaylist) throws PlaylistCreationException;
}
