import { getToken } from '@/api/auth';
export const API_URL = process.env.BACKEND_API_URL || 'http://localhost:8080';
const AUTH_HEADER_PREFIX = 'Bearer ';
export const fetchWrapper = async (url, options) => {
    const token = getToken();
    if (options === undefined) {
        options = {};
    }
    if (token) {
        options.headers = {
            ...options.headers,
            Authorization: `${AUTH_HEADER_PREFIX} ${token}`
        };
    }
    const response = await fetch(url, options);
    if (!response.ok) {
        throw new Error(response.statusText);
    }
    return response.json();
}