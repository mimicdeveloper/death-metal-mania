import { createRouter, createWebHistory } from 'vue-router'
import { useStore } from 'vuex'

import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import LogoutView from '../views/LogoutView.vue'
import RegisterView from '../views/RegisterView.vue'
import ContactView from '../views/ContactView.vue'
import UserView from '../views/UserView.vue'
import ProfileView from '../views/ProfileView.vue'
import UpdateProfileView from '../views/UpdateProfileView.vue'
import FavoritesView from '../views/FavoritesView.vue'
import AdminView from '../views/AdminView.vue'
import EventsView from '../views/EventsView.vue'

const routes = [
  { path: '/', name: 'home', component: HomeView, meta: { requiresAuth: false } },
  { path: '/login', name: 'login', component: LoginView, meta: { requiresAuth: false } },
  { path: '/logout', name: 'logout', component: LogoutView, meta: { requiresAuth: false } },
  { path: '/register', name: 'register', component: RegisterView, meta: { requiresAuth: false } },
  { path: '/contact', name: 'contact', component: ContactView, meta: { requiresAuth: false } },
  { path: '/user', name: 'user', component: UserView, meta: { requiresAuth: true } },
  { path: '/profile', name: 'profile', component: ProfileView, meta: { requiresAuth: true } },
  { path: '/update-profile', name: 'update-profile', component: UpdateProfileView, meta: { requiresAuth: true } },
  { path: '/favorites', name: 'favorites', component: FavoritesView, meta: { requiresAuth: true } },
  { path: '/admin', name: 'admin', component: AdminView, meta: { requiresAuth: true } },
  { path: '/events', name: 'events', component: EventsView, meta: { requiresAuth: true } }
]

const router = createRouter({
  // Add the base here:
  history: createWebHistory('/death-metal-mania/'),
  routes
});

router.beforeEach((to) => {
  const store = useStore();
  const requiresAuth = to.matched.some(x => x.meta.requiresAuth);
  const userRole = store.state.user?.role || null;

  if (requiresAuth && !store.state.token) {
    return { name: 'login' };
  }

  if (to.name === 'admin' && userRole !== 'ROLE_ADMIN') {
    return { name: 'home' };
  }

  const userRoutes = ['user', 'profile', 'update-profile', 'favorites', 'events'];
  if (userRoutes.includes(to.name) && !['ROLE_USER', 'ROLE_ADMIN'].includes(userRole)) {
    return { name: 'home' };
  }
});

export default router;
