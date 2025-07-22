<template>
  <section id="search-section">
    <h2>Search Death Metal Bands and Albums</h2>
    <div class="search-controls">
      <button @click="searchByGenre">Show Death Metal Bands</button>

      <div class="search-input-group">
        <input type="text" v-model="bandName" placeholder="Search by Band Name..." />
        <button @click="searchBand">Search Band</button>
      </div>

      <button @click="clearResults">Clear</button>
    </div>

    <div class="search-input-group" style="margin-bottom: 1rem;">
      <input type="text" v-model="spotifyId" placeholder="Enter Spotify ID to Get Albums..." />
      <button @click="searchAlbums">Search Albums</button>
    </div>

    <!-- Loading Spinner -->
    <loading-spinner :spin="isLoading" id="spinner" />

    <div id="search-results">
      <div v-if="results.length === 0 && searched && !isLoading">No results found.</div>
      <div v-for="(item, index) in results" :key="index" class="band">
        <h3>{{ item.name }}</h3>
        <p><strong>ID:</strong> {{ item.id }}</p>

        <!-- Album display -->
        <div v-if="item.images && item.images.length > 0">
          <img :src="item.images[0].url" :alt="item.name + ' album cover'" class="album-cover" />
          <p v-if="item.release_date"><strong>Release Date:</strong> {{ item.release_date }}</p>
          <iframe
            v-if="item.id"
            :src="`https://open.spotify.com/embed/album/${item.id}`"
            width="300"
            height="250"
            allowtransparency="true"
            allow="encrypted-media"
            class="spotify-player spotify-album"
          ></iframe>
        </div>

        <!-- Band display -->
        <div v-else-if="item.popularity !== undefined">
          <p><strong>Popularity:</strong> {{ item.popularity }}</p>
          <iframe
            v-if="item.id"
            :src="`https://open.spotify.com/embed/artist/${item.id}`"
            width="300"
            height="80"
            frameborder="0"
            allowtransparency="true"
            allow="encrypted-media"
            class="spotify-player spotify-artist"
          ></iframe>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import axios from 'axios';
import LoadingSpinner from '@/components/LoadingSpinner.vue';

export default {
  name: 'SearchBands',
  components: {
    LoadingSpinner,
  },
  data() {
    return {
      bandName: '',
      spotifyId: '',
      results: [],
      searched: false,
      isLoading: false,
    };
  },
  methods: {
    async searchByGenre() {
      this.isLoading = true;
      try {
        const response = await axios.get('http://localhost:9000/bands/searchByDeathMetalGenre');
        this.results = response.data.artists?.items || [];
      } catch (error) {
        console.error('Error fetching death metal bands:', error);
        this.results = [];
      } finally {
        this.searched = true;
        this.isLoading = false;
      }
    },
    async searchBand() {
      const bandName = this.bandName.trim();
      if (!bandName) return;

      this.isLoading = true;
      try {
        const response = await axios.get(
          `http://localhost:9000/bands/searchByBandName?bandName=${encodeURIComponent(bandName)}`
        );
        this.results = response.data.artists?.items || [];
      } catch (error) {
        console.error('Error searching for band:', error);
        this.results = [];
      } finally {
        this.searched = true;
        this.isLoading = false;
      }
    },
    async searchAlbums() {
      const spotifyId = this.spotifyId.trim();
      if (!spotifyId) return;

      this.isLoading = true;
      try {
        const response = await axios.get(
          `http://localhost:9000/bands/${encodeURIComponent(spotifyId)}/albums`
        );
        this.results = response.data.items || [];
      } catch (error) {
        console.error('Error fetching albums:', error);
        this.results = [];
      } finally {
        this.searched = true;
        this.isLoading = false;
      }
    },
    clearResults() {
      this.bandName = '';
      this.spotifyId = '';
      this.results = [];
      this.searched = false;
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

.search-controls {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 1.5rem;
  margin-bottom: 1rem;
}

.search-input-group {
  display: flex;
  align-items: center;
  gap: 1rem;
  flex: 1;
  justify-content: center;
}

.band {
  display: flex;
  justify-content: space-between;
  flex-direction: column;
  align-items: center;
  background-color: #222;
  padding: 1rem;
  margin-bottom: 0.5rem;
  border-radius: 6px;
}

.band p {
  text-align: center;
  width: 100%;
}

.band > div {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

button {
  padding: 0.75rem 1.5rem;
  margin-left: 1rem;
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

.album-cover {
  width: 200px;
  height: auto;
  margin-top: 1rem;
  border-radius: 4px;
  box-shadow: 0 0 8px rgba(255, 255, 255, 0.2);
}

.spotify-player {
  margin-top: 1rem;
  border-radius: 8px;
  display: block;
  border: none !important;
  box-shadow: none;
  outline: none;
  background-color: transparent;
}

.spotify-album {
  height: 250px; 
}

.spotify-artist {
  height: 80px; 
}

.load-spinner {
  transition-property: opacity;
  transition-duration: 400ms;
  font-size: 3rem;
  display: block;
  margin: 2rem auto;
  color: crimson;
}


/* Mobile Styles */
@media only screen and (max-width: 600px) {
  .search-controls {
    flex-direction: column;
    align-items: center;
    gap: 1rem;
    width: 100%;
    justify-content: center;
  }

  .search-input-group {
    width: 100%;
    justify-content: center;
  }

  button {
    width: 90%;
    max-width: 150px;
    margin: 0 auto;
  }

  input[type='text'] {
    width: 90%;
  }
}
</style>
