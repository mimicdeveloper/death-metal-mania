<template>
  <main>
    <section id="registration-section">
      <h2>Create an Account</h2>
      <form id="registration-form" @submit.prevent="register">
        <div class="input-group">
          <label for="username">Username:</label>
          <input
            type="text"
            id="username"
            v-model="user.username"
            required
            placeholder="Enter username"
          />
        </div>

        <div class="input-group">
          <label for="firstName">First Name:</label>
          <input
            type="text"
            id="firstName"
            v-model="user.firstName"
            required
            placeholder="Enter first name"
          />
        </div>

        <div class="input-group">
          <label for="lastName">Last Name:</label>
          <input
            type="text"
            id="lastName"
            v-model="user.lastName"
            required
            placeholder="Enter last name"
          />
        </div>

        <div class="input-group">
          <label for="email">Email:</label>
          <input
            type="email"
            id="email"
            v-model="user.email"
            required
            placeholder="Enter email"
          />
        </div>

        <div class="input-group">
          <label for="password">Password:</label>
          <input
            type="password"
            id="password"
            v-model="user.password"
            required
            placeholder="Enter password"
          />
        </div>

        <div class="input-group">
          <label for="confirmPassword">Confirm Password:</label>
          <input
            type="password"
            id="confirmPassword"
            v-model="user.confirmPassword"
            required
            placeholder="Confirm password"
          />
        </div>

        <div class="input-group">
          <label for="role">Role:</label>
          <select id="role" v-model="user.role" required>
            <option value="">Select a Role</option>
            <option value="ROLE_USER">User</option>
            <option value="ROLE_ADMIN">Admin</option>
          </select>
        </div>

        <!-- Status message -->
        <div v-if="status.message" :class="['status-message', status.type]">
          {{ status.message }}
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

export default {
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
    register() {
      this.status.message = ""; // clear previous messages

      if (this.user.password !== this.user.confirmPassword) {
        this.error("Password & Confirm Password do not match");
        return;
      }

      authService
        .register(this.user)
        .then((response) => {
          if (response.status === 201) {
            this.success("Thank you for registering, please sign in.");
            setTimeout(() => {
              this.$router.push("/login");
            }, 3000); // delay redirect so user can read message
          }
        })
        .catch((error) => {
          const response = error.response;
          if (!response) {
            this.error("Network error. Please try again.");
          } else if (response.status === 400 && response.data.errors) {
            this.error("Please check your input and try again.");
          } else {
            this.error(response.data.message || "Registration failed.");
          }
        });
    },
  },
};
</script>

<style scoped>
#registration-section {
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

input,
select {
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

select {
  background-color: #727272;
  color: white;
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

.signin-link {
  text-align: center;
  font-size: 1rem;
}

.signin-link a {
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
