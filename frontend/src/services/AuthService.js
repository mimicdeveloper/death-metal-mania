// services/AuthService.js

import api from '../api.js';  // your configured Axios instance

export default {
  login(user) {
    return api.post('/login', user);
  },
  register(user) {
    return api.post('/register', user);
  }
}
