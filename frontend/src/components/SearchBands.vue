<template>
  <section id="search-section">
    <h2 class="section-title">Death Metal Bands</h2>

    <div class="search-controls">
      <button class="btn-primary" @click="searchByGenre" :disabled="isLoading">
        <span v-if="isLoading">Loading...</span>
        <span v-else>Show Death Metal Bands</span>
      </button>

      <div class="search-input-group">
        <input type="text" v-model="bandName" placeholder="Search by band name..." @keyup.enter="searchBand" />
        <button class="btn-primary" @click="searchBand" :disabled="isLoading">Search</button>
      </div>

      <div class="search-input-group">
        <input type="text" v-model="spotifyId" placeholder="Spotify ID for albums..." @keyup.enter="searchAlbums" />
        <button class="btn-secondary" @click="searchAlbums" :disabled="isLoading">Albums</button>
      </div>

      <button class="btn-ghost" @click="clearResults" v-if="results.length > 0 || genreResults.length > 0">Clear</button>
    </div>

    <!-- Genre filter bar — only shown when browsing by genre -->
    <div class="genre-filter-bar" v-if="allGenres.length > 0">
      <span class="filter-label">Filter:</span>
      <div class="genre-chips">
        <button
          class="genre-chip"
          :class="{ active: activeGenres.length === 0 }"
          @click="activeGenres = []"
        >All</button>
        <button
          v-for="genre in allGenres"
          :key="genre"
          class="genre-chip"
          :class="{ active: activeGenres.includes(genre) }"
          @click="toggleGenre(genre)"
        >{{ genre }}</button>
      </div>
    </div>

    <loading-spinner :spin="isLoading" id="spinner" />

    <!-- Band grid results -->
    <div v-if="displayedBands.length > 0" class="band-grid">
      <div
        v-for="(band, index) in displayedBands"
        :key="index"
        class="band-card"
        @click="toggleExpand(index)"
        :class="{ expanded: expandedIndex === index }"
      >
        <div class="band-card-inner">
          <div class="band-image-wrap">
            <img
              v-if="band.images && band.images.length > 0"
              :src="band.images[0].url"
              :alt="band.name"
              class="band-image"
            />
            <div v-else class="band-image-placeholder">
              <span>🤘</span>
            </div>
          </div>
          <div class="band-info">
            <h3 class="band-name">{{ band.name }}</h3>
            <div class="genre-tags" v-if="band.genres && band.genres.length > 0">
              <span v-for="g in band.genres.slice(0, 3)" :key="g" class="genre-tag">{{ g }}</span>
            </div>
            <div class="popularity-bar" v-if="band.popularity !== undefined">
              <span class="pop-label">Popularity</span>
              <div class="pop-track">
                <div class="pop-fill" :style="{ width: band.popularity + '%' }"></div>
              </div>
              <span class="pop-value">{{ band.popularity }}</span>
            </div>
          </div>
        </div>

        <!-- Expanded Spotify player -->
        <div v-if="expandedIndex === index" class="spotify-expand" @click.stop>
          <iframe
            :src="`https://open.spotify.com/embed/artist/${band.id}`"
            width="100%"
            height="152"
            frameborder="0"
            allowtransparency="true"
            allow="encrypted-media"
            class="spotify-player"
          ></iframe>
        </div>
      </div>
    </div>

    <!-- Album results (list layout) -->
    <div v-if="albumResults.length > 0" class="album-list">
      <div v-for="(album, index) in albumResults" :key="index" class="album-card">
        <img
          v-if="album.images && album.images.length > 0"
          :src="album.images[0].url"
          :alt="album.name"
          class="album-cover"
        />
        <div class="album-info">
          <h3>{{ album.name }}</h3>
          <p v-if="album.release_date">{{ album.release_date }}</p>
          <iframe
            :src="`https://open.spotify.com/embed/album/${album.id}`"
            width="100%"
            height="152"
            frameborder="0"
            allowtransparency="true"
            allow="encrypted-media"
            class="spotify-player"
          ></iframe>
        </div>
      </div>
    </div>

    <div v-if="searched && !isLoading && displayedBands.length === 0 && albumResults.length === 0" class="no-results">
      No results found.
    </div>
  </section>
</template>

<script>
import api from '@/api';
import LoadingSpinner from '@/components/LoadingSpinner.vue';
import { useToast } from 'vue-toastification';

