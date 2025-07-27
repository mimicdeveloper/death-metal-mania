<template>
    <main>
      <section class="profile-section">
        <h2>User Profile</h2>
        <div id="profileContainer" class="profile-container" v-if="profile">
          <p><strong>Profile ID:</strong> {{ profile.profileId }}</p>
          <p><strong>User ID:</strong> {{ profile.userId }}</p>
          <p><strong>First Name:</strong> {{ profile.firstName }}</p>
          <p><strong>Last Name:</strong> {{ profile.lastName }}</p>
          <p><strong>Email:</strong> {{ profile.email }}</p>
        </div>
      </section>
    </main>
  </template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      profile: null
    };
  },
  created() {
    const token = localStorage.getItem('token');
    if (!token) {
      this.$router.push('/login');
      return;
    }

    axios.get('http://localhost:9000/user', {
      headers: { Authorization: `Bearer ${token}` }
    })
      .then(response => {
        this.profile = response.data;
      })
      .catch(error => {
        console.error('Failed to fetch profile:', error);
        localStorage.removeItem('token');
        this.$router.push('/login');
      });
  }
}
</script>


<style scoped>
.profile-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 2rem;
  color: #fff;
}

.profile-section h2 {
  font-size: 1.8rem;
  font-weight: bold;
  margin-bottom: 1.5rem;
  text-align: center;
  color: #fff;
  letter-spacing: 1px;
}

.profile-container {
  background-color: #2b2b2b;
  padding: 1.5rem 2rem;
  border-radius: 8px;
  max-width: 400px;
  width: 100%;
  box-shadow: 0 2px 8px rgba(0,0,0,0.5);
}

.profile-container p {
  margin: 0.5rem 0;
}

.profile-container strong {
  color: #ccc;
}

</style>
