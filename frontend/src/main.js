import './assets/main.css';

import { createApp } from 'vue';
import MyApp from './App.vue';
import { createStore } from './store';
import router from './router';
import api from './api'; // ✅ Import your configured Axios instance

/* FontAwesome core */
import { library } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import {
  faCartPlus,
  faTrashCan,
  faXmark,
  faMagnifyingGlass,
  faRotate,
  faTable,
  faGrip
} from '@fortawesome/free-solid-svg-icons';

/* Add icons to the library */
library.add(
  faCartPlus,
  faTrashCan,
  faXmark,
  faMagnifyingGlass,
  faRotate,
  faTable,
  faGrip
);

/**
 * Check if a JWT token is expired.
 * @param {string} token - JWT token string
 * @returns {boolean} true if expired, false otherwise
 */
function isTokenExpired(token) {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    const now = Math.floor(Date.now() / 1000);
    return payload.exp < now;
  } catch (e) {
    console.error("Invalid token format:", e);
    return true;
  }
}

// Remove expired token and user info from localStorage on app load
const storedToken = localStorage.getItem('token');
if (storedToken && isTokenExpired(storedToken)) {
  console.log("Expired JWT found — removing from storage.");
  localStorage.removeItem('token');
  localStorage.removeItem('user');
}

// Grab token and user from localStorage (may be null if removed)
let currentToken = localStorage.getItem('token');
let currentUser = JSON.parse(localStorage.getItem('user'));

if (currentToken) {
  // ✅ Use your custom Axios instance to set the Authorization header
  api.defaults.headers.common['Authorization'] = `Bearer ${currentToken}`;
}

// Create Vuex store with stored credentials
const store = createStore(currentToken, currentUser);

// Create and mount Vue app
const app = createApp(MyApp);
app.use(store);
app.use(router);
app.component('font-awesome-icon', FontAwesomeIcon);
app.mount('#app');
