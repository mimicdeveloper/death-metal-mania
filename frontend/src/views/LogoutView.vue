<template>
  <main id="logout-section">
    <h1>Logout</h1>
    <p v-if="status.message" :class="['status-message', status.type]">
      {{ status.message }}
    </p>
  </main>
</template>

<script>
export default {
  data() {
    return {
      status: {
        message: "",
        type: "",
      },
    };
  },
  created() {
    this.$store.commit("LOGOUT");
    this.status.message = "You have been logged out.";
    this.status.type = "success";

    // Redirect after 3 seconds so user sees the message
    setTimeout(() => {
      this.$router.push("/login");
    }, 3000);
  },
};
</script>

<style scoped>
#logout-section {
  max-width: 600px;
  margin: 3rem auto;
  padding: 2rem;
  text-align: center;
  background-color: #222;
  border-radius: 10px;
  color: white;
}

h1 {
  font-size: 2rem;
  margin-bottom: 1.5rem;
}

.status-message {
  font-weight: bold;
  font-size: 1.2rem;
  padding: 0.75rem 1rem;
  border-radius: 6px;
}

.status-message.success {
  background-color: #4caf50; /* green */
  color: white;
}
</style>
