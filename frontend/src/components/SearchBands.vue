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

      <button class="btn-ghost" @click="clearResults" v-if="genreResults.length > 0">Clear</button>
    </div>

    <!-- Genre filter bar -->
    <div class="genre-filter-bar" v-if="allGenres.length > 0">
      <span class="filter-label">Filter:</span>
      <div class="genre-chips">
        <button class="genre-chip" :class="{ active: activeGenres.length === 0 }" @click="activeGenres = []">All</button>
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

    <!-- Band grid -->
    <div v-if="displayedBands.length > 0" class="band-grid">
      <div
        v-for="band in displayedBands"
        :key="band.id"
        class="band-card"
        :class="{ expanded: expandedBandId === band.id }"
      >
        <!-- Band header — click to expand/collapse albums -->
        <div class="band-header" @click="toggleBand(band)">
          <div class="band-photo-wrap">
            <img
              v-if="band.images && band.images.length > 0"
              :src="band.images[0].url"
              :alt="band.name"
              class="band-photo"
            />
            <div v-else class="band-photo-placeholder">🤘</div>
          </div>
          <div class="band-meta">
            <h3 class="band-name">{{ band.name }}</h3>
            <div class="genre-tags" v-if="band.genres && band.genres.length > 0">
              <span v-for="g in band.genres.slice(0, 4)" :key="g" class="genre-tag">{{ g }}</span>
            </div>
            <div class="popularity-bar" v-if="band.popularity !== undefined">
              <div class="pop-track">
                <div class="pop-fill" :style="{ width: band.popularity + '%' }"></div>
              </div>
              <span class="pop-value">{{ band.popularity }}</span>
            </div>
          </div>
          <span class="expand-hint">{{ expandedBandId === band.id ? '▲' : '▼' }}</span>
        </div>

        <!-- Albums section — shown when band is expanded -->
        <div v-if="expandedBandId === band.id" class="albums-section" @click.stop>
          <div v-if="loadingAlbums.has(band.id)" class="albums-loading">
            <loading-spinner :spin="true" />
          </div>
          <div v-else-if="bandAlbums[band.id] && bandAlbums[band.id].length > 0">
            <h4 class="albums-title">Albums</h4>
            <div class="albums-grid">
              <div
                v-for="album in bandAlbums[band.id]"
                :key="album.id"
                class="album-tile"
                :class="{ playing: expandedAlbumId === album.id }"
                @click="toggleAlbum(album.id)"
              >
                <div class="album-cover-wrap">
                  <img
                    v-if="album.images && album.images.length > 0"
                    :src="album.images[0].url"
                    :alt="album.name"
                    class="album-cover"
                  />
                  <div v-else class="album-cover-placeholder">💿</div>
                  <div class="album-play-overlay">{{ expandedAlbumId === album.id ? '■' : '▶' }}</div>
                </div>
                <p class="album-name">{{ album.name }}</p>
                <p class="album-year" v-if="album.release_date">{{ album.release_date.substring(0, 4) }}</p>

                <!-- Spotify player for this album -->
                <div v-if="expandedAlbumId === album.id" class="album-player" @click.stop>
                  <iframe
                    :src="`https://open.spotify.com/embed/album/${album.id}?theme=0`"
                    width="100%"
                    height="380"
                    frameborder="0"
                    allowtransparency="true"
                    allow="autoplay; clipboard-write; encrypted-media; fullscreen; picture-in-picture"
                    class="spotify-iframe"
                  ></iframe>
                </div>
              </div>
            </div>
          </div>
          <div v-else class="no-albums">No albums found.</div>
        </div>
      </div>
    </div>

    <div v-if="searched && !isLoading && displayedBands.length === 0" class="no-results">
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
      genreResults: [],
      searched: false,
      isLoading: false,
      allGenres: [],
      activeGenres: [],
      expandedBandId: null,
      expandedAlbumId: null,
      bandAlbums: {},        // { [bandId]: album[] }
      loadingAlbums: new Set(),
    };
  },
  computed: {
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
      this.expandedBandId = null;
      this.expandedAlbumId = null;
      try {
        const response = await api.get('/bands/searchByDeathMetalGenre');
        this.genreResults = response.data.artists?.items || [];
        this.buildGenreList();
        if (this.genreResults.length > 0) {
          this.toast.success(`Loaded ${this.genreResults.length} bands`);
        } else {
          this.toast.warning('No bands found.');
        }
      } catch {
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
      this.allGenres = [];
      this.activeGenres = [];
      this.expandedBandId = null;
      this.expandedAlbumId = null;
      try {
        const response = await api.get(`/bands/searchByBandName?bandName=${encodeURIComponent(name)}`);
        this.genreResults = response.data.artists?.items || [];
        if (this.genreResults.length > 0) {
          this.toast.success(`Found ${this.genreResults.length} result(s) for "${name}"`);
        } else {
          this.toast.warning(`No bands found for "${name}"`);
        }
      } catch {
        this.genreResults = [];
        this.toast.error('Search failed. Try again.');
      } finally {
        this.searched = true;
        this.isLoading = false;
      }
    },

    async toggleBand(band) {
      if (this.expandedBandId === band.id) {
        this.expandedBandId = null;
        this.expandedAlbumId = null;
        return;
      }
      this.expandedBandId = band.id;
      this.expandedAlbumId = null;

      // Fetch albums if not already loaded
      if (!this.bandAlbums[band.id]) {
        this.loadingAlbums = new Set([...this.loadingAlbums, band.id]);
        try {
          const response = await api.get(`/bands/${encodeURIComponent(band.id)}/albums`);
          this.bandAlbums = { ...this.bandAlbums, [band.id]: response.data.items || [] };
        } catch {
          this.bandAlbums = { ...this.bandAlbums, [band.id]: [] };
          this.toast.error('Could not load albums for this band.');
        } finally {
          const next = new Set(this.loadingAlbums);
          next.delete(band.id);
          this.loadingAlbums = next;
        }
      }
    },

    toggleAlbum(albumId) {
      this.expandedAlbumId = this.expandedAlbumId === albumId ? null : albumId;
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
      if (idx === -1) this.activeGenres.push(genre);
      else this.activeGenres.splice(idx, 1);
    },

    clearResults() {
      this.bandName = '';
      this.genreResults = [];
      this.allGenres = [];
      this.activeGenres = [];
      this.searched = false;
      this.expandedBandId = null;
      this.expandedAlbumId = null;
      this.bandAlbums = {};
    },
  },
};
</script>

