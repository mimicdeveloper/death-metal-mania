<template>
  <main>
    <section id="favoriteBands">
      <h2>Your Favorite Bands</h2>
      <div v-if="favoriteBands.length === 0" class="empty-state">No favorite bands added yet.</div>
      <ul v-else class="favorites-list">
        <li v-for="(band, index) in favoriteBands" :key="index" class="favorite-item">
          <span class="fav-name">{{ band.bandName }}</span>
          <span class="fav-rating">
            <span v-for="n in 5" :key="n" class="star" :class="{ filled: n <= band.rating }">★</span>
          </span>
        </li>
      </ul>
    </section>

    <hr class="section-divider" />

    <section id="addFavoriteSection">
      <h2>Add to Favorites</h2>
      <form @submit.prevent="addFavorite">
        <div class="input-group">
          <label for="bandId">Band ID</label>
          <input type="number" v-model="bandId" id="bandId" name="bandId" required placeholder="Enter band ID" />
        </div>
        <div class="input-group">
          <label for="bandName">Band Name</label>
          <input type="text" v-model="bandName" id="bandName" name="bandName" required placeholder="Enter band name" />
        </div>
        <div class="input-group">
          <label for="rating">Rating (1–5)</label>
          <input type="number" v-model="rating" id="rating" name="rating" min="1" max="5" required />
        </div>
        <button type="submit">Add Favorite</button>
      </form>
    </section>

    <hr class="section-divider" />

    <section id="removeFavoriteSection">
      <h2>Remove from Favorites</h2>
      <form @submit.prevent="removeFavorite">
        <div class="input-group">
          <label for="removeBandName">Band Name</label>
          <input type="text" v-model="removeBandName" id="removeBandName" name="removeBandName" required placeholder="Enter band name to remove" />
        </div>
        <button type="submit" class="btn-danger">Remove Favorite</button>
      </form>
    </section>
  </main>
</template>

<script>
import api from '@/api';
import { useToast } from 'vue-toastification';

export default {
  name: 'FavoritesView',
  setup() {
    const toast = useToast();
    return { toast };
  },
  data() {
    return {
      favoriteBands: [],
      bandId: '',
      bandName: '',
      rating: 1,
      removeBandName: '',
    };
  },
  async created() {
    await this.loadFavoriteBands();
  },
  methods: {
    async loadFavoriteBands() {
      const token = localStorage.getItem('token');
      if (!token) {
        this.toast.warning('Please log in to view your favorite bands.');
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
        this.toast.warning('Please log in to add a favorite.');
        return;
      }
      try {
        await api.post(
          '/favorites',
          { bandId: this.bandId, bandName: this.bandName, rating: this.rating },
          { headers: { Authorization: `Bearer ${token}` } }
        );
        this.toast.success(`"${this.bandName}" added to favorites!`);
        this.bandId = '';
        this.bandName = '';
        this.rating = 1;
        await this.loadFavoriteBands();
      } catch (error) {
        this.toast.error(`Failed to add favorite: ${error.response ? error.response.data.message : error.message}`);
      }
    },

    async removeFavorite() {
      const token = localStorage.getItem('token');
      if (!token) {
        this.toast.warning('Please log in to remove a favorite.');
        return;
      }
      try {
        await api.delete(`/favorites/${encodeURIComponent(this.removeBandName)}`, {
          headers: { Authorization: `Bearer ${token}` }
        });
        this.toast.success(`"${this.removeBandName}" removed from favorites.`);
        this.removeBandName = '';
        await this.loadFavoriteBands();
      } catch (error) {
        this.toast.error(`Failed to remove favorite: ${error.response ? error.response.data.message : error.message}`);
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
  background-color: #111;
  border: 1px solid #2a2a2a;
  border-radius: 12px;
  max-width: 600px;
  margin: 2rem auto;
  color: #fff;
}

h2 {
  font-size: 1.5rem;
  margin-bottom: 1.5rem;
  text-align: center;
  color: #fff;
  font-weight: 900;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.empty-state {
  text-align: center;
  color: #666;
  padding: 1rem 0;
}

.favorites-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 0.6rem;
}

.favorite-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #1a1a1a;
  border: 1px solid #2a2a2a;
  border-radius: 8px;
  padding: 0.75rem 1rem;
}

.fav-name {
  font-weight: 600;
  font-size: 1rem;
}

.star {
  font-size: 1.1rem;
  color: #444;
}

.star.filled {
  color: crimson;
}

form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 0.35rem;
}

label {
  font-weight: 600;
  font-size: 0.85rem;
  color: #aaa;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

input {
  padding: 0.7rem 1rem;
  font-size: 1rem;
  background: #1c1c1c;
  color: #fff;
  border: 1px solid #333;
  border-radius: 6px;
  transition: border-color 0.2s;
}

input:focus {
  outline: none;
  border-color: crimson;
}

input::placeholder {
  color: #555;
}

button {
  background-color: crimson;
  color: white;
  padding: 0.75rem 1.5rem;
  font-size: 1rem;
  font-weight: 700;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  align-self: center;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  transition: background-color 0.2s, transform 0.1s;
}

button:hover {
  background-color: #a30000;
}

button:active {
  transform: scale(0.97);
}

.btn-danger {
  background-color: #333;
  border: 1px solid #550000;
}

.btn-danger:hover {
  background-color: #a30000;
  border-color: crimson;
}

.section-divider {
  border: none;
  border-top: 1px solid #2a2a2a;
  margin: 0;
}
</style>
