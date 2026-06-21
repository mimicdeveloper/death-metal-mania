<template>
  <div class="home-container">
    <main>
      <DiscoverSection />
      <hr class="section-divider" />
      <SearchBands />
      <hr class="section-divider" />
      <SearchEvents />
    </main>
  </div>
</template>

<script>
import SearchBands from '../components/SearchBands.vue';
import SearchEvents from '../components/SearchEvents.vue';
import DiscoverSection from '../components/DiscoverSection.vue';

export default {
  name: 'HomeView',
  components: {
    SearchBands,
    SearchEvents,
    DiscoverSection,
  }
};
</script>

<style scoped>
.home-container {
  background-color: #0a0a0a;
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
  margin: 0;
  border: none;
  border-top: 1px solid #1e1e1e;
}
</style>
