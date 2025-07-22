<template>
  <main>
    <section class="user-section">
      <h2>User Dashboard</h2>
      <div class="dashboard-buttons">
        <button @click="viewProfile" class="dashboard-btn">View Profile</button>
        <button @click="updateProfile" class="dashboard-btn">Update Profile</button>
        <button @click="viewFavorites" class="dashboard-btn">View Favorite Bands</button>
        <button @click="viewEvents" class="dashboard-btn">View Favorite Events</button>
      </div>
    </section>
  </main>
</template>

<script>
export default {
  methods: {
    viewProfile() {
      this.$router.push({ name: 'profile' });
    },
    updateProfile() {
      this.$router.push({ name: 'update-profile' });
    },
    viewFavorites() {
      this.$router.push({ name: 'favorites' });
    },
    viewEvents() {
      this.$router.push({ name: 'events' }); 
    },
  },
};
</script>

<style scoped>
.user-section {
  text-align: center;
  margin-top: 2rem;
}

h2 {
  font-size: 2rem;
  margin-bottom: 2rem;
}

.dashboard-buttons {
  display: flex;
  justify-content: center;
  gap: 2rem;
  flex-wrap: wrap;
  margin-top: 1rem;
}

.dashboard-btn {
  padding: 0.75rem 2rem;
  background-color: crimson;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.dashboard-btn:hover {
  background-color: rgb(132, 12, 36);
}
</style>
