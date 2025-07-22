<template>
  <nav>
    <button v-on:click="navigateToHome">Home</button>
    <button v-on:click="navigateToRegister">Register</button>
    <button v-on:click="navigateToLogin">Login</button>
    <button v-on:click="navigateToUser">User</button>
    <button v-on:click="navigateToAdmin">Admin</button>
    <button v-on:click="navigateToContact">Contact</button>
    <button v-on:click="logout">Logout</button>
  </nav>
</template>

  
  <script>
  import { useRouter } from 'vue-router';
  
  export default {
    name: 'MainNav',
    setup() {
      const router = useRouter();
  
      const navigateToHome = () => {
        router.push('/');
      };
  
      const navigateToRegister = () => {
        router.push('/register');
      };
  
      const navigateToLogin = () => {
        router.push('/login');
      };
  
      const navigateToUser = () => {
        router.push('/user');
      };
  
      const navigateToAdmin = () => {
        router.push('/admin');
      };
  
      const navigateToContact = () => {
        router.push('/contact');
      };
  
      const logout = () => {
        router.push('/logout');
      };

      return {
        navigateToHome,
        navigateToRegister,
        navigateToLogin,
        navigateToUser,
        navigateToAdmin,
        navigateToContact,
        logout
      };
    }
  };
  </script>
  
  <style scoped>
  nav {
    background-color: #000000;
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    align-items: center;
    padding: 1rem;
    gap: 1rem;
  }
  
  nav button {
  background-color: #333;
  border: none;
  padding: 1rem 2rem;           
  color: white;
  cursor: pointer;
  font-size: 1.1rem;
  font-weight: 600;            
  border-radius: 8px;           
  transition: background-color 0.3s ease;
  min-width: 120px;             
}

  
  nav button:hover {
    background-color: crimson;
  }
  </style>
  