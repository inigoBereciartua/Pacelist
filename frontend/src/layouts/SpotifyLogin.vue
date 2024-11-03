<template>
    <div class="login-container">
        <div class="login-content">
            <h1>Welcome to Pacelist</h1>
            <p class="subtitle">Boost your runs with playlists tailored to your stride. Music you love, perfectly paced.</p>
            <button @click="loginWithSpotify" class="login-button">
                <img src="@/assets/spotify-icon.png" alt="Spotify Icon" class="spotify-icon" />
                Log in with Spotify
            </button>
            <p class="login-info-text">
                By logging in with Spotify, you agree to our
                <router-link to="/terms-and-conditions" class="terms-link">Terms and Conditions</router-link>. This allows us to personalize playlists with songs you love!
            </p>
        </div>
    </div>
</template>

<script>
import { API_URL } from "@/utils/api";

export default {
    name: 'SpotifyLogin',
    methods: {
        loginWithSpotify() {
            window.location.href = `${API_URL}/auth/login?redirectUri=` + window.location.origin + '/playlist-info-form';
        }
    }
};
</script>

<style>
.login-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background: #121212;
}

.login-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
    color: #1DB954;
    max-width: 400px;
}

h1 {
    font-size: 2.5em;
    margin-bottom: 10px;
}

.subtitle {
    font-size: 1.2em;
    color: #b3b3b3;
    margin-bottom: 20px;
}

.login-info-text {
    font-size: 0.9em;
    color: #b3b3b3;
    margin-top: 15px;
}

.terms-link {
    color: #1DB954;
    text-decoration: none;
}

.terms-link:hover {
    text-decoration: underline;
}

.login-button {
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    background: linear-gradient(90deg, #1DB954, #1ed760);
    border: none;
    padding: 15px 28px;
    border-radius: 25px;
    cursor: pointer;
    font-size: 16px;
}

.login-button:hover {
    background: linear-gradient(90deg, #1ed760, #1DB954);
}

.spotify-icon {
    width: 24px;
    height: 24px;
    margin-right: 12px;
}
</style>
