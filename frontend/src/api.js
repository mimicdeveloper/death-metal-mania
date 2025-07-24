import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_REMOTE_API, // pulls from .env file
  headers: {
    'Content-Type': 'application/json'
  }
});

export default api;
