<template>
    <div>
        <back-arrow/>
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
                        :style="{ backgroundColor: selectedSongsIds.includes(track.id) ? '#182c25' : '#455b55' }"
                        class="track-item"
                        @click="toggleSelection(track.id)"
                    >
                        <img :src="track.picture" alt="Album Cover" class="album-cover" />
                        <p class="track-name"><strong>{{ track.title }}</strong></p>
                        <p class="artist-name">by {{ track.artist }}</p>
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
import { API_URL } from '../utils/api';
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
                return 0; // Handle case when playlist is not loaded
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
            try {
                const response = await fetch(`${API_URL}/api/playlist?pace=${pace}&distance=${distance}&height=${height}`, {
                    credentials: 'include'
                });

                if (response.ok) {
                    const rawData = await response.text();

                    let data;
                    try {
                        data = JSON.parse(rawData);

                        if (typeof data === 'string') {
                            // For some reason the data is double-encoded
                            data = JSON.parse(data);
                        }
                        this.playlist = data;
                    } catch (error) {
                        toast.error('An error has occured while processing request');
                        return;
                    }
                } else {
                    toast.error('An error has occurred while fetching playlist:', response.statusText);
                }
            } catch (error) {
                toast.error('An error has occurred while fetching playlist:', error);
            }
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
            // Send selected songs to the backend
            const toast = useToast();
            const request = {
                name: this.playlist.name,
                visible: false,
                colaborative: false,
                songIds: this.selectedSongsIds
            };

            this.loading = true;
            fetch(`${API_URL}/api/playlist`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(request),
                credentials: 'include'
            })
                .then(response => {
                    this.loading = false;
                    if (response.ok) {
                        toast.success('Playlist has been successfully created!');
                        this.playlist = null;
                        this.$router.push({ name: 'PlaylistInfoForm' });
                    } else {
                        toast.error('Failed to submit selected songs:', response.statusText);
                    }
                })
                .catch(error => {
                    toast.error('Failed to submit selected songs:', error);
                });
        },
        goBack() {
            this.$router.go(-1);
        }
    }
};
</script>

<style scoped>
.spotify-container {
    max-width: 900px;
    margin: 0 auto;
    padding: 20px;
    font-family: Arial, sans-serif;
}

.recent-tracks {
    margin-top: 20px;
}

.playlist-data {
  background-color: #212121;
  padding: 20px;
  border-radius: 10px;
  margin-bottom: 20px; /* Adds space at the bottom of the playlist card */
}

.track-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr)); /* Responsive grid */
  gap: 20px;
}

.track-item {
  text-align: center;
  border: 1px solid #333;
  border-radius: 10px;
  padding: 10px;
  color: white;
  transition: transform 0.2s ease;
  cursor: pointer;
}

.track-item:hover {
  transform: scale(1.05); /* Slight zoom on hover */
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
  border-color: #306844;
  border-width: 3px;
  border-style: solid;
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
