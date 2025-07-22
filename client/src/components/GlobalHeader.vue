<template>
    <header>
      <h1>
        <img src="/img/deathmetalmanialogo.png" alt="Death Metal Mania Logo" class="logo" />
      </h1>
    </header>
  </template>
  
  <script>
  export default {
    name: 'GlobalHeader'
  };
  </script>
  
  <style scoped>
  header {
    background-color: #000000;
    text-align: center;
    padding: 1rem 0;
  }
  
  .logo {
    max-width: 500px;
    width: 100%;
    height: auto;
    display: block;
    margin: 0 auto;
  }
  </style>
  