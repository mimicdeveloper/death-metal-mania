<template>
  <div id="death-metal-mania">
    <GlobalHeader />
    <MainNav />
    <div class="page-content">
      <router-view />
    </div>
    <GlobalFooter />
  </div>
</template>

<script>
import GlobalHeader from './components/GlobalHeader.vue';
import GlobalFooter from './components/GlobalFooter.vue';
import MainNav from './components/MainNav.vue';

export default {
  name: 'App',
  components: { GlobalHeader, MainNav, GlobalFooter }
};
</script>

<style>
*, *::before, *::after { box-sizing: border-box; }

body {
  margin: 0;
  padding: 0;
  background-color: #0a0a0a;
  color: #fff;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  font-size: 16px;
  -webkit-font-smoothing: antialiased;
}

#death-metal-mania {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #0a0a0a;
}

.page-content {
  flex: 1;
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 1.5rem 1.25rem 3rem;
  box-sizing: border-box;
}

@media (max-width: 768px) {
  .page-content {
    padding: 1rem 0.75rem 2rem;
  }
}
</style>
