<template>
    <section id="favorites-section">
      <h2>Your Favorite Events</h2>
  
      <loading-spinner :spin="isLoading" id="spinner" />
  
      <div v-if="favorites.length === 0 && !isLoading">No favorite events found.</div>
  
      <div v-for="(event, index) in favorites" :key="index" class="band">
        <h3>{{ event.name }}</h3>
        <p><strong>Date:</strong> {{ formatDate(event.localDate) }}</p>
        <p><strong>Time:</strong> {{ formatTime(event.localTime) }}</p>
        <p><strong>City:</strong> {{ event.city || 'N/A' }}</p>
        <p><strong>State:</strong> {{ event.state || 'N/A' }}</p>
        <p><strong>Venue:</strong> {{ event.venue || 'N/A' }}</p>
        <p><strong>URL:</strong> 
          <a :href="event.url" target="_blank" v-if="event.url">Tickets</a>
          <span v-else>N/A</span>
        </p>
        <p><strong>Info:</strong> {{ event.info || 'N/A' }}</p>
  
        <button @click="deleteFavoriteEvent(event.eventId)">Remove from Favorites</button>
      </div>
    </section>
  </template>
  
  <script>
  import LoadingSpinner from '@/components/LoadingSpinner.vue';
  import { mapState } from 'vuex';
  
  export default {
    name: 'EventsView',
    components: {
      LoadingSpinner,
    },
    data() {
      return {
        favorites: [],
        isLoading: false,
      };
    },
    computed: {
      ...mapState(['user', 'token']),
    },
    methods: {
      formatTime(time24) {
        if (!time24) return 'N/A';
        const [hourStr, minuteStr] = time24.split(':');
        let hour = parseInt(hourStr, 10);
        const ampm = hour >= 12 ? 'PM' : 'AM';
        hour = hour % 12 || 12;
        return `${hour}:${minuteStr} ${ampm}`;
      },
      formatDate(dateStr) {
        if (!dateStr) return 'N/A';
        const date = new Date(dateStr);
        return date.toLocaleDateString('en-US', {
          year: 'numeric',
          month: 'short',
          day: 'numeric',
        });
      },
      async fetchFavorites() {
        this.isLoading = true;
        try {
          const response = await fetch('http://localhost:9000/favorites/events', {
            headers: {
              Authorization: `Bearer ${this.token}`,
            },
          });
  
          if (!response.ok) {
            throw new Error('Failed to fetch favorite events');
          }
  
          const data = await response.json();
          console.log('Favorites from backend:', data); // Helpful debug line
          this.favorites = data;
        } catch (error) {
          console.error('Error fetching favorites:', error);
          this.favorites = [];
        } finally {
          this.isLoading = false;
        }
      },
      async deleteFavoriteEvent(eventId) {
        if (!confirm('Are you sure you want to remove this event from your favorites?')) return;
  
        try {
          const response = await fetch(`http://localhost:9000/favorites/events/${eventId}`, {
            method: 'DELETE',
            headers: {
              Authorization: `Bearer ${this.token}`,
            },
          });
  
          if (!response.ok) {
            const errorText = await response.text();
            console.error('Delete error:', errorText);
            alert('Failed to remove event from favorites.');
            return;
          }
  
          // Update the UI after successful deletion
          this.favorites = this.favorites.filter(event => event.eventId !== eventId);
          alert('Event removed from favorites!');
        } catch (error) {
          console.error('Network error during deletion:', error);
          alert('Network error while removing event from favorites.');
        }
      },
    },
    created() {
      this.fetchFavorites();
    },
  };
  </script>
  
  <style scoped>
  #favorites-section {
    padding: 1rem;
    max-width: 700px;
    margin: auto;
  }
  
  .band {
    background-color: #222;
    color: white;
    padding: 1rem;
    margin-bottom: 1rem;
    border-radius: 6px;
  }
  
  h2 {
    text-align: center;
    margin-bottom: 1.5rem;
  }
  
  a {
    color: crimson;
  }
  
  a:hover {
    text-decoration: underline;
  }
  
  button {
    margin-top: 0.75rem;
    padding: 0.5rem 1.2rem;
    background-color: #c0392b;
    color: white;
    font-weight: bold;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  
  button:hover {
    background-color: #922b21;
  }
  </style>
  