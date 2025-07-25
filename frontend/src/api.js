import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_REMOTE_API,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Optional: Automatically attach token if available
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
