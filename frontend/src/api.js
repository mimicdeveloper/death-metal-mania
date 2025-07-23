import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.MODE === 'production'
    ? 'https://tragic-greta-mimicdev-d365cdd2.koyeb.app'
    : 'http://localhost:9000',
});

export default api;
