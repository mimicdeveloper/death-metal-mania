<template>
  <main>
    <section id="login-section">
      <h2>Login</h2>
      <form id="login-form" @submit.prevent="login">
        <div class="input-group">
          <label for="loginUsername">Username</label>
          <input
            type="text"
            id="loginUsername"
            name="username"
            v-model="user.username"
            required
            placeholder="Enter username"
          />
        </div>

        <div class="input-group">
          <label for="loginPassword">Password</label>
          <input
            type="password"
            id="loginPassword"
            name="password"
            v-model="user.password"
            required
            placeholder="Enter password"
          />
        </div>

        <button type="submit">Login</button>

        <hr />
        <p class="register-link">
          Need an account?
          <router-link to="/register">Register!</router-link>
        </p>
      </form>
    </section>
  </main>
</template>

<script>
import authService from "../services/AuthService";
import { useToast } from "vue-toastification";

export default {
  setup() {
    const toast = useToast();
    return { toast };
  },
  data() {
    return {
      user: {
        username: "",
        password: "",
      },
    };
  },
  methods: {
    login() {
      authService
        .login(this.user)
        .then((response) => {
          if (response.status === 200) {
            this.toast.success("Successfully logged in!");
            this.$store.commit("SET_AUTH_TOKEN", response.data.token);
            this.$store.commit("SET_USER", response.data.user);
            setTimeout(() => {
              this.$router.push("/");
            }, 1000);
          }
        })
        .catch((error) => {
          const response = error.response;
          if (!response) {
            this.toast.error("Network error. Please try again.");
          } else if (response.status === 401) {
            this.toast.error("Invalid username or password!");
          } else {
            this.toast.error(response.data?.message || "Login failed.");
          }
        });
    },
  },
};
</script>

<style scoped>
#login-section {
  max-width: 480px;
  margin: 3rem auto;
  background-color: #111;
  padding: 2.5rem;
  border-radius: 12px;
  border: 1px solid #2a2a2a;
  color: white;
}

h2 {
  text-align: center;
  font-size: 1.8rem;
  margin-bottom: 2rem;
  font-weight: 900;
  letter-spacing: 0.05em;
  text-transform: uppercase;
}

form {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 0.4rem;
}

label {
  font-weight: 600;
  font-size: 0.9rem;
  color: #aaa;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

input {
  padding: 0.75rem 1rem;
  font-size: 1rem;
  border: 1px solid #333;
  border-radius: 6px;
  background-color: #1c1c1c;
  color: white;
  transition: border-color 0.2s;
}

input:focus {
  outline: none;
  border-color: crimson;
}

input::placeholder {
  color: #555;
}

button {
  align-self: center;
  padding: 0.75rem 2rem;
  background-color: crimson;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 1rem;
  font-weight: 700;
  cursor: pointer;
  letter-spacing: 0.05em;
  text-transform: uppercase;
  transition: background-color 0.2s, transform 0.1s;
}

button:hover {
  background-color: #a30000;
}

button:active {
  transform: scale(0.97);
}

hr {
  margin: 1.5rem 0 1rem;
  border: none;
  border-top: 1px solid #2a2a2a;
}

.register-link {
  text-align: center;
  font-size: 0.95rem;
  color: #888;
}

.register-link a {
  color: crimson;
  text-decoration: none;
  font-weight: 700;
}

.register-link a:hover {
  text-decoration: underline;
}
</style>
