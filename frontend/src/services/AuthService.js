import axios from 'axios';

const api = axios.create({
  baseURL: 'https://tragic-greta-mimicdev-d365cdd2.koyeb.app',
});

export default {
  register(user) {
    return api.post('/register', user); // exact match with backend mapping
  },
  login(user) {
    return api.post('/login', user);
  }
};
