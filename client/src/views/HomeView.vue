<template>
  <div class="home-container">

    <main>
      <SearchBands />
      <hr class="section-divider" />
      <SearchEvents />
    </main>

  </div>
</template>

<script>
import SearchBands from '../components/SearchBands.vue';
import SearchEvents from '../components/SearchEvents.vue';

export default {
  name: 'HomeView',
  components: {
    SearchBands,
    SearchEvents,
  }
};
</script>

<style scoped>
.home-container {
  background-color: #000000;
  color: #ffffff;
  min-height: 100vh;
  font-family: 'Arial', sans-serif;
  font-size: 18px;
}

main {
  display: grid;
  grid-template-columns: 1fr;
  gap: 2rem;
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
  box-sizing: border-box;
}

.section-divider {
  margin: 2rem 0;
  border: none;
  border-top: 2px solid #444;
}
</style>
