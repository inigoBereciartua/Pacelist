<template>
    <div>
        <back-arrow/>
        <div class="terms-container">
            <h1>Terms and Conditions</h1>

            <section>
                <h2>1. Acceptance of Terms</h2>
                <p>
                    By using this app, you agree to these Terms and Conditions. If you do not agree with these terms, please discontinue use of the app.
                </p>
            </section>

            <section>
                <h2>2. Data Collection and Usage</h2>
                <p>
                    This app connects to your Spotify account to access limited data necessary for core functionality, including:
                </p>
                <ul>
                    <li>Displaying your Spotify username.</li>
                    <li>Accessing your liked songs to create personalized playlists.</li>
                    <li>Creating and managing playlists based on your preferences.</li>
                </ul>
                <p>We do <strong>not</strong> store any data permanently. All sessions and data are stored temporarily in memory and are cleared upon logout or session expiration.</p>
            </section>

            <section>
                <h2>3. Session Management</h2>
                <p>
                    User sessions are stored in memory only for the duration of your activity within the app. Upon logout or session expiration, all session data is cleared, and you will need to reauthorize the app to reconnect with Spotify.
                </p>
            </section>

            <section>
                <h2>4. No Data Storage</h2>
                <p>
                    This app does not store any user data in a database or other permanent storage. No personal or Spotify data is retained after your session ends.
                </p>
            </section>

            <section>
                <h2>5. Third-Party Integrations</h2>
                <p>
                    This app uses the Spotify API to access your account data for essential functions only. We comply with Spotify's guidelines and terms, ensuring only necessary data is accessed with your permission.
                </p>
            </section>

            <section>
                <h2>6. User Responsibilities</h2>
                <p>
                    Users are responsible for keeping their Spotify login credentials secure and must follow Spotify’s terms of service. Any actions taken in the app are the responsibility of the user.
                </p>
            </section>

            <section>
                <h2>7. Limitations of Liability</h2>
                <p>
                    This app is provided "as-is" without any warranties, and we are not liable for any damages or losses resulting from the use of the app, including but not limited to errors, data loss, or unauthorized access to Spotify data.
                </p>
            </section>

            <section>
                <h2>8. User Control</h2>
                <p>
                    You have the right to disconnect from Spotify at any time. The app provides a way to do so, and you may also revoke access directly in your Spotify account settings.
                </p>
            </section>

            <section>
                <h2>9. Changes to Terms and Conditions</h2>
                <p>
                    We reserve the right to update these Terms and Conditions occasionally to reflect changes in functionality or data usage practices. We encourage you to review these terms periodically.
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
.terms-container {
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
</style>
