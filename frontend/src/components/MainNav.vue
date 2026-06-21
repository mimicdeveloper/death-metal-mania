<template>
  <nav class="main-nav">
    <div class="nav-inner">

      <!-- Brand -->
      <button class="nav-brand" @click="go('/')">☠ DMM</button>

      <!-- Desktop links -->
      <div class="nav-links">
        <button class="nav-link" @click="go('/')">Home</button>
        <button class="nav-link" @click="go('/contact')">Contact</button>

        <template v-if="isLoggedIn">
          <button class="nav-link" @click="go('/user')">Profile</button>
          <button class="nav-link" @click="go('/favorites')">Favorites</button>
          <button class="nav-link" @click="go('/events')">Events</button>
          <button v-if="isAdmin" class="nav-link" @click="go('/admin')">Admin</button>
          <button class="nav-link nav-link--active" @click="go('/logout')">Logout</button>
        </template>
        <template v-else>
          <button class="nav-link" @click="go('/register')">Register</button>
          <button class="nav-link nav-link--active" @click="go('/login')">Login</button>
        </template>
      </div>

      <!-- Hamburger -->
      <button class="hamburger" @click="menuOpen = !menuOpen" :class="{ open: menuOpen }" aria-label="Menu">
        <span></span><span></span><span></span>
      </button>
    </div>

    <!-- Mobile drawer -->
    <div class="mobile-menu" :class="{ open: menuOpen }" @click="menuOpen = false">
      <button class="mobile-link" @click="go('/')">Home</button>
      <button class="mobile-link" @click="go('/contact')">Contact</button>

      <template v-if="isLoggedIn">
        <button class="mobile-link" @click="go('/user')">Profile</button>
        <button class="mobile-link" @click="go('/favorites')">Favorites</button>
        <button class="mobile-link" @click="go('/events')">Events</button>
        <button v-if="isAdmin" class="mobile-link" @click="go('/admin')">Admin</button>
        <button class="mobile-link mobile-link--danger" @click="go('/logout')">Logout</button>
      </template>
      <template v-else>
        <button class="mobile-link" @click="go('/register')">Register</button>
        <button class="mobile-link mobile-link--cta" @click="go('/login')">Login</button>
      </template>
    </div>
  </nav>
</template>

<script>
import { useRouter } from 'vue-router';

export default {
  name: 'MainNav',
  setup() {
    const router = useRouter();
    return { router };
  },
  data() {
    return { menuOpen: false };
  },
  computed: {
    isLoggedIn() {
      return !!this.$store.state.token;
    },
    isAdmin() {
      return this.$store.state.user?.role === 'ROLE_ADMIN';
    },
  },
  methods: {
    go(path) {
      this.menuOpen = false;
      this.router.push(path);
    },
  },
};
</script>

<style scoped>
.main-nav {
  background-color: #0a0a0a;
  border-bottom: 1px solid #1a1a1a;
  position: sticky;
  top: 0;
  z-index: 100;
}

.nav-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1.5rem;
  height: 52px;
}

/* Brand */
.nav-brand {
  background: none;
  border: none;
  color: crimson;
  font-size: 1.1rem;
  font-weight: 900;
  letter-spacing: 0.12em;
  cursor: pointer;
  padding: 0;
  text-transform: uppercase;
  white-space: nowrap;
}

.nav-brand:hover {
  color: #ff2a4a;
}

/* Desktop links */
.nav-links {
  display: flex;
  align-items: center;
  gap: 0.25rem;
}

.nav-link {
  background: none;
  border: none;
  color: #aaa;
  font-size: 0.88rem;
  font-weight: 600;
  letter-spacing: 0.04em;
  text-transform: uppercase;
  cursor: pointer;
  padding: 0.45rem 0.85rem;
  border-radius: 6px;
  transition: color 0.15s, background 0.15s;
  white-space: nowrap;
}

.nav-link:hover {
  color: #fff;
  background: #161616;
}

.nav-link--active {
  color: crimson;
}

.nav-link--active:hover {
  color: #fff;
  background: crimson;
}

/* Hamburger */
.hamburger {
  display: none;
  flex-direction: column;
  justify-content: center;
  gap: 5px;
  background: none;
  border: none;
  cursor: pointer;
  padding: 0.25rem;
  width: 32px;
  height: 32px;
}

.hamburger span {
  display: block;
  width: 22px;
  height: 2px;
  background: #aaa;
  border-radius: 2px;
  transition: transform 0.2s, opacity 0.2s, background 0.2s;
}

.hamburger:hover span { background: crimson; }

.hamburger.open span:nth-child(1) { transform: translateY(7px) rotate(45deg); }
.hamburger.open span:nth-child(2) { opacity: 0; }
.hamburger.open span:nth-child(3) { transform: translateY(-7px) rotate(-45deg); }

/* Mobile drawer */
.mobile-menu {
  display: none;
  flex-direction: column;
  background: #0d0d0d;
  border-top: 1px solid #1a1a1a;
  padding: 0.5rem 0;
  max-height: 0;
  overflow: hidden;
  transition: max-height 0.25s ease;
}

.mobile-menu.open {
  max-height: 400px;
}

.mobile-link {
  background: none;
  border: none;
  color: #bbb;
  font-size: 1rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  cursor: pointer;
  padding: 0.85rem 1.5rem;
  text-align: left;
  transition: color 0.15s, background 0.15s;
}

.mobile-link:hover {
  color: #fff;
  background: #161616;
}

.mobile-link--cta { color: crimson; }
.mobile-link--cta:hover { background: crimson; color: #fff; }

.mobile-link--danger { color: #888; }
.mobile-link--danger:hover { color: crimson; background: #111; }

@media (max-width: 768px) {
  .nav-links { display: none; }
  .hamburger { display: flex; }
  .mobile-menu { display: flex; }
}
</style>