export default {
  name: 'SearchBands',
  components: { LoadingSpinner },
  setup() {
    const toast = useToast();
    return { toast };
  },
  data() {
    return {
      bandName: '',
      spotifyId: '',
      genreResults: [],   // from Show Bands button
      albumResults: [],   // from album search
      searched: false,
      isLoading: false,
      allGenres: [],
      activeGenres: [],
      expandedIndex: null,
    };
  },
  computed: {
    results() {
      return this.genreResults;
    },
    displayedBands() {
      if (this.activeGenres.length === 0) return this.genreResults;
      return this.genreResults.filter(band =>
        band.genres && band.genres.some(g => this.activeGenres.includes(g))
      );
    },
  },
  methods: {
    async searchByGenre() {
      this.isLoading = true;
      this.albumResults = [];
      this.expandedIndex = null;
      try {
        const response = await api.get('/bands/searchByDeathMetalGenre');
        this.genreResults = response.data.artists?.items || [];
        this.buildGenreList();
        if (this.genreResults.length > 0) {
          this.toast.success(`Loaded ${this.genreResults.length} bands`);
        } else {
          this.toast.warning('No bands found.');
        }
      } catch (error) {
        this.genreResults = [];
        this.toast.error('Failed to fetch bands. Try again.');
      } finally {
        this.searched = true;
        this.isLoading = false;
      }
    },
    async searchBand() {
      const name = this.bandName.trim();
      if (!name) return;
      this.isLoading = true;
      this.albumResults = [];
      this.allGenres = [];
      this.activeGenres = [];
      this.expandedIndex = null;
      try {
        const response = await api.get(`/bands/searchByBandName?bandName=${encodeURIComponent(name)}`);
        this.genreResults = response.data.artists?.items || [];
        if (this.genreResults.length > 0) {
          this.toast.success(`Found ${this.genreResults.length} result(s) for "${name}"`);
        } else {
          this.toast.warning(`No bands found for "${name}"`);
        }
      } catch (error) {
        this.genreResults = [];
        this.toast.error('Search failed. Try again.');
      } finally {
        this.searched = true;
        this.isLoading = false;
      }
    },
    async searchAlbums() {
      const id = this.spotifyId.trim();
      if (!id) return;
      this.isLoading = true;
      this.genreResults = [];
      this.allGenres = [];
      this.activeGenres = [];
      this.expandedIndex = null;
      try {
        const response = await api.get(`/bands/${encodeURIComponent(id)}/albums`);
        this.albumResults = response.data.items || [];
        if (this.albumResults.length > 0) {
          this.toast.success(`Loaded ${this.albumResults.length} album(s)`);
        } else {
          this.toast.warning('No albums found for that ID.');
        }
      } catch (error) {
        this.albumResults = [];
        this.toast.error('Failed to fetch albums. Check the Spotify ID.');
      } finally {
        this.searched = true;
        this.isLoading = false;
      }
    },
    buildGenreList() {
      const set = new Set();
      this.genreResults.forEach(band => {
        if (band.genres) band.genres.forEach(g => set.add(g));
      });
      this.allGenres = [...set].sort();
      this.activeGenres = [];
    },
    toggleGenre(genre) {
      const idx = this.activeGenres.indexOf(genre);
      if (idx === -1) {
        this.activeGenres.push(genre);
      } else {
        this.activeGenres.splice(idx, 1);
      }
    },
    toggleExpand(index) {
      this.expandedIndex = this.expandedIndex === index ? null : index;
    },
    clearResults() {
      this.bandName = '';
      this.spotifyId = '';
      this.genreResults = [];
      this.albumResults = [];
      this.allGenres = [];
      this.activeGenres = [];
      this.searched = false;
      this.expandedIndex = null;
    },
  },
};
</script>

<style scoped>
#search-section {
  padding-bottom: 2rem;
}

.section-title {
  text-align: center;
  font-size: 2rem;
  font-weight: 900;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: #fff;
  margin-bottom: 2rem;
}

/* Controls */
.search-controls {
  display: flex;
  flex-wrap: wrap;
  gap: 0.75rem;
  align-items: center;
  justify-content: center;
  margin-bottom: 1.5rem;
}

