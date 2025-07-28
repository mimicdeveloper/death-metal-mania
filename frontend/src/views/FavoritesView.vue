<template>
  <main>
    <!-- Favorite Bands Section -->
    <section id="favoriteBands">
      <h2>Your Favorite Bands</h2>
      <div v-if="favoriteBands.length === 0">No favorite bands added yet.</div>
      <ul v-else>
        <li v-for="(band, index) in favoriteBands" :key="index">
          {{ band.bandName }} - Rating: {{ band.rating }}
        </li>
      </ul>
    </section>

    <hr class="section-divider" />

    <!-- Add to Favorites Section -->
    <section id="addFavoriteSection">
      <h2>Add to Favorites</h2>
      <form @submit.prevent="addFavorite">
        <label for="bandId">Band ID:</label>
        <input type="number" v-model="bandId" id="bandId" name="bandId" required />

        <label for="bandName">Band Name:</label>
        <input type="text" v-model="bandName" id="bandName" name="bandName" required />

        <label for="rating">Rating:</label>
        <input type="number" v-model="rating" id="rating" name="rating" min="1" max="5" required />

        <button type="submit">Add Favorite</button>
      </form>
      <div v-if="addFavoriteMessage" id="addFavoriteMessage">{{ addFavoriteMessage }}</div>
    </section>

    <hr class="section-divider" />

    <!-- Remove from Favorites Section -->
    <section id="removeFavoriteSection">
      <h2>Remove from Favorites</h2>
      <form @submit.prevent="removeFavorite">
        <label for="removeBandName">Band Name:</label>
        <input type="text" v-model="removeBandName" id="removeBandName" name="removeBandName" required />
        <button type="submit">Remove Favorite</button>
      </form>
      <div v-if="removeFavoriteMessage" id="removeFavoriteMessage">{{ removeFavoriteMessage }}</div>
    </section>
  </main>
</template>

<script>
import api from '@/api';

export default {
  name: 'FavoritesView',
  data() {
    return {
      favoriteBands: [],
      bandId: '',
      bandName: '',
      rating: 1,
      removeBandName: '',
      addFavoriteMessage: '',
      removeFavoriteMessage: '',
    };
  },

  async created() {
    await this.loadFavoriteBands();
  },

  methods: {
    async loadFavoriteBands() {
      const token = localStorage.getItem('token');
      if (!token) {
        this.addFavoriteMessage = 'Please log in to view your favorite bands.';
        return;
      }
      try {
        const response = await api.get('/favorites', {
          headers: { Authorization: `Bearer ${token}` }
        });
        this.favoriteBands = response.data;
      } catch (error) {
        console.error('Failed to load favorite bands:', error);
      }
    },

    async addFavorite() {
      const token = localStorage.getItem('token');
      if (!token) {
        this.addFavoriteMessage = 'Please log in to add a favorite.';
        return;
      }

      try {
        await api.post(
          '/favorites',
          {
            bandId: this.bandId,
            bandName: this.bandName,
            rating: this.rating,
          },
          {
            headers: { Authorization: `Bearer ${token}` }
          }
        );
        this.addFavoriteMessage = 'Favorite added!';
        this.bandId = '';
        this.bandName = '';
        this.rating = 1;
        await this.loadFavoriteBands();
      } catch (error) {
        this.addFavoriteMessage = `Failed to add favorite: ${
          error.response ? error.response.data.message : error.message
        }`;
      }
    },

    async removeFavorite() {
      const token = localStorage.getItem('token');
      if (!token) {
        this.removeFavoriteMessage = 'Please log in to remove a favorite.';
        return;
      }

      try {
        await api.delete(`/favorites/${encodeURIComponent(this.removeBandName)}`, {
          headers: { Authorization: `Bearer ${token}` }
        });
        this.removeFavoriteMessage = 'Favorite removed!';
        this.removeBandName = '';
        await this.loadFavoriteBands();
      } catch (error) {
        this.removeFavoriteMessage = `Failed to remove favorite: ${
          error.response ? error.response.data.message : error.message
        }`;
      }
    },
  },
};
</script>

<style scoped>
#favoriteBands,
#addFavoriteSection,
#removeFavoriteSection {
  padding: 2rem;
  background-color: #2b2b2b;
  border-radius: 8px;
  max-width: 600px;
  margin: 2rem auto;
  color: #fff;
}

h2 {
  font-size: 2rem;
  margin-bottom: 1.5rem;
  text-align: center;
  color: #fff;
  font-weight: bold;
}

form {
  background-color: #333;
  padding: 2rem;
  border-radius: 8px;
  width: 100%;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.5);
}

form label {
  display: block;
  margin: 0.5rem 0 0.25rem;
}

form input {
  width: 100%;
  padding: 0.5rem;
  margin-bottom: 1rem;
  background: #1a1a1a;
  color: #fff;
  border: 1px solid #444;
  border-radius: 4px;
}

form button {
  background-color: crimson;
  color: white;
  padding: 0.75rem 1.5rem;
  font-size: 1rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  display: block;
  margin: 1rem auto 0;
}

form button:hover {
  background-color: #a30000;
}

#addFavoriteMessage,
#removeFavoriteMessage {
  margin-top: 1rem;
  font-weight: bold;
  color: #fff;
}

.section-divider {
  border: 1px solid #444;
  margin: 2rem 0;
}
</style>
