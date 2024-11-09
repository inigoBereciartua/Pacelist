<template>
    <div>
        <div class="header">
            <back-arrow/>
            <div class="spotify-attribution">
                <a href="https://www.spotify.com/" target="_blank" class="contact-link">
                    <img src="@/assets/spotify-icon.png" alt="Spotify Icon" class="spotify-icon" /> Data provided by Spotify
                </a>
            </div>
        </div>
        <div v-if="playlist" class="recent-tracks">
            <h2>{{ playlist.name }}</h2>
            <div class="playlist-data">
                <div class="progress-container">
                    <h4>Playlist duration: {{ currentSelectedSongsDurationInMinutes }} minutes / {{ neededDurationForSessionInMinutes }} minutes needed</h4>
                    <progress :value="currentSelectedSongsDurationInMinutes" :max="neededDurationForSessionInMinutes"></progress>
                </div>
                <div class="submit-container">
                    <button @click="submitSelectedSongs">Submit Selected Songs</button>
                </div>
                <div class="track-grid">
                    <div
                        v-for="(track, index) in playlist.songs"
                        :key="index"
                        :class="{ 'selected-song': selectedSongsIds.includes(track.id) }"
                        :style="{ backgroundColor: selectedSongsIds.includes(track.id) ? '#1b3b33' : '#455b55' }"
                        class="track-item"
                        @click="toggleSelection(track.id)"
                    >
                        <div class="checkmark-overlay" v-if="selectedSongsIds.includes(track.id)">
                            ✓
                        </div>
                        <img :src="track.picture" alt="Album Cover" class="album-cover" />
                        <p class="track-name" :class="{ 'selected-text': selectedSongsIds.includes(track.id) }">
                            <strong>{{ track.title }}</strong>
                        </p>
                        <p class="artist-name" :class="{ 'selected-text': selectedSongsIds.includes(track.id) }">by {{ track.artist }}</p>
                    </div>
                </div>
            </div>
        </div>
        <LoadingSpinner :isLoading="playlist == null" />
    </div>
</template>

<script>
import { useToast } from "vue-toastification";
import LoadingSpinner from "@/components/LoadingSpinner.vue";
import playlistApi from '@/api/playlist';
import BackArrow from "@/components/BackArrow.vue";

export default {
    name: 'PlaylistPreview',
    components: {
        LoadingSpinner,
        BackArrow
    },
    data() {
        return {
            recentTracks: [],
            recentTracksWithBpm: [],
            playlist: null,
            selectedSongsIds: [],
            loading: false
        };
    },
    mounted() {
        const pace = this.$route.query.pace;
        const distance = this.$route.query.distance;
        const height = this.$route.query.height;

        if (pace && distance && height) {
            this.loading = true;
            this.getTracksForSession(pace, distance, height);
        } else {
            const toast = useToast();
            toast.error('Invalid form data');
            this.$router.push({ name: 'PlaylistInfoForm' });
        }
    },

    watch: {
        playlist: {
            handler: function (newVal) {
                if (newVal != null && newVal.songs != null) {
                    let duration = 0;
                    let selectedSongs = [];
                    for (const song of newVal.songs) {
                        if (duration <= newVal.neededDurationInSeconds) {
                            selectedSongs.push(song.id);
                            duration += song.duration;
                        } else {
                            break;
                        }
                    }
                    this.selectedSongsIds = selectedSongs;
                }
            },
            deep: true
        }
    },
    computed: {
        currentSelectedSongsDurationInMinutes() {
            if (!this.playlist || !this.playlist.songs) {
                return 0; // TODO: Handle case when playlist is not loaded
            }
            const durationInSeconds = this.playlist.songs
                .filter(song => this.selectedSongsIds.includes(song.id))
                .reduce((acc, song) => acc + song.duration, 0);
            return Math.floor(durationInSeconds / 60);
        },
        neededDurationForSessionInMinutes() {
            if (!this.playlist || !this.playlist.neededDurationInSeconds) {
                return 0;
            }
            return Math.floor(this.playlist.neededDurationInSeconds / 60);
        }
    },
    methods: {
        async getTracksForSession(pace, distance, height) {
            const toast = useToast();
            playlistApi.getPlaylistProposal(this, pace, distance, height)
                .then((response) => {
                    this.playlist = response;
                })
                .catch(() => {
                    toast.error('An error has occured while processing request');
                });
        },
        toggleSelection(songId) {
            const index = this.selectedSongsIds.indexOf(songId);
            if (index === -1) {
                this.selectedSongsIds.push(songId);
            } else {
                this.selectedSongsIds.splice(index, 1);
            }
        },
        submitSelectedSongs() {
            const toast = useToast();
            const request = {
                name: this.playlist.name,
                visible: false,
                colaborative: false,
                songIds: this.selectedSongsIds
            };

            this.loading = true;
            playlistApi.savePlaylist(this, request)
                .then(() => {
                    this.loading = false;
                    this.playlist = null;
                    toast.success('Playlist has been successfully created!');
                    this.$router.push({ name: 'PlaylistInfoForm' });
                })
                .catch(() => {
                    this.loading = false;
                    toast.error('Failed to submit selected songs');
                });
        }
    }
};
</script>

<style scoped>
.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px 20px;
    width: 100%;
    background-color: #121212;
    z-index: 10;
    box-sizing: border-box;
}

.spotify-attribution {
    font-size: 12px;
    color: #1db954;
    font-weight: bold;
    white-space: nowrap;
    margin-right: 10px;
}

.spotify-icon {
    width: 16px;
    height: 16px;
    margin-right: 5px;
    filter: brightness(0) saturate(100%) invert(44%) sepia(55%) saturate(262%) hue-rotate(89deg) brightness(95%) contrast(91%);
}

.playlist-data {
    background-color: #212121;
    padding: 20px;
    border-radius: 10px;
    margin-bottom: 20px;
}

.track-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
    gap: 20px;
}

.track-item {
    text-align: center;
    border: 1px solid #333;
    border-radius: 10px;
    padding: 10px;
    color: white;
    transition: transform 0.2s ease, background-color 0.2s ease;
    cursor: pointer;
    position: relative;
}

.track-item:hover {
    transform: scale(1.05);
    background-color: #3b4b4a;
}

.album-cover {
    width: 100px;
    height: 100px;
    object-fit: cover;
    margin-bottom: 10px;
    border-radius: 8px;
}

.track-name {
    font-size: 14px;
    font-weight: bold;
    margin: 5px 0;
    color: white;
}

.artist-name {
    font-size: 12px;
    color: grey;
}

.selected-song {
    border-color: #1db954;
    border-width: 3px;
    border-style: solid;
    background-color: #1b3b33;
}

.selected-text {
    font-weight: bold;
    color: #d4f0e2;
}

.checkmark-overlay {
    position: absolute;
    top: -8px;
    right: -8px;
    font-size: 16px;
    color: #1db954;
    background-color: rgba(0, 0, 0, 0.9);
    padding: 2px 6px;
    border-radius: 50%;
    box-shadow: 0 0 4px rgba(0, 0, 0, 0.5);
}

progress {
    width: 100%;
    height: 20px;
    border-radius: 10px;
    overflow: hidden;
}

progress::-webkit-progress-bar {
    background-color: #182c25;
    border-radius: 10px;
}

progress::-webkit-progress-value {
    background-color: #1db954;
    border-radius: 10px;
}

.submit-container {
    text-align: center;
    margin-top: 20px;
}

</style>