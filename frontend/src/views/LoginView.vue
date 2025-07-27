<template>
  <main>
    <section id="login-section">
      <h2>Login</h2>
      <form id="login-form" @submit.prevent="login">
        <div class="input-group">
          <label for="loginUsername">Username:</label>
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
          <label for="loginPassword">Password:</label>
          <input
            type="password"
            id="loginPassword"
            name="password"
            v-model="user.password"
            required
            placeholder="Enter password"
          />
        </div>

        <!-- Status message -->
        <div v-if="status.message" :class="['status-message', status.type]">
          {{ status.message }}
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

export default {
  data() {
    return {
      user: {
        username: "",
        password: "",
      },
      status: {
        message: "",
        type: "", // "success" or "error"
      },
    };
  },
  methods: {
    error(msg) {
      this.status.message = msg;
      this.status.type = "error";
    },
    success(msg) {
      this.status.message = msg;
      this.status.type = "success";
    },
    login() {
      this.status.message = ""; // clear previous status

      authService
        .login(this.user)
        .then((response) => {
          if (response.status === 200) {
            this.success("Successfully logged in!");
            this.$store.commit("SET_AUTH_TOKEN", response.data.token);
            this.$store.commit("SET_USER", response.data.user);
            setTimeout(() => {
              this.$router.push("/");
            }, 3000); // delay redirect so user sees message
          }
        })
        .catch((error) => {
          const response = error.response;
          if (!response) {
            this.error("Network error. Please try again.");
          } else if (response.status === 401) {
            this.error("Invalid username or password!");
          } else {
            this.error(response.data?.message || "Login failed.");
          }
        });
    },
  },
};
</script>

<style scoped>
#login-section {
  max-width: 600px;
  margin: 3rem auto;
  background-color: #222;
  padding: 2rem;
  border-radius: 10px;
  color: white;
}

h2 {
  text-align: center;
  font-size: 1.8rem;
  margin-bottom: 2rem;
}

form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.input-group {
  display: flex;
  flex-direction: column;
}

label {
  margin-bottom: 0.5rem;
  font-weight: bold;
}

input {
  padding: 0.75rem 1rem;
  font-size: 1.1rem;
  border: 1px solid #444;
  border-radius: 6px;
  background-color: #727272;
  color: white;
}

input::placeholder {
  color: rgb(220, 220, 220);
}

button {
  align-self: center;
  padding: 0.75rem 1.5rem;
  background-color: crimson;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
}

button:hover {
  background-color: rgb(132, 12, 36);
}

hr {
  margin: 2rem 0 1rem 0;
  border: none;
  border-top: 1px solid #555;
}

.register-link {
  text-align: center;
  font-size: 1rem;
}

.register-link a {
  color: crimson;
  text-decoration: underline;
  font-weight: bold;
}

/* Status messages */
.status-message {
  margin-bottom: 1rem;
  padding: 0.75rem 1rem;
  border-radius: 6px;
  font-weight: bold;
  font-size: 1.1rem;
  text-align: center;
}

.status-message.success {
  background-color: #4caf50; /* green */
  color: white;
}

.status-message.error {
  background-color: #f44336; /* red */
  color: white;
}
</style>
