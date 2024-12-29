<template>
    <div>
        <back-arrow/>
        <div class="privacy-container">
            <h1>Privacy Policy</h1>

            <section>
                <h2>1. Introduction</h2>
                <p>
                    Welcome to Pacelist! This Privacy Policy explains how we collect, use, and protect your data when you use our app. By using the app, you agree to the practices described in this Privacy Policy.
                </p>
            </section>

            <section>
                <h2>2. Data We Collect</h2>
                <p>
                    We access limited data from your Spotify account to provide personalized playlist functionality. This includes:
                </p>
                <ul>
                    <li>Your Spotify username.</li>
                    <li>Your liked songs and playlists.</li>
                    <li>Basic account information provided by Spotify (e.g., profile image, display name).</li>
                </ul>
                <p>
                    We do not collect or store any sensitive information, such as your Spotify login credentials.
                </p>
            </section>

            <section>
                <h2>3. How We Use Your Data</h2>
                <p>
                    The data we access is used solely to:
                </p>
                <ul>
                    <li>Create and manage playlists tailored to your preferences.</li>
                    <li>Provide personalized recommendations.</li>
                    <li>Enhance your user experience within the app.</li>
                </ul>
                <p>
                    We do not use your data for marketing, advertising, or sharing with third parties.
                </p>
            </section>

            <section>
                <h2>4. Data Storage</h2>
                <p>
                    We do not store any user data permanently. All session data is temporarily stored in memory while you use the app and is cleared upon logout or session expiration.
                </p>
            </section>

            <section>
                <h2>5. Third-Party Services</h2>
                <p>
                    This app uses the Spotify API to access your data. By using the app, you agree to Spotify’s <a href="https://www.spotify.com/legal/end-user-agreement/">Terms of Service</a> and <a href="https://www.spotify.com/legal/privacy-policy/">Privacy Policy</a>. We comply with Spotify’s guidelines to ensure your data is handled securely.
                </p>
            </section>

            <section>
                <h2>6. User Control</h2>
                <p>
                    You have full control over your data. You can:
                </p>
                <ul>
                    <li>Disconnect the app from your Spotify account at any time.</li>
                    <li>Revoke the app's permissions directly in your Spotify account settings.</li>
                </ul>
                <p>
                    Disconnecting the app will immediately stop all data access and remove all session data.
                </p>
            </section>

            <section>
                <h2>7. Data Security</h2>
                <p>
                    We take reasonable measures to ensure your data is secure while using the app. However, since the app does not store data permanently, risks are minimized.
                </p>
            </section>

            <section>
                <h2>8. Changes to This Privacy Policy</h2>
                <p>
                    We may update this Privacy Policy occasionally to reflect changes in functionality or legal requirements. Any updates will be posted within the app, and we encourage you to review the policy periodically.
                </p>
            </section>

            <section>
                <h2>Contact Us</h2>
                <p>
                    If you have questions or concerns about this Privacy Policy, please <a href="mailto:ibereciartua99@gmail.com">contact us</a>.
                </p>
            </section>

            <div class="back-button-container">
                <button @click="goBack" class="back-button">Back</button>
            </div>
        </div>

    </div>
</template>

<script>

import BackArrow from "@/components/BackArrow.vue";
export default {
    name: 'TermsAndConditions',
    components: {
        BackArrow
    },
    methods: {
        goBack() {
            this.$router.go(-1);
        }
    }
};
</script>

<style scoped>
.privacy-container {
    max-width: 800px;
    margin: 0 auto;
    padding: 20px;
    color: lightgray;
    font-family: Arial, sans-serif;
}

h1 {
    text-align: center;
    font-size: 2em;
    margin-bottom: 20px;
}

section {
    margin-bottom: 20px;
}

h2 {
    font-size: 1.5em;
    color: white;
    margin-bottom: 10px;
}

p {
    font-size: 1em;
    line-height: 1.6;
}

ul {
    list-style-type: disc;
    padding-left: 20px;
}

.back-button-container {
    text-align: center;
    margin-top: 20px;
}

.back-button {
    background-color: #1db954;
    color: white;
    padding: 10px 20px;
    border: none;
    border-radius: 25px;
    font-size: 1em;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

.back-button:hover {
    background-color: #1ed760;
}

a {
    color: #1db954;
    text-decoration: none;
}
</style>
