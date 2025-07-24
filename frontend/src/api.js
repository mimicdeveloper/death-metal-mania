import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_REMOTE_API || 'http://localhost:9000',
});

export default api;