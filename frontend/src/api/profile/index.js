

const getData = async (context) => {
    // Return a promise
    return new Promise((resolve, reject) => {
        // Get the user data
        context.$axios.get('/api/profile')
            .then(response => {
                // Resolve the promise
                resolve(response.data);
            })
            .catch(error => {
                // Reject the promise
                reject(error);
            });
    });
}

export default {
    getData
}