const API_BASE = 'http://localhost:8080';

/*
 * 경로가 / 로 끝나는 엔드포인트의 경우 특정 파라미터 값을
 * / 뒤쪽에 붙야줘야함
 */
export const ENDPOINTS = {
    LOGIN: `${API_BASE}/login`,
    SIGNUP: `${API_BASE}/signup`,
    USER_UPDATE: `${API_BASE}/update/password`,
    CREATE_SHORTEN_URL: `${API_BASE}/url/user/create`,
    URL_LIST_FOR_USER: `${API_BASE}/url/user/all/`,
    DELETE_URL: `${API_BASE}/url/delete/`, // urlId 값이 뒤에 붙어야함
    URL_LIST_FOR_ADMIN: `${API_BASE}/url/admin/all`,
    URL_UPDATE: `${API_BASE}/url/admin/update`,
    URL_LIST_BY_EMAIL_FOR_ADMIN: `${API_BASE}/url/admin/all/` // email 값이 뒤에 붙어야함
};