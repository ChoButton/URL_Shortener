const API_BASE = 'http://localhost:8080';

export const ENDPOINTS = {
    LOGIN: `${API_BASE}/login`,
    SIGNUP: `${API_BASE}/signup`,
    USER_UPDATE: `${API_BASE}/update/password`,
    CREATE_SHORTEN_URL: `${API_BASE}/url/user/create`,
    URL_LIST_FOR_USER: `${API_BASE}/url/user/all/`,
    DELETE_URL: `${API_BASE}/url/delete/`,
    URL_LIST_FOR_ADMIN: `${API_BASE}/url/admin/all`,
    URL_UPDATE: `${API_BASE}/url/admin/update`
};