<template>
  <div class="home-view">
    <DiscoverSection />
    <div class="section-divider"></div>
    <SearchBands />
    <div class="section-divider"></div>
    <SearchEvents />
  </div>
</template>

<script>
import SearchBands from '../components/SearchBands.vue';
import SearchEvents from '../components/SearchEvents.vue';
import DiscoverSection from '../components/DiscoverSection.vue';

export default {
  name: 'HomeView',
  components: { SearchBands, SearchEvents, DiscoverSection }
};
</script>

<style scoped>
.home-view {
  display: flex;
  flex-direction: column;
  gap: 0;
}

.section-divider {
  height: 1px;
  background: #1a1a1a;
  margin: 2rem 0;
}
</style>
