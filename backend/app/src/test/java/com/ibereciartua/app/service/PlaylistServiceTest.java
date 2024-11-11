package com.ibereciartua.app.service;

import com.ibereciartua.app.domain.PlaylistResponse;
import com.ibereciartua.commons.domain.Song;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaylistServiceTest {

    @Mock
    SpotifyService spotifyService;
    @Mock
    AuthService authService;

    @InjectMocks
    PlaylistService playlistService;

    @Nested
    class getPlaylistProposal {
        @Test
        void getPlaylistProposal_shouldReturnCorrectPlaylistResponse() {
            float paceInMinPerKm = 5.0f;
            float distance = 10.0f;
            float height = 175.0f;

            List<Song> songs = List.of(
                    new Song("1", "Song 1", "Artist 1", "Album 1", "Picture 1", 138, null, 180),
                    new Song("2", "Song 2", "Artist 2", "Album 2", "Picture 2", 140, null, 180),
                    new Song("3", "Song 3", "Artist 3", "Album 3", "Picture 3", 136, null, 180)
            );

            when(authService.getAccessToken()).thenReturn("mockedAccessToken");
            when(spotifyService.getSongs(anyString(), anyInt())).thenReturn(songs).thenReturn(List.of());

            PlaylistResponse playlistResponse = playlistService.getPlaylistProposal(paceInMinPerKm, distance, height);

            assertNotNull(playlistResponse);
            assertEquals (playlistResponse.getName(), "Running Session - 10.0km - 5.0min/km - 138 BPM");
            assertEquals(138, playlistResponse.getBpm());
            assertEquals(3000, playlistResponse.getNeededDurationInSeconds());
            assertEquals(3, playlistResponse.getSongs().size());
        }

        @Test
        void getPlaylistProposal_shouldHandleNoSongsFound() {
            float paceInMinPerKm = 5.0f;
            float distance = 10.0f;
            float height = 175.0f;

            when(authService.getAccessToken()).thenReturn("mockedAccessToken");
            when(spotifyService.getSongs(anyString(), anyInt())).thenReturn(List.of());

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                playlistService.getPlaylistProposal(paceInMinPerKm, distance, height);
            });

            assertEquals("No songs found", exception.getMessage());
        }

        @Test
        void getPlaylistProposal_shouldRemoveExceedingSongs() {
            float paceInMinPerKm = 5.0f;
            float distance = 10.0f;
            float height = 175.0f;

            List<Song> songs = List.of(
                    new Song("1", "Song 1", "Artist 1", "Album 1", "Picture 1", 138, null, 2000),
                    new Song("2", "Song 2", "Artist 2", "Album 2", "Picture 2", 140, null, 3000),
                    new Song("3", "Song 3", "Artist 3", "Album 3", "Picture 3", 136, null, 3080),
                    new Song("4", "Song 4", "Artist 4", "Album 4", "Picture 4", 140, null, 3000)
            );

            when(authService.getAccessToken()).thenReturn("mockedAccessToken");
            when(spotifyService.getSongs(anyString(), anyInt())).thenReturn(songs).thenReturn(List.of());

            PlaylistResponse playlistResponse = playlistService.getPlaylistProposal(paceInMinPerKm, distance, height);

            assertNotNull(playlistResponse);
            assertEquals(3000, playlistResponse.getNeededDurationInSeconds());
            assertEquals(3, playlistResponse.getSongs().size());
        }

        @Test
        void getPlaylistProposal_shouldHandleZeroPace() {
            float paceInMinPerKm = 0.0f;
            float distance = 10.0f;
            float height = 175.0f;

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                playlistService.getPlaylistProposal(paceInMinPerKm, distance, height);
            });

            assertEquals("Invalid parameters", exception.getMessage());
        }

        @Test
        void getPlaylistProposal_shouldHandleZeroDistance() {
            float paceInMinPerKm = 5.0f;
            float distance = 0.0f;
            float height = 175.0f;

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                playlistService.getPlaylistProposal(paceInMinPerKm, distance, height);
            });

            assertEquals("Invalid parameters", exception.getMessage());
        }

        @Test
        void getPlaylistProposal_shouldHandleZeroHeight() {
            float paceInMinPerKm = 5.0f;
            float distance = 10.0f;
            float height = 0.0f;

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                playlistService.getPlaylistProposal(paceInMinPerKm, distance, height);
            });

            assertEquals("Invalid parameters", exception.getMessage());
        }

        @Test
        void getPlaylistProposal_shouldHandleNegativePace() {
            float paceInMinPerKm = -5.0f;
            float distance = 10.0f;
            float height = 175.0f;

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                playlistService.getPlaylistProposal(paceInMinPerKm, distance, height);
            });

            assertEquals("Invalid parameters", exception.getMessage());
        }

        @Test
        void getPlaylistProposal_shouldHandleNegativeDistance() {
            float paceInMinPerKm = 5.0f;
            float distance = -10.0f;
            float height = 175.0f;

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                playlistService.getPlaylistProposal(paceInMinPerKm, distance, height);
            });

            assertEquals("Invalid parameters", exception.getMessage());
        }

        @Test
        void getPlaylistProposal_shouldHandleNegativeHeight() {
            float paceInMinPerKm = 5.0f;
            float distance = 10.0f;
            float height = -175.0f;

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                playlistService.getPlaylistProposal(paceInMinPerKm, distance, height);
            });

            assertEquals("Invalid parameters", exception.getMessage());
        }
    }

    @Nested
    class calculateBPM {
        @Test
        void calculateBPM_shouldReturnCorrectBPM() {
            double paceInMinPerKm = 5.0;
            double height = 175.0;
            int expectedBPM = 138;

            int actualBPM = PlaylistService.calculateBPM(paceInMinPerKm, height);

            assertEquals(expectedBPM, actualBPM);
        }

        @Test
        void calculateBPM_shouldHandleZeroHeight() {
            double paceInMinPerKm = 5.0;
            double height = 0.0;

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                PlaylistService.calculateBPM(paceInMinPerKm, height);
            });

            assertEquals("Invalid parameters", exception.getMessage());
        }

        @Test
        void calculateBPM_shouldHandleZeroPace() {
            double paceInMinPerKm = 0.0;
            double height = 175.0;

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                PlaylistService.calculateBPM(paceInMinPerKm, height);
            });

            assertEquals("Invalid parameters", exception.getMessage());
        }

        @Test
        void calculateBPM_shouldHandleNegativeValues() {
            double paceInMinPerKm = -5.0;
            double height = -175.0;

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                PlaylistService.calculateBPM(paceInMinPerKm, height);
            });

            assertEquals("Invalid parameters", exception.getMessage());
        }
    }
}