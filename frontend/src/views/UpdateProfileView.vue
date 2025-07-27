<template>
  <main>
    <section id="updateProfileSection">
      <h2>Update Profile</h2>
      <form @submit.prevent="updateProfile">
        <label for="email">Email:</label>
        <input type="email" id="email" v-model="email" required />

        <label for="firstName">First Name:</label>
        <input type="text" id="firstName" v-model="firstName" required />

        <label for="lastName">Last Name:</label>
        <input type="text" id="lastName" v-model="lastName" required />

        <button type="submit">Update</button>
      </form>

      <div v-if="status.message" :class="['status-message', status.type]">
        {{ status.message }}
      </div>
    </section>
  </main>
</template>

<script>
import api from '@/api';

export default {
  name: 'UpdateProfileView',
  data() {
    return {
      email: '',
      firstName: '',
      lastName: '',
      status: {
        message: '',
        type: '', // 'success' or 'error'
      }
    };
  },
  async created() {
    const token = localStorage.getItem('token');
    if (!token) {
      this.$router.push('/login');
      return;
    }

    try {
      const response = await api.get('/user', {
        headers: { Authorization: `Bearer ${token}` }
      });
      const { email, firstName, lastName } = response.data;
      this.email = email;
      this.firstName = firstName;
      this.lastName = lastName;
    } catch (error) {
      console.error('Failed to load profile:', error);
      localStorage.removeItem('token');
      this.$router.push('/login');
    }
  },
  methods: {
    async updateProfile() {
      try {
        const token = this.$store.state.token || localStorage.getItem('token');
        const profileData = {
          firstName: this.firstName,
          lastName: this.lastName,
          email: this.email,
        };

        await api.put('/user', profileData, {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        });

        this.status.message = 'Profile updated successfully.';
        this.status.type = 'success';
      } catch (err) {
        console.error(err);
        if (err.response && err.response.data) {
          this.status.message = `Failed to update profile: ${err.response.data.message || 'Unknown error'}`;
        } else {
          this.status.message = 'Failed to update profile.';
        }
        this.status.type = 'error';
      }
    }
  }
};
</script>


<style scoped>
#updateProfileSection {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 2rem;
  color: #fff;
}

h2 {
  font-size: 2rem;
  margin-bottom: 1.5rem;
  text-align: center;
  color: #fff;
  font-weight: bold;
}

form {
  background-color: #2b2b2b;
  padding: 2rem;
  border-radius: 8px;
  max-width: 400px;
  width: 100%;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.5);
}

form label {
  display: block;
  margin: 0.5rem 0 0.25rem;
}

form input {
  width: 100%;
  padding: 0.5rem;
  margin-bottom: 1rem;
  background: #1a1a1a;
  color: #fff;
  border: 1px solid #444;
  border-radius: 4px;
}

form button {
  background-color: crimson;
  color: white;
  padding: 0.75rem 1.5rem;
  font-size: 1.1rem;
  font-weight: 600;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  display: block;
  margin: 1rem auto 0;
}

form button:hover {
  background-color: #a30000;
}

/* Status message styling */
.status-message {
  margin-top: 1rem;
  padding: 0.75rem 1rem;
  border-radius: 6px;
  font-weight: bold;
  font-size: 1.1rem;
  text-align: center;
}

.status-message.success {
  background-color: #4caf50;
  color: white;
}

.status-message.error {
  background-color: #f44336;
  color: white;
}
</style>
