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
                <h2>2. Definitions</h2>
                <p>
                    - "App" refers to Pacelist, the application you are using.<br>
                    - "Spotify API" refers to the interface provided by Spotify to access certain data.<br>
                    - "User Data" refers to data obtained from your Spotify account, including playlists, liked songs, and account information.<br>
                    - "Session Data" refers to temporary data stored during your use of the app.
                </p>
            </section>

            <section>
                <h2>3. Data Collection and Usage</h2>
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
                <h2>4. User Consent</h2>
                <p>
                    By logging into the app with your Spotify account, you consent to the access, use, and processing of your data as outlined in these Terms and Conditions.
                </p>
            </section>

            <section>
                <h2>5. Privacy Policy</h2>
                <p>
                    Please refer to our <a href="/privacy-policy">Privacy Policy</a> for detailed information about how we handle and protect your data.
                </p>
            </section>

            <section>
                <h2>6. Session Management</h2>
                <p>
                    User sessions are stored in memory only for the duration of your activity within the app. Upon logout or session expiration, all session data is cleared, and you will need to reauthorize the app to reconnect with Spotify.
                </p>
            </section>

            <section>
                <h2>7. No Data Storage</h2>
                <p>
                    This app does not store any user data in a database or other permanent storage. No personal or Spotify data is retained after your session ends.
                </p>
            </section>

            <section>
                <h2>8. Third-Party Integrations</h2>
                <p>
                    This app uses the Spotify API to access your account data for essential functions only. We comply with Spotify's guidelines and terms, ensuring only necessary data is accessed with your permission.
                </p>
            </section>

            <section>
                <h2>9. User Responsibilities</h2>
                <p>
                    Users are responsible for keeping their Spotify login credentials secure and must follow Spotify’s terms of service. Any actions taken in the app are the responsibility of the user.
                </p>
            </section>

            <section>
                <h2>10. Limitations of Liability</h2>
                <p>
                    This app is provided "as-is" without any warranties, and we are not liable for any damages or losses resulting from the use of the app, including but not limited to errors, data loss, or unauthorized access to Spotify data.
                </p>
            </section>

            <section>
                <h2>11. User Control</h2>
                <p>
                    You have the right to disconnect from Spotify at any time. The app provides a way to do so, and you may also revoke access directly in your Spotify account settings.
                </p>
            </section>

            <section>
                <h2>12. Governing Law and Dispute Resolution</h2>
                <p>
                    These Terms and Conditions shall be governed by and construed in accordance with the laws of Spain. Any disputes arising out of or relating to these terms shall be resolved in the courts of Spain.
                </p>
            </section>

            <section>
                <h2>13. Termination of Service</h2>
                <p>
                    We reserve the right to terminate or restrict access to the app at our sole discretion if we believe you have violated these terms or Spotify’s terms of service.
                </p>
            </section>

            <section>
                <h2>14. Disclaimer</h2>
                <p>
                    This app is not affiliated with Spotify. Spotify is not responsible for any issues or liabilities arising from the use of this app.
                </p>
            </section>

            <section>
                <h2>15. Changes to Terms and Conditions</h2>
                <p>
                    We reserve the right to update these Terms and Conditions occasionally to reflect changes in functionality or data usage practices. We encourage you to review these terms periodically.
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

a {
    color: #1db954;
    text-decoration: none;
}
</style>
