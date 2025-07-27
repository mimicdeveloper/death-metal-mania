<template>
  <section id="search-section">
    <h2>Find Concerts</h2>
    <div class="search-input-group">
      <input type="text" v-model="eventQuery" placeholder="Search Events by Band Name..." />
      <button @click="searchEvents">Search Events</button>
      <button @click="clearResults">Clear</button>
    </div>

    <loading-spinner :spin="isLoading" id="spinner" />

    <div id="event-results">
      <div v-if="events.length === 0 && searched && !isLoading">No events found.</div>
      <div v-for="(event, index) in events" :key="index" class="band">
        <h3>{{ event.name }}</h3>
        <p><strong>Date:</strong> {{ formatDate(event.dates.start.localDate) }}</p>
        <p><strong>Time:</strong> {{ formatTime(event.dates.start.localTime) }}</p>
        <p><strong>City:</strong> {{ event.city }}</p>
        <p><strong>State:</strong> {{ event.state }}</p>
        <p><strong>Venue:</strong> {{ event.venue }}</p>
        <p><strong>URL:</strong> <a :href="event.url" target="_blank">Tickets</a></p>
        <p><strong>Info:</strong> {{ event.info || 'N/A' }}</p>

        <button v-if="userRole === 'ROLE_USER' || userRole === 'ROLE_ADMIN'" @click="handleEventAction(event)">
          Add to Favorites
        </button>
      </div>
    </div>
  </section>
</template>

<script>
import api from '@/api';
import LoadingSpinner from '@/components/LoadingSpinner.vue';
import { mapState } from 'vuex';

export default {
  name: 'SearchEvents',
  components: {
    LoadingSpinner,
  },
  data() {
    return {
      eventQuery: '',
      events: [],
      searched: false,
      isLoading: false,
    };
  },
  computed: {
    ...mapState(['user', 'token']),
    userRole() {
      return this.user?.role || null;
    },
  },
  methods: {
    async searchEvents() {
      const bandName = this.eventQuery.trim();
      if (!bandName) return;

      this.isLoading = true;
      try {
        const response = await api.get(`/events/search`, {
          params: { band_name: bandName },
        });
        this.events = response.data._embedded?.events || [];
      } catch (error) {
        console.error('Error fetching events:', error);
        this.events = [];
      } finally {
        this.searched = true;
        this.isLoading = false;
      }
    },
    clearResults() {
      this.eventQuery = '';
      this.events = [];
      this.searched = false;
    },
    formatTime(time24) {
      if (!time24) return 'N/A';
      const [hourStr, minuteStr] = time24.split(':');
      let hour = parseInt(hourStr);
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
    async handleEventAction(event) {
      if (!this.user || !this.token) {
        alert('Please log in to add favorites.');
        return;
      }

      const favoriteEvent = {
        eventId: event.id,
        name: event.name,
        localDate: event.dates?.start?.localDate || '',
        localTime: event.dates?.start?.localTime || '',
        city: event._embedded?.venues?.[0]?.city?.name || '',
        state: event._embedded?.venues?.[0]?.state?.name || '',
        venue: event._embedded?.venues?.[0]?.name || '',
        url: event.url,
        info: event.info || '',
      };

      try {
        const response = await api.post('/favorites/events', favoriteEvent, {
          headers: {
            Authorization: `Bearer ${this.token}`,
          },
        });

        if (response.status === 200 || response.status === 201) {
          alert(`Added "${event.name}" to your favorites!`);
        } else {
          alert('Failed to add event to favorites.');
        }
      } catch (error) {
        console.error('Error adding to favorites:', error);
        alert('Network error while adding event to favorites.');
      }
    },
  },
};
</script>


<style scoped>
#search-section h2 {
  text-align: center;
  font-size: 1.5rem;
  margin-bottom: 2rem;
}

.search-input-group {
  display: flex;
  align-items: center;
  gap: 1rem;
  justify-content: center;
  flex-wrap: wrap;
  margin-bottom: 1rem;
}

.band {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #222;
  padding: 1rem;
  margin-bottom: 0.5rem;
  border-radius: 6px;
}

button {
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

input[type='text'] {
  padding: 0.75rem 1rem;
  width: 375px;
  font-size: 1.1rem;
  border: 1px solid #444;
  border-radius: 6px;
  background-color: #727272;
  color: white;
}

input::placeholder {
  color: rgb(220, 220, 220);
}

.load-spinner {
  transition-property: opacity;
  transition-duration: 400ms;
  font-size: 3rem;
  display: block;
  margin: 2rem auto;
  color: crimson;
}
</style>
