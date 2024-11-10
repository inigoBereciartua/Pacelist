const PROFILE_URL = '/api/profile';

const getData = async (context) => {
    return new Promise((resolve, reject) => {
        context.$axios.get(PROFILE_URL)
            .then(response => {
                resolve(response.data);
            })
            .catch(error => {
                reject(error);
            });
    });
}

export default {
    getData
}