<template>
    <div>
        <back-arrow />
        <div class="spotify-container">
            <div v-if="playlist == null" class="centered-content">
                <h1 v-if="username">Welcome, {{ username }}!</h1>
                <p v-else>Fetching user info...</p> <!-- Fallback text if username is empty -->

                <p class="intro-text">
                    Please enter your body composition and run objectives to help us personalize playlists that match your
                    workout needs.
                </p>

                <div class="form-container">
                    <form @submit.prevent="submitForm">
                        <div class="form-group">
                            <label for="height">Height</label>
                            <div class="input-with-unit">
                                <input type="number" v-model="height" id="height" placeholder="Enter your height" />
                                <span class="unit">cm</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="pace">Pace</label>
                            <div class="input-with-unit">
                                <input type="number" step="0.1" v-model="pace" id="pace" placeholder="Enter your pace" />
                                <span class="unit">min/km</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="distance">Distance</label>
                            <div class="input-with-unit">
                                <input type="number" step="0.1" v-model="distance" id="distance"
                                    placeholder="Enter distance" />
                                <span class="unit">km</span>
                            </div>
                        </div>
                        <div class="submit-container">
                            <button type="submit">Submit</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</template>


<script>
import { useToast } from "vue-toastification";
import { API_URL } from '../utils/api';
import BackArrow from "@/components/BackArrow.vue";

export default {
    name: 'PlaylistInfoForm',
    components: {
        BackArrow
    },
    data() {
        return {
            username: '',
            recentTracks: [],
            recentTracksWithBpm: [],
            playlist: null,
            selectedSongsIds: [],
            /* Form fields */
            height: 170,
            pace: 5.0,
            distance: 5.0
        };
    },
    mounted() {
        this.getUser();
    },
    methods: {
        async getUser() {
            const toast = useToast();
            try {
                const response = await fetch(`${API_URL}/spotify/userinfo`, {
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

                        if (data && data.username) {
                            this.username = data.id;
                        } else {
                            toast.error('An error has occured while processing user information request');
                        }
                    } catch (error) {
                        toast.error('An error has occured while processing user information request');
                        return;
                    }
                } else {
                    toast.error('An error has occured while processing user information request');
                }
            } catch (error) {
                toast.error('An error has occured while processing user information request');
            }
        },
        submitForm() {
            // Go to PlaylistPreview passing form data
            this.$router.push({
                name: 'PlaylistPreview',
                query: {
                    height: this.height,
                    pace: this.pace,
                    distance: this.distance
                }
            });
        },
    }
};
</script>

<style scoped>
.spotify-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    font-family: Arial, sans-serif;
    padding: 20px;
    color: white;
}

.centered-content {
    text-align: center;
    max-width: 500px;
}

.intro-text {
    margin-bottom: 20px;
    color: #b3b3b3;
    /* Subtle gray for introduction text */
}

.form-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20px;
    background-color: #212121;
    border-radius: 10px;
    color: white;
    width: 90%;
    max-width: 400px;
    margin: 0 auto;
}

.form-group {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    margin-bottom: 15px;
    width: 100%;
}

.form-group label {
    margin-bottom: 5px;
    text-align: left;
    color: #b3b3b3;
}

.input-with-unit {
    display: flex;
    align-items: center;
    width: 100%;
    max-width: 300px;
}

.input-with-unit input {
    flex: 1;
    padding: 8px;
    border: 1px solid #455b55;
    border-radius: 5px 0 0 5px;
    /* Rounded left corners */
    text-align: left;
}

.input-with-unit .unit {
    padding: 8px;
    background-color: #333;
    border: 1px solid #455b55;
    border-left: none;
    /* Prevents double border */
    border-radius: 0 5px 5px 0;
    /* Rounded right corners */
    font-size: 0.9em;
    color: #b3b3b3;
    text-align: center;
}

.submit-container {
    text-align: center;
    margin-top: 20px;
}

.submit-container button {
    background-color: #1DB954;
    color: white;
    padding: 10px 20px;
    border: none;
    border-radius: 25px;
    cursor: pointer;
    font-size: 16px;
    transition: background-color 0.3s ease;
}

.submit-container button:hover {
    background-color: #1ed760;
}
</style>