<style scoped>
#search-section {
  padding-bottom: 3rem;
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
  border: 1px solid #333;
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

input::placeholder { color: #555; }

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
}

.btn-primary:hover:not(:disabled) { background-color: #a30000; }
.btn-primary:active:not(:disabled) { transform: scale(0.97); }
.btn-primary:disabled { opacity: 0.6; cursor: not-allowed; }

.btn-ghost {
  padding: 0.65rem 1.2rem;
  background: transparent;
  color: #aaa;
  border: 1px solid #333;
  border-radius: 6px;
  font-size: 0.95rem;
  cursor: pointer;
  transition: color 0.2s, border-color 0.2s;
}

.btn-ghost:hover { color: #fff; border-color: #666; }

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
  font-size: 0.85rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  padding-top: 0.3rem;
  white-space: nowrap;
}

.genre-chips { display: flex; flex-wrap: wrap; gap: 0.4rem; }

.genre-chip {
  padding: 0.25rem 0.8rem;
  border-radius: 20px;
  font-size: 0.78rem;
  font-weight: 600;
  cursor: pointer;
  border: 1px solid #333;
  background: #1a1a1a;
  color: #aaa;
  transition: all 0.15s;
  text-transform: capitalize;
}

.genre-chip:hover { border-color: crimson; color: #fff; }
.genre-chip.active { background-color: crimson; border-color: crimson; color: #fff; }

/* Band grid */
.band-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1rem;
  align-items: start;
}

.band-card {
  background: #111;
  border-radius: 12px;
  overflow: hidden;
  transition: box-shadow 0.2s;
}

.band-card.expanded {
  box-shadow: 0 0 24px rgba(220, 20, 60, 0.2);
}

.band-header {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  cursor: pointer;
  transition: background 0.15s;
  user-select: none;
}

.band-header:hover { background: #161616; }

.band-photo-wrap { flex-shrink: 0; }

.band-photo {
  width: 72px;
  height: 72px;
  border-radius: 8px;
  object-fit: cover;
}

.band-photo-placeholder {
  width: 72px;
  height: 72px;
  border-radius: 8px;
  background: #1e1e1e;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 2rem;
}

.band-meta { flex: 1; min-width: 0; }

.band-name {
  font-size: 1rem;
  font-weight: 700;
  color: #fff;
  margin: 0 0 0.35rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.genre-tags { display: flex; flex-wrap: wrap; gap: 0.25rem; margin-bottom: 0.4rem; }

.genre-tag {
  background: #1e1e1e;
  color: #999;
  font-size: 0.68rem;
  padding: 0.1rem 0.45rem;
  border-radius: 10px;
  text-transform: capitalize;
}

.popularity-bar { display: flex; align-items: center; gap: 0.4rem; }

.pop-track {
  flex: 1;
  height: 3px;
  background: #222;
  border-radius: 2px;
  overflow: hidden;
}

.pop-fill {
  height: 100%;
  background: crimson;
  border-radius: 2px;
}

.pop-value { font-size: 0.68rem; color: #666; min-width: 20px; }

.expand-hint {
  color: #555;
  font-size: 0.75rem;
  flex-shrink: 0;
}

/* Albums section */
.albums-section {
  padding: 0 1rem 1rem;
  border-top: 1px solid #1e1e1e;
}

.albums-loading {
  display: flex;
  justify-content: center;
  padding: 1rem 0;
}

.albums-title {
  font-size: 0.85rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: #666;
  margin: 1rem 0 0.75rem;
}

.albums-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(110px, 1fr));
  gap: 0.75rem;
}

.album-tile {
  cursor: pointer;
  border-radius: 8px;
  overflow: hidden;
  transition: transform 0.15s;
}

.album-tile:hover { transform: scale(1.03); }
.album-tile.playing { grid-column: 1 / -1; }

.album-cover-wrap {
  position: relative;
  aspect-ratio: 1;
  border-radius: 6px;
  overflow: hidden;
}

.album-tile.playing .album-cover-wrap {
  aspect-ratio: unset;
  height: 110px;
  width: 110px;
  flex-shrink: 0;
}

.album-tile.playing {
  display: flex;
  align-items: flex-start;
  gap: 1rem;
  padding: 0.75rem;
  background: #161616;
  border-radius: 8px;
}

.album-cover {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.album-cover-placeholder {
  width: 100%;
  height: 100%;
  background: #1e1e1e;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 2rem;
}

.album-play-overlay {
  position: absolute;
  inset: 0;
  background: rgba(0,0,0,0.55);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.4rem;
  color: #fff;
  opacity: 0;
  transition: opacity 0.15s;
}

.album-cover-wrap:hover .album-play-overlay,
.album-tile.playing .album-play-overlay {
  opacity: 1;
}

.album-name {
  font-size: 0.75rem;
  font-weight: 600;
  color: #ddd;
  margin: 0.35rem 0 0;
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.album-tile.playing .album-name {
  text-align: left;
  font-size: 0.9rem;
  white-space: normal;
  color: #fff;
}

.album-year {
  font-size: 0.68rem;
  color: #666;
  margin: 0.15rem 0 0;
  text-align: center;
}

.album-tile.playing .album-year { text-align: left; }

.album-player {
  flex: 1;
  min-width: 0;
}

.spotify-iframe {
  display: block;
  border: none;
  border-radius: 8px;
  width: 100%;
}

.no-albums {
  color: #555;
  font-size: 0.9rem;
  padding: 1rem 0;
  text-align: center;
}

.no-results {
  text-align: center;
  color: #555;
  padding: 3rem 0;
  font-size: 1.1rem;
}

@media (max-width: 600px) {
  .search-controls { flex-direction: column; }
  .search-input-group { width: 100%; }
  input[type='text'] { width: 100%; min-width: unset; flex: 1; }
  .band-grid { grid-template-columns: 1fr; }
}
</style>
