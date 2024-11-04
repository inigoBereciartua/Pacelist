# Pacelist 🎧 The Running Playlist Generator for Spotify
A smart app that curates the perfect playlist for your running sessions by syncing with your Spotify account and analyzing your pace and running goals.

## Try It Out
You can test the current version at [pacelist.ibereciartua.com](https://pacelist.ibereciartua.com). **Note:** Due to pending production approval from Spotify, users need to provide an email to request access before logging in.

## Features
- 🎯 **Personalized Playlists:** Tailored song recommendations based on your running goals and preferences.
- 🎵 **Spotify Integration:** Leverages your saved Spotify tracks to match the ideal BPM for your pace.
- 🏃 **Optimized Running Experience:** Keeps you motivated and on track with music that supports your goals.
## Prerequisites
Before starting, ensure you have the following installed:
- **Java 21**
- **Maven 3.6.3**
- **Node.js 19.9.0**
- **npm 9.6.3**

Additionally, [create a Spotify Developer account](https://developer.spotify.com/dashboard) to access the Spotify API. Set up a new app and get the client ID and client secret.

## Getting Started
Follow the steps below to set up the project locally:

1. Clone the Repository
    ```bash
    git clone https://github.com/yourusername/spotify-running-playlist.git
    cd spotify-running-playlist
    ```
2. Backend Setup (Spring Boot)
- Navigate to the backend folder:
    ```bash
    cd backend
    ```
- Export the Spotify client ID and client secret as environment variables:
    ```bash
    export SPOTIFY_CLIENT_ID=your_client_id
    export SPOTIFY_CLIENT_SECRET=your_client_secret
    ```
- Install the dependencies:
    ```bash
    mvn clean install
    ```

- Run the Spring Boot application:
    ```bash
    mvn spring-boot:run
    ```
3. Frontend Setup (VueJS)
- Navigate to the frontend folder:
    ```bash
    cd frontend
    ```
- Install the dependencies:
    ```bash
    npm install
    ```
- Run the VueJS application:
    ```bash
    npm run serve
    ```

## Usage
Once the project is up and running, open your browser and navigate to the provided local URL. Log in using your Spotify account, provide your running details, and start generating playlists that match your ideal running pace.

Contributing
Feel free to submit pull requests or open issues if you find bugs or have feature suggestions. Contributions are welcome!

License
This project is licensed under the MIT License.