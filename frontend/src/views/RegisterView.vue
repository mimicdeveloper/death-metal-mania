<template>
  <main>
    <section id="registration-section">
      <h2>Create Account</h2>
      <form id="registration-form" @submit.prevent="register">
        <div class="input-group">
          <label for="username">Username</label>
          <input type="text" id="username" v-model="user.username" required placeholder="Enter username" />
        </div>
        <div class="input-group">
          <label for="firstName">First Name</label>
          <input type="text" id="firstName" v-model="user.firstName" required placeholder="Enter first name" />
        </div>
        <div class="input-group">
          <label for="lastName">Last Name</label>
          <input type="text" id="lastName" v-model="user.lastName" required placeholder="Enter last name" />
        </div>
        <div class="input-group">
          <label for="email">Email</label>
          <input type="email" id="email" v-model="user.email" required placeholder="Enter email" />
        </div>
        <div class="input-group">
          <label for="password">Password</label>
          <input type="password" id="password" v-model="user.password" required placeholder="Enter password" />
        </div>
        <div class="input-group">
          <label for="confirmPassword">Confirm Password</label>
          <input type="password" id="confirmPassword" v-model="user.confirmPassword" required placeholder="Confirm password" />
        </div>
        <div class="input-group">
          <label for="role">Role</label>
          <select id="role" v-model="user.role" required>
            <option value="">Select a Role</option>
            <option value="ROLE_USER">User</option>
            <option value="ROLE_ADMIN">Admin</option>
          </select>
        </div>

        <button type="submit">Register</button>

        <hr />
        <p class="signin-link">
          Have an account?
          <router-link to="/login">Sign in!</router-link>
        </p>
      </form>
    </section>
  </main>
</template>

<script>
import authService from "@/services/AuthService";
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
        confirmPassword: "",
        role: "",
        firstName: "",
        lastName: "",
        email: "",
      },
    };
  },
  methods: {
    register() {
      if (this.user.password !== this.user.confirmPassword) {
        this.toast.error("Passwords do not match.");
        return;
      }

      authService
        .register(this.user)
        .then((response) => {
          if (response.status === 201) {
            this.toast.success("Account created! Redirecting to login...");
            setTimeout(() => {
              this.$router.push("/login");
            }, 1500);
          }
        })
        .catch((error) => {
          const response = error.response;
          if (!response) {
            this.toast.error("Network error. Please try again.");
          } else if (response.status === 400 && response.data.errors) {
            this.toast.error("Please check your input and try again.");
          } else {
            this.toast.error(response.data.message || "Registration failed.");
          }
        });
    },
  },
};
</script>

<style scoped>
#registration-section {
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
  gap: 1.1rem;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 0.35rem;
}

label {
  font-weight: 600;
  font-size: 0.85rem;
  color: #aaa;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

input,
select {
  padding: 0.7rem 1rem;
  font-size: 1rem;
  border: 1px solid #333;
  border-radius: 6px;
  background-color: #1c1c1c;
  color: white;
  transition: border-color 0.2s;
}

input:focus,
select:focus {
  outline: none;
  border-color: crimson;
}

input::placeholder {
  color: #555;
}

select option {
  background-color: #1c1c1c;
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
  margin-top: 0.5rem;
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

.signin-link {
  text-align: center;
  font-size: 0.95rem;
  color: #888;
}

.signin-link a {
  color: crimson;
  text-decoration: none;
  font-weight: 700;
}

.signin-link a:hover {
  text-decoration: underline;
}
</style>
