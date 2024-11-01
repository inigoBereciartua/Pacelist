<template>
  <div class="login-container">
    <div class="login-content">
      <h1>Welcome to Sportify</h1>
      <p class="subtitle">Boost your runs with playlists tailored to your stride. Music you love, perfectly paced.</p>
      <button @click="loginWithSpotify" class="login-button">
        <img src="@/assets/spotify-icon.png" alt="Spotify Icon" class="spotify-icon" />
        Login with Spotify
      </button>
      <p class="login-info-subtitle">
        We need you to log in with Spotify so we can personalize playlists with music you love!
      </p>
    </div>
  </div>
</template>

<script>
import { API_URL } from '../utils/api';

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
  background: #121212; /* Dark background */
}

.login-content {
  display: flex;
  flex-direction: column; /* Stack items vertically */
  align-items: center; /* Center items horizontally within content box */
  text-align: center;
  color: #1DB954; /* Spotify green */
  max-width: 400px; /* Optional max-width for better alignment on large screens */
}

h1 {
  font-size: 2.5em;
  margin-bottom: 10px;
}

.subtitle {
  font-size: 1.2em;
  color: #b3b3b3; /* Subtle gray for subtitle */
  margin-bottom: 20px;
}

.login-info-subtitle {
  font-size: 0.9em;
  color: #b3b3b3; /* Subtle gray for information text */
  margin-top: 20px;
}

.login-button {
  display: flex;
  align-items: center;
  justify-content: center; /* Center text and icon within button */
  color: white;
  background: linear-gradient(90deg, #1DB954, #1ed760); /* Gradient for button */
  border: none;
  padding: 12px 24px;
  border-radius: 25px;
  cursor: pointer;
  font-size: 16px;
}

.login-button:hover {
  background: linear-gradient(90deg, #1ed760, #1DB954); /* Hover effect */
}

.spotify-icon {
  width: 24px;
  height: 24px;
  margin-right: 12px;
}
</style>