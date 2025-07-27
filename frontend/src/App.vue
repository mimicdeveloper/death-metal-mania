<template>
  <div id="death-metal-mania">
    <GlobalHeader />
    <MainNav />
    <main>
      <router-view />
    </main>
    <GlobalFooter />
  </div>
</template>

<script>
import GlobalHeader from './components/GlobalHeader.vue';
import GlobalFooter from './components/GlobalFooter.vue';
import MainNav from './components/MainNav.vue';

export default {
  name: 'App',
  components: {
    GlobalHeader,
    MainNav,
    GlobalFooter
  }
};
</script>

<style>
#death-metal-mania {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #000;
  color: #fff;
  font-family: 'Arial', sans-serif;
}

main {
  flex: 1;
  padding: 2rem;
  box-sizing: border-box;
}

router-view {
  display: block;
  margin: 0 auto;
  max-width: 1200px;
  width: 100%;
}

/* Mobile styles */
@media only screen and (max-width: 600px) {
  body {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
  }

  .container {
    padding: 0.75rem;
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  header {
    width: 100%;
    text-align: center;
    overflow: hidden;
  }

  header img {
    display: block;
    margin: 0 auto;
    max-width: 90vw;
    height: auto;
  }

  nav {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    gap: 0.5rem;
    width: 100%;
    padding: 0.5rem;
    box-sizing: border-box;
  }

  nav button {
    width: 42%;
    max-width: 140px;
    padding: 10px;
    font-size: 0.95rem;
    text-align: center;
  }

  main {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 0 1rem;
    box-sizing: border-box;
  }

  #search-section,
  #event-search-section {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 1.5rem;
    box-sizing: border-box;
  }

  h2 {
    font-size: 1.2rem;
    text-align: center;
    margin: 1rem 0;
  }

  .search-controls,
  .search-input-group {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 0.5rem;
    margin-bottom: 1rem;
    width: 100%;
  }

  input[type="text"] {
    width: 90%;
    max-width: 400px;
    padding: 0.5rem;
    font-size: 1rem;
    box-sizing: border-box;
    text-align: center;
    margin: 0 auto;
  }

  button {
    width: 90%;
    max-width: 150px;
    padding: 10px;
    font-size: 0.95rem;
    text-align: center;
    margin: 0 auto;
  }

  #search-results,
  #event-results {
    width: 90%;
    font-size: 0.9rem;
    padding: 0.5rem;
  }

  .section-divider {
    width: 90%;
    margin: 1.5rem auto;
    border: none;
    border-top: 1px solid #999;
  }

  footer {
    text-align: center;
    padding: 1rem 0;
    font-size: 0.9rem;
    width: 100%;
  }
}
</style>