.search-input-group {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

input[type='text'] {
  padding: 0.65rem 1rem;
  font-size: 1rem;
  border: 1px solid #444;
  border-radius: 6px;
  background-color: #1c1c1c;
  color: #fff;
  min-width: 220px;
  transition: border-color 0.2s;
}

input[type='text']:focus {
  outline: none;
  border-color: crimson;
}

input::placeholder {
  color: #888;
}

.btn-primary {
  padding: 0.65rem 1.4rem;
  background-color: crimson;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 1rem;
  font-weight: 700;
  cursor: pointer;
  transition: background-color 0.2s, transform 0.1s;
  letter-spacing: 0.03em;
}

.btn-primary:hover:not(:disabled) {
  background-color: #a30000;
}

.btn-primary:active:not(:disabled) {
  transform: scale(0.97);
}

.btn-primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-secondary {
  padding: 0.65rem 1.4rem;
  background-color: #333;
  color: white;
  border: 1px solid #555;
  border-radius: 6px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: background-color 0.2s;
}

.btn-secondary:hover:not(:disabled) {
  background-color: #444;
}

.btn-ghost {
  padding: 0.65rem 1.2rem;
  background: transparent;
  color: #aaa;
  border: 1px solid #444;
  border-radius: 6px;
  font-size: 0.95rem;
  cursor: pointer;
  transition: color 0.2s, border-color 0.2s;
}

.btn-ghost:hover {
  color: #fff;
  border-color: #888;
}

/* Genre filter bar */
.genre-filter-bar {
  display: flex;
  align-items: flex-start;
  gap: 0.75rem;
  margin-bottom: 1.5rem;
  flex-wrap: wrap;
}

.filter-label {
  color: #aaa;
  font-size: 0.9rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  padding-top: 0.35rem;
  white-space: nowrap;
}

.genre-chips {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.genre-chip {
  padding: 0.3rem 0.85rem;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 600;
  cursor: pointer;
  border: 1px solid #444;
  background: #1a1a1a;
  color: #ccc;
  transition: all 0.15s;
  text-transform: capitalize;
}

.genre-chip:hover {
  border-color: crimson;
  color: #fff;
}

.genre-chip.active {
  background-color: crimson;
  border-color: crimson;
  color: #fff;
}

/* Band grid */
.band-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1.25rem;
}

.band-card {
  background: #111;
  border: none;
  border-radius: 10px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.15s, box-shadow 0.2s;
}

.band-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 20px rgba(220, 20, 60, 0.15);
}

.band-card.expanded {
  box-shadow: 0 0 20px rgba(220, 20, 60, 0.25);
}

.band-card-inner {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
}

.band-image-wrap {
  flex-shrink: 0;
}

.band-image {
  width: 70px;
  height: 70px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #333;
}

.band-image-placeholder {
  width: 70px;
  height: 70px;
  border-radius: 50%;
  background: #1e1e1e;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.8rem;
  border: 2px solid #333;
}

.band-info {
  flex: 1;
  min-width: 0;
}

.band-name {
  font-size: 1rem;
  font-weight: 700;
  color: #fff;
  margin: 0 0 0.4rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.genre-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.3rem;
  margin-bottom: 0.5rem;
}

.genre-tag {
  background: #1e1e1e;
  border: 1px solid #3a3a3a;
  color: #bbb;
  font-size: 0.7rem;
  padding: 0.15rem 0.5rem;
  border-radius: 10px;
  text-transform: capitalize;
}

.popularity-bar {
  display: flex;
  align-items: center;
  gap: 0.4rem;
}

.pop-label {
  font-size: 0.7rem;
  color: #666;
  white-space: nowrap;
}

.pop-track {
  flex: 1;
  height: 4px;
  background: #2a2a2a;
  border-radius: 2px;
  overflow: hidden;
}

.pop-fill {
  height: 100%;
  background: crimson;
  border-radius: 2px;
  transition: width 0.4s ease;
}

.pop-value {
  font-size: 0.7rem;
  color: #888;
  min-width: 22px;
}

/* Spotify expand area */
.spotify-expand {
  padding: 0 1rem 1rem;
  border-top: 1px solid #222;
}

.spotify-player {
  border-radius: 8px;
  display: block;
  border: none;
  margin-top: 0.75rem;
}

/* Album list */
.album-list {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.album-card {
  display: flex;
  gap: 1.5rem;
  background: #111;
  border: none;
  border-radius: 10px;
  padding: 1.25rem;
  align-items: flex-start;
}

.album-cover {
  width: 120px;
  height: 120px;
  border-radius: 6px;
  object-fit: cover;
  flex-shrink: 0;
}

.album-info {
  flex: 1;
}

.album-info h3 {
  font-size: 1.1rem;
  font-weight: 700;
  color: #fff;
  margin: 0 0 0.4rem;
}

.album-info p {
  color: #888;
  font-size: 0.9rem;
  margin: 0 0 0.75rem;
}

/* No results */
.no-results {
  text-align: center;
  color: #666;
  padding: 3rem 0;
  font-size: 1.1rem;
}

/* Mobile */
@media (max-width: 600px) {
  .search-controls {
    flex-direction: column;
  }

  .search-input-group {
    width: 100%;
  }

  input[type='text'] {
    width: 100%;
    min-width: unset;
    flex: 1;
  }

  .band-grid {
    grid-template-columns: 1fr;
  }

  .album-card {
    flex-direction: column;
  }

  .album-cover {
    width: 100%;
    height: auto;
  }
}
</style>
