
const ACCESS_TOKEN = 'access_token';

const saveToken = (token) => {
    localStorage.setItem(ACCESS_TOKEN, token);
}

const getToken = () => {
    return localStorage.getItem(ACCESS_TOKEN);
}

const logout = () => {
    localStorage.removeItem(ACCESS_TOKEN);
}

const tokenInterceptor = (config) => {
    const token = getToken();
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
}

export {
    getToken,
    saveToken,
    logout,
    tokenInterceptor
}