import axios from 'axios';

const api = axios.create({
  baseURL:
    window.location.hostname === 'localhost'
      ? 'http://localhost:9000'
      : 'https://tragic-greta-mimicdev-d365cdd2.koyeb.app',
});

export default api;
