const PLAYLIST_URL = '/api/playlist';

const getPlaylistProposal = async (context, pace, distance, height) => {
    return new Promise((resolve, reject) => {
        context.$axios.get(`${PLAYLIST_URL}?pace=${pace}&distance=${distance}&height=${height}`)
            .then(response => {
                resolve(response.data);
            })
            .catch(error => {
                reject(error);
            });
    });
};

const savePlaylist = async (context, playlist) => {
    return new Promise((resolve, reject) => {
        context.$axios.post(PLAYLIST_URL, playlist)
            .then(response => {
                resolve(response.data);
            })
            .catch(error => {
                reject(error);
            });
    });
};

export default {
    getPlaylistProposal,
    savePlaylist
}