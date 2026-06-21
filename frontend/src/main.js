import './assets/main.css';
import 'vue-toastification/dist/index.css';

import { createApp } from 'vue';
import MyApp from './App.vue';
import { createStore } from './store';
import router from './router';
import api from './api';
import Toast from 'vue-toastification';

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

library.add(
  faCartPlus,
  faTrashCan,
  faXmark,
  faMagnifyingGlass,
  faRotate,
  faTable,
  faGrip
);

const toastOptions = {
  position: 'top-right',
  timeout: 3500,
  closeOnClick: true,
  pauseOnHover: true,
  draggable: true,
  showCloseButtonOnHover: false,
  hideProgressBar: false,
  closeButton: 'button',
  icon: true,
  toastClassName: 'dmm-toast',
};

function isTokenExpired(token) {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    const now = Math.floor(Date.now() / 1000);
    return payload.exp < now;
  } catch (e) {
    return true;
  }
}

const storedToken = localStorage.getItem('token');
if (storedToken && isTokenExpired(storedToken)) {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
}

let currentToken = localStorage.getItem('token');
let currentUser = JSON.parse(localStorage.getItem('user'));

if (currentToken) {
  api.defaults.headers.common['Authorization'] = `Bearer ${currentToken}`;
}

const store = createStore(currentToken, currentUser);

const app = createApp(MyApp);
app.use(store);
app.use(router);
app.use(Toast, toastOptions);
app.component('font-awesome-icon', FontAwesomeIcon);
app.mount('#app');
