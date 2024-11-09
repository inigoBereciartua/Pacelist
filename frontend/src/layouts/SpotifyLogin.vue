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
            <div class="contact-section">
                <p>Have questions or feedback? Reach out:</p>
                <div class="contact-links">
                    <a href="mailto:ibereciartua99@gmail.com" class="contact-link">
                        <img src="@/assets/email.svg" alt="Email Icon" class="contact-icon" /> Email the Developer
                    </a>
                    |
                    <a href="https://www.linkedin.com/in/i%C3%B1igo-bereciartua-rocha/" target="_blank" class="contact-link">
                        <img src="@/assets/linkedin.svg" alt="LinkedIn Icon" class="contact-icon" /> LinkedIn
                    </a>
                </div>
            </div>
            <div class="repository-section">
                <a href="https://github.com/inigoBereciartua/Pacelist" target="_blank" class="contact-link">
                    <img src="@/assets/github.svg" alt="GitHub Icon" class="contact-icon" /> GitHub Repository
                </a>
            </div>
        </div>
    </div>
</template>

<script>
import { API_URL } from "@/api/utils";

export default {
    name: 'SpotifyLogin',
    methods: {
        loginWithSpotify() {
            window.location.href = `${API_URL}/auth/login?redirectUri=` + window.location.origin + '/playlist-info-form';
        }
    },
    mounted() {
        const urlParams = new URLSearchParams(window.location.search);
        const token = urlParams.get('token');
        if (token) {
            localStorage.setItem('access_token', token);
            this.$router.push('/playlist-info-form');
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

.contact-section {
    margin-top: 20px;
    color: #b3b3b3;
    font-size: 0.9em;
    text-align: center;
}


.contact-link:hover {
    text-decoration: underline;
}

.contact-links {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-top: 10px;
}
.contact-link {
    display: flex;
    align-items: center;
    color: #1DB954;
    text-decoration: none;
    margin: 0 5px;
}

.contact-icon {
    width: 16px;
    height: 16px;
    margin-right: 5px;
    filter: brightness(0) saturate(100%) invert(44%) sepia(55%) saturate(262%) hue-rotate(89deg) brightness(95%) contrast(91%);
}

.repository-section {
    margin-top: 20px;
    font-size: 0.9em;
    text-align: center;
}
</style>
