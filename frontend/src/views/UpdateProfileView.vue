<template>
  <main>
    <section id="updateProfileSection">
      <h2>Update Profile</h2>
      <form @submit.prevent="updateProfile">
        <div class="input-group">
          <label for="email">Email</label>
          <input type="email" id="email" v-model="email" required placeholder="Enter email" />
        </div>
        <div class="input-group">
          <label for="firstName">First Name</label>
          <input type="text" id="firstName" v-model="firstName" required placeholder="Enter first name" />
        </div>
        <div class="input-group">
          <label for="lastName">Last Name</label>
          <input type="text" id="lastName" v-model="lastName" required placeholder="Enter last name" />
        </div>
        <button type="submit">Update</button>
      </form>
    </section>
  </main>
</template>

<script>
import api from '@/api';
import { useToast } from 'vue-toastification';

export default {
  name: 'UpdateProfileView',
  setup() {
    const toast = useToast();
    return { toast };
  },
  data() {
    return {
      email: '',
      firstName: '',
      lastName: '',
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
        await api.put(
          '/user',
          { firstName: this.firstName, lastName: this.lastName, email: this.email },
          { headers: { Authorization: `Bearer ${token}`, 'Content-Type': 'application/json' } }
        );
        this.toast.success('Profile updated successfully!');
      } catch (err) {
        console.error(err);
        const msg = err.response?.data?.message || 'Failed to update profile.';
        this.toast.error(msg);
      }
    }
  }
};
</script>

<style scoped>
#updateProfileSection {
  max-width: 480px;
  margin: 3rem auto;
  background-color: #111;
  padding: 2.5rem;
  border-radius: 12px;
  border: 1px solid #2a2a2a;
  color: #fff;
}

h2 {
  font-size: 1.8rem;
  margin-bottom: 2rem;
  text-align: center;
  font-weight: 900;
  text-transform: uppercase;
  letter-spacing: 0.05em;
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

input {
  padding: 0.7rem 1rem;
  font-size: 1rem;
  background: #1c1c1c;
  color: #fff;
  border: 1px solid #333;
  border-radius: 6px;
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
  background-color: crimson;
  color: white;
  padding: 0.75rem 2rem;
  font-size: 1rem;
  font-weight: 700;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  transition: background-color 0.2s, transform 0.1s;
  margin-top: 0.5rem;
}

button:hover {
  background-color: #a30000;
}

button:active {
  transform: scale(0.97);
}
</style>
