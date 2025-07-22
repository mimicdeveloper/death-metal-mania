<template>
    <main>
      <section id="contact-section">
        <h2>Contact Us</h2>
        <form id="contact-form" @submit.prevent="submitForm">
          <div class="input-group">
            <label for="name">Name:</label>
            <input
              type="text"
              id="name"
              name="name"
              v-model="form.name"
              placeholder="Your name..."
              required
            />
          </div>
  
          <div class="input-group">
            <label for="email">Email:</label>
            <input
              type="email"
              id="email"
              name="email"
              v-model="form.email"
              placeholder="you@example.com"
              required
            />
          </div>
  
          <div class="input-group">
            <label for="message">Message:</label>
            <textarea
              id="message"
              name="message"
              v-model="form.message"
              rows="5"
              placeholder="Write your message here..."
              required
            ></textarea>
          </div>
  
          <button type="submit">Send Message</button>
        </form>
      </section>
    </main>
  </template>
  
  <script>
  export default {
    data() {
      return {
        form: {
          name: "",
          email: "",
          message: "",
        },
      };
    },
    methods: {
      submitForm() {
        
        console.log("Contact form submitted:", this.form);
        alert("Your message has been sent!");
        
        this.form.name = "";
        this.form.email = "";
        this.form.message = "";
      },
    },
  };
  </script>
  
  <style scoped>
  #contact-section {
    max-width: 600px;
    margin: 3rem auto;
    background-color: #222;
    padding: 2rem;
    border-radius: 10px;
    color: white;
  }
  
  h2 {
    text-align: center;
    font-size: 1.8rem;
    margin-bottom: 2rem;
  }
  
  form {
    display: flex;
    flex-direction: column;
    gap: 1.5rem;
  }
  
  .input-group {
    display: flex;
    flex-direction: column;
  }
  
  label {
    margin-bottom: 0.5rem;
    font-weight: bold;
  }
  
  input,
  textarea {
    padding: 0.75rem 1rem;
    font-size: 1.1rem;
    border: 1px solid #444;
    border-radius: 6px;
    background-color: #727272;
    color: white;
  }
  
  input::placeholder,
  textarea::placeholder {
    color: rgb(220, 220, 220);
  }
  
  button {
    align-self: center;
    padding: 0.75rem 1.5rem;
    background-color: crimson;
    color: white;
    border: none;
    border-radius: 6px;
    font-size: 1.1rem;
    font-weight: 600;
    cursor: pointer;
  }
  
  button:hover {
    background-color: rgb(132, 12, 36);
  }
  </style>
  