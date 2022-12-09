const API_ENDPOINT = location.hostname === 'localhost' ?
    `${location.protocol}//${location.hostname}:8080/api` : `${location.protocol}//${location.hostname}/api`;

if (location.hostname.includes('localhost') && !!sessionStorage.getItem('dev-auth')) {
    axios.defaults.headers.common['Authorization'] = `Basic ${window.btoa(sessionStorage.getItem('dev-auth'))}`;
}

const apiClient = axios.create({
    baseURL: `${API_ENDPOINT}`
});

apiClient.interceptors.response.use(response => {
    return response;
}, error => {
    alert(error.message);
    return Promise.reject(error);
});