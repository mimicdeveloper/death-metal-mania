<template>
  <section id="search-section">
    <h2 class="section-title">Death Metal Bands</h2>

    <!-- Primary genre buttons -->
    <div class="genre-buttons">
      <button
        v-for="g in genreButtons"
        :key="g.key"
        class="genre-btn"
        :class="{ active: activeButton === g.key }"
        :disabled="isLoading"
        @click="loadGenre(g)"
      >{{ g.label }}</button>
    </div>

    <!-- Band name search -->
    <div class="search-row">
      <input type="text" v-model="bandName" placeholder="Search by band name..." @keyup.enter="searchBand" />
      <button class="btn-primary" @click="searchBand" :disabled="isLoading">Search</button>
      <button class="btn-ghost" @click="clearResults" v-if="genreResults.length > 0">Clear</button>
    </div>

    <!-- Subgenre filter chips — only after a genre load -->
    <div class="filter-bar" v-if="allGenres.length > 0">
      <span class="filter-label">Filter:</span>
      <div class="filter-chips">
        <button class="chip" :class="{ active: activeGenres.length === 0 }" @click="activeGenres = []">All</button>
        <button
          v-for="genre in allGenres"
          :key="genre.label"
          class="chip"
          :class="{ active: activeGenres.some(g => g.label === genre.label) }"
          @click="toggleGenre(genre)"
        >{{ genre.label }}</button>
      </div>
    </div>

    <loading-spinner :spin="isLoading" id="spinner" />

    <!-- Results count + pagination info -->
    <div class="results-meta" v-if="displayedBands.length > 0">
      <span>{{ displayedBands.length }} bands{{ activeGenres.length ? ' (filtered)' : '' }}</span>
      <span v-if="totalPages > 1">Page {{ currentPage }} of {{ totalPages }}</span>
    </div>

    <!-- Band grid -->
    <div v-if="pagedBands.length > 0" class="band-grid">
      <div
        v-for="band in pagedBands"
        :key="band.id"
        class="band-card"
        :class="{ expanded: expandedBandId === band.id }"
      >
        <div class="band-header" @click="toggleBand(band)">
          <div class="band-photo-wrap">
            <img v-if="band.images && band.images.length > 0" :src="band.images[0].url" :alt="band.name" class="band-photo" />
            <div v-else class="band-photo-placeholder">🤘</div>
          </div>
          <div class="band-meta">
            <h3 class="band-name">{{ band.name }}</h3>
            <div class="genre-tags" v-if="band.genres && band.genres.length > 0">
              <span v-for="g in band.genres.slice(0, 3)" :key="g" class="genre-tag">{{ g }}</span>
            </div>
            <div class="popularity-bar" v-if="band.popularity !== undefined">
              <div class="pop-track"><div class="pop-fill" :style="{ width: band.popularity + '%' }"></div></div>
              <span class="pop-value">{{ band.popularity }}</span>
            </div>
          </div>
          <span class="chevron">{{ expandedBandId === band.id ? '▲' : '▼' }}</span>
        </div>

        <!-- Albums -->
        <div v-if="expandedBandId === band.id" class="albums-section" @click.stop>
          <div v-if="loadingAlbums.has(band.id)" class="albums-loading">
            <loading-spinner :spin="true" />
          </div>
          <div v-else-if="bandAlbums[band.id] && bandAlbums[band.id].length > 0">
            <h4 class="albums-title">Albums — tap to preview</h4>
            <div class="albums-grid">
              <div
                v-for="album in bandAlbums[band.id]"
                :key="album.id"
                class="album-tile"
                :class="{ selected: expandedAlbumId === album.id }"
                @click="toggleAlbum(album.id)"
              >
                <div class="album-cover-wrap">
                  <img v-if="album.images && album.images.length > 0" :src="album.images[0].url" :alt="album.name" class="album-cover" />
                  <div v-else class="album-cover-placeholder">💿</div>
                  <div class="album-play-overlay">{{ expandedAlbumId === album.id ? '■' : '▶' }}</div>
                </div>
                <p class="album-name">{{ album.name }}</p>
                <p class="album-year" v-if="album.release_date">{{ album.release_date.substring(0, 4) }}</p>
              </div>
            </div>

            <div v-if="expandedAlbumId" class="player-panel" @click.stop>
              <template v-for="album in bandAlbums[band.id]" :key="album.id">
                <div v-if="album.id === expandedAlbumId" class="player-inner">
                  <div class="player-info">
                    <img v-if="album.images && album.images.length > 0" :src="album.images[0].url" :alt="album.name" class="player-thumb" />
                    <div class="player-text">
                      <p class="player-album-name">{{ album.name }}</p>
                      <p class="player-year" v-if="album.release_date">{{ album.release_date.substring(0, 4) }}</p>
                    </div>
                    <button class="player-close" @click="expandedAlbumId = null">✕</button>
                  </div>
                  <iframe
                    :src="`https://open.spotify.com/embed/album/${album.id}?theme=0`"
                    width="100%" height="352" frameborder="0"
                    allowtransparency="true"
                    allow="autoplay; clipboard-write; encrypted-media; fullscreen; picture-in-picture"
                    class="spotify-iframe"
                  ></iframe>
                </div>
              </template>
            </div>
          </div>
          <div v-else class="no-albums">No albums found.</div>
        </div>
      </div>
    </div>

    <!-- Pagination -->
    <div class="pagination" v-if="totalPages > 1">
      <button class="page-btn" @click="goPage(1)" :disabled="currentPage === 1">«</button>
      <button class="page-btn" @click="goPage(currentPage - 1)" :disabled="currentPage === 1">‹</button>
      <button
        v-for="p in visiblePages"
        :key="p"
        class="page-btn"
        :class="{ active: p === currentPage }"
        @click="goPage(p)"
      >{{ p }}</button>
      <button class="page-btn" @click="goPage(currentPage + 1)" :disabled="currentPage === totalPages">›</button>
      <button class="page-btn" @click="goPage(totalPages)" :disabled="currentPage === totalPages">»</button>
    </div>

    <div v-if="searched && !isLoading && displayedBands.length === 0" class="no-results">No results found.</div>
  </section>
</template>

<script>
import api from '@/api';
import LoadingSpinner from '@/components/LoadingSpinner.vue';
import { useToast } from 'vue-toastification';

const SUBGENRE_BUTTONS = [
  { key: 'death-metal',   label: 'Death Metal',           genre: 'death metal',       allow: 'death metal,slam,brutal death,hardcore death,blackened death,death and roll,swedish death,finnish death,old school death,cavernous', block: 'deathcore,metalcore' },
  { key: 'slam',          label: 'Slam',                  genre: 'slam death metal',  allow: 'slam,slamming,brutal slam', block: 'deathcore' },
  { key: 'brutal',        label: 'Brutal Death Metal',    genre: 'brutal death metal',allow: 'brutal death',   block: 'deathcore,metalcore' },
  { key: 'cavernous',     label: 'Cavernous Death Metal', genre: 'cavernous death metal', allow: 'cavernous', block: '' },
  { key: 'blackened',     label: 'Blackened Death Metal', genre: 'blackened death metal', allow: 'blackened death', block: '' },
  { key: 'old-school',    label: 'Old School Death Metal',genre: 'old school death metal', allow: 'old school death,death metal', block: 'deathcore' },
  { key: 'swedish',       label: 'Swedish Death Metal',   genre: 'swedish death metal', allow: 'swedish death', block: '' },
  { key: 'death-roll',    label: 'Death & Roll',          genre: 'death and roll',    allow: 'death and roll,death metal', block: '' },
  { key: 'technical',     label: 'Technical Death Metal', genre: 'technical death metal', allow: 'technical death,death metal', block: 'deathcore' },
  { key: 'hardcore-death',label: 'Hardcore Death Metal',  genre: 'hardcore death metal',  allow: 'hardcore death,death metal', block: 'deathcore,metalcore' },
];

const PAGE_SIZE = 24;

export default {
  name: 'SearchBands',
  components: { LoadingSpinner },
  setup() {
    return { toast: useToast() };
  },
  data() {
    return {
      bandName: '',
      genreResults: [],
      searched: false,
      isLoading: false,
      allGenres: [],
      activeGenres: [],
      activeButton: null,
      currentPage: 1,
      expandedBandId: null,
      expandedAlbumId: null,
      bandAlbums: {},
      loadingAlbums: new Set(),
      genreButtons: SUBGENRE_BUTTONS,
    };
  },
  computed: {
    displayedBands() {
      if (this.activeGenres.length === 0) return this.genreResults;
      return this.genreResults.filter(band =>
        band.genres && this.activeGenres.some(active =>
          band.genres.some(bg => bg.toLowerCase().includes(active.match))
        )
      );
    },
    totalPages() {
      return Math.ceil(this.displayedBands.length / PAGE_SIZE);
    },
    pagedBands() {
      const start = (this.currentPage - 1) * PAGE_SIZE;
      return this.displayedBands.slice(start, start + PAGE_SIZE);
    },
    visiblePages() {
      const total = this.totalPages;
      const cur = this.currentPage;
      if (total <= 7) return Array.from({ length: total }, (_, i) => i + 1);
      const pages = new Set([1, total, cur, cur - 1, cur + 1].filter(p => p >= 1 && p <= total));
      return [...pages].sort((a, b) => a - b);
    },
  },
  methods: {
    async loadGenre(g) {
      this.isLoading = true;
      this.activeButton = g.key;
      this.expandedBandId = null;
      this.expandedAlbumId = null;
      this.currentPage = 1;
      this.activeGenres = [];
      try {
        const params = new URLSearchParams({ genre: g.genre });
        if (g.allow) params.append('allow', g.allow);
        if (g.block) params.append('block', g.block);
        const response = await api.get(`/bands/searchBySubgenre?${params}`);
        this.genreResults = response.data.artists?.items || [];
        this.buildGenreList();
        if (this.genreResults.length > 0) {
          this.toast.success(`Loaded ${this.genreResults.length} bands`);
        } else {
          this.toast.warning(`No results for ${g.label} — try a broader genre`);
        }
      } catch {
        this.genreResults = [];
        this.toast.error('Could not reach the backend. Is Koyeb up?');
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
      this.activeButton = null;
      this.expandedBandId = null;
      this.expandedAlbumId = null;
      this.currentPage = 1;
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
      if (!this.bandAlbums[band.id]) {
        this.loadingAlbums = new Set([...this.loadingAlbums, band.id]);
        try {
          const response = await api.get(`/bands/${encodeURIComponent(band.id)}/albums`);
          this.bandAlbums = { ...this.bandAlbums, [band.id]: response.data.items || [] };
        } catch {
          this.bandAlbums = { ...this.bandAlbums, [band.id]: [] };
          this.toast.error('Could not load albums.');
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
      const approved = [
        { label: 'Death Metal',           match: 'death metal' },
        { label: 'Slam',                  match: 'slam' },
        { label: 'Brutal Death Metal',    match: 'brutal death' },
        { label: 'Hardcore Death Metal',  match: 'hardcore death' },
        { label: 'Cavernous Death Metal', match: 'cavernous' },
        { label: 'Blackened Death Metal', match: 'blackened death' },
        { label: 'Death & Roll',          match: 'death and roll' },
        { label: 'Swedish Death Metal',   match: 'swedish death' },
        { label: 'Finnish Death Metal',   match: 'finnish death' },
        { label: 'Old School Death Metal',match: 'old school death' },
        { label: 'Technical Death Metal', match: 'technical death' },
        { label: 'Hardcore Death Metal',  match: 'hardcore death' },
      ];
      this.allGenres = approved.filter(g =>
        this.genreResults.some(band =>
          band.genres && band.genres.some(bg => bg.toLowerCase().includes(g.match))
        )
      );
      this.activeGenres = [];
    },

    toggleGenre(genre) {
      const idx = this.activeGenres.findIndex(g => g.label === genre.label);
      if (idx === -1) this.activeGenres.push(genre);
      else this.activeGenres.splice(idx, 1);
      this.currentPage = 1;
    },

    goPage(p) {
      if (p < 1 || p > this.totalPages) return;
      this.currentPage = p;
      this.expandedBandId = null;
      this.expandedAlbumId = null;
      window.scrollTo({ top: 0, behavior: 'smooth' });
    },

    clearResults() {
      this.bandName = '';
      this.genreResults = [];
      this.allGenres = [];
      this.activeGenres = [];
      this.searched = false;
      this.activeButton = null;
      this.expandedBandId = null;
      this.expandedAlbumId = null;
      this.currentPage = 1;
      this.bandAlbums = {};
    },
  },
};
</script>

<style scoped>
#search-section { padding-bottom: 3rem; }

.section-title {
  text-align: center;
  font-size: 1.8rem;
  font-weight: 900;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: #fff;
  margin-bottom: 1.5rem;
}

/* Genre buttons */
.genre-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 0.6rem;
  justify-content: center;
  margin-bottom: 1.25rem;
}

.genre-btn {
  padding: 0.5rem 1rem;
  background: #1a1a1a;
  color: #aaa;
  border: 1px solid #2a2a2a;
  border-radius: 6px;
  font-size: 0.82rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  cursor: pointer;
  transition: all 0.15s;
}

.genre-btn:hover:not(:disabled) { background: #222; color: #fff; border-color: #444; }
.genre-btn.active { background: crimson; color: #fff; border-color: crimson; }
.genre-btn:disabled { opacity: 0.5; cursor: not-allowed; }

/* Search row */
.search-row {
  display: flex;
  align-items: center;
  gap: 0.6rem;
  justify-content: center;
  margin-bottom: 1.25rem;
  flex-wrap: wrap;
}

input[type='text'] {
  padding: 0.6rem 1rem;
  font-size: 1rem;
  border: 1px solid #2a2a2a;
  border-radius: 6px;
  background: #141414;
  color: #fff;
  min-width: 220px;
  flex: 1;
  max-width: 380px;
  transition: border-color 0.2s;
}

input[type='text']:focus { outline: none; border-color: crimson; }
input::placeholder { color: #444; }

.btn-primary {
  padding: 0.6rem 1.25rem;
  background: crimson;
  color: #fff;
  border: none;
  border-radius: 6px;
  font-size: 0.9rem;
  font-weight: 700;
  cursor: pointer;
  transition: background 0.15s;
  white-space: nowrap;
}

.btn-primary:hover:not(:disabled) { background: #a30000; }
.btn-primary:disabled { opacity: 0.5; cursor: not-allowed; }

.btn-ghost {
  padding: 0.6rem 1rem;
  background: transparent;
  color: #555;
  border: 1px solid #2a2a2a;
  border-radius: 6px;
  font-size: 0.88rem;
  cursor: pointer;
  transition: color 0.15s, border-color 0.15s;
  white-space: nowrap;
}

.btn-ghost:hover { color: #aaa; border-color: #444; }

/* Filter bar */
.filter-bar {
  display: flex;
  align-items: flex-start;
  gap: 0.6rem;
  margin-bottom: 1.25rem;
  flex-wrap: wrap;
}

.filter-label {
  color: #555;
  font-size: 0.75rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  padding-top: 0.3rem;
  white-space: nowrap;
}

.filter-chips { display: flex; flex-wrap: wrap; gap: 0.35rem; }

.chip {
  padding: 0.2rem 0.7rem;
  border-radius: 20px;
  font-size: 0.72rem;
  font-weight: 600;
  cursor: pointer;
  border: 1px solid #2a2a2a;
  background: #141414;
  color: #888;
  transition: all 0.12s;
  text-transform: capitalize;
}

.chip:hover { border-color: crimson; color: #fff; }
.chip.active { background: crimson; border-color: crimson; color: #fff; }

/* Results meta */
.results-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.78rem;
  color: #444;
  margin-bottom: 0.75rem;
}

/* Band grid */
.band-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 0.75rem;
  align-items: start;
}

.band-card { background: #111; border-radius: 10px; overflow: hidden; }
.band-card.expanded { box-shadow: 0 0 20px rgba(220,20,60,0.18); }

.band-header {
  display: flex;
  align-items: center;
  gap: 0.85rem;
  padding: 0.85rem;
  cursor: pointer;
  user-select: none;
  transition: background 0.12s;
}

.band-header:hover { background: #161616; }

.band-photo-wrap { flex-shrink: 0; }

.band-photo {
  width: 64px;
  height: 64px;
  border-radius: 8px;
  object-fit: cover;
}

.band-photo-placeholder {
  width: 64px;
  height: 64px;
  border-radius: 8px;
  background: #1c1c1c;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.8rem;
}

.band-meta { flex: 1; min-width: 0; }

.band-name {
  font-size: 0.95rem;
  font-weight: 700;
  color: #fff;
  margin: 0 0 0.3rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.genre-tags { display: flex; flex-wrap: wrap; gap: 0.2rem; margin-bottom: 0.35rem; }

.genre-tag {
  background: #1c1c1c;
  color: #777;
  font-size: 0.65rem;
  padding: 0.1rem 0.4rem;
  border-radius: 10px;
  text-transform: capitalize;
}

.popularity-bar { display: flex; align-items: center; gap: 0.35rem; }

.pop-track { flex: 1; height: 3px; background: #1e1e1e; border-radius: 2px; overflow: hidden; }
.pop-fill { height: 100%; background: crimson; border-radius: 2px; }
.pop-value { font-size: 0.62rem; color: #444; min-width: 18px; }

.chevron { color: #333; font-size: 0.7rem; flex-shrink: 0; }

/* Albums */
.albums-section { padding: 0 0.85rem 0.85rem; border-top: 1px solid #1a1a1a; }
.albums-loading { display: flex; justify-content: center; padding: 1rem; }

.albums-title {
  font-size: 0.72rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: #444;
  margin: 0.85rem 0 0.65rem;
}

.albums-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(90px, 1fr));
  gap: 0.5rem;
}

.album-tile { cursor: pointer; border-radius: 6px; overflow: hidden; transition: transform 0.12s; }
.album-tile:hover { transform: scale(1.05); }
.album-tile.selected { outline: 2px solid crimson; outline-offset: 2px; }

.album-cover-wrap { position: relative; aspect-ratio: 1; border-radius: 5px; overflow: hidden; }

.album-cover { width: 100%; height: 100%; object-fit: cover; display: block; }

.album-cover-placeholder {
  width: 100%; height: 100%;
  background: #1c1c1c;
  display: flex; align-items: center; justify-content: center;
  font-size: 1.5rem;
}

.album-play-overlay {
  position: absolute; inset: 0;
  background: rgba(0,0,0,0.6);
  display: flex; align-items: center; justify-content: center;
  font-size: 1.2rem; color: #fff;
  opacity: 0; transition: opacity 0.12s;
}

.album-cover-wrap:hover .album-play-overlay,
.album-tile.selected .album-play-overlay { opacity: 1; }

.album-name {
  font-size: 0.65rem;
  font-weight: 600;
  color: #bbb;
  margin: 0.25rem 0 0;
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.album-year { font-size: 0.6rem; color: #444; margin: 0; text-align: center; }

/* Player panel */
.player-panel { margin-top: 0.85rem; background: #0d0d0d; border-radius: 8px; overflow: hidden; }

.player-inner { display: flex; flex-direction: column; }

.player-info {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.75rem;
  border-bottom: 1px solid #1a1a1a;
}

.player-thumb { width: 38px; height: 38px; border-radius: 4px; object-fit: cover; flex-shrink: 0; }

.player-text { flex: 1; min-width: 0; }
.player-album-name { font-size: 0.85rem; font-weight: 700; color: #fff; margin: 0; }
.player-year { font-size: 0.68rem; color: #555; margin: 0.1rem 0 0; }

.player-close {
  background: none; border: none; color: #444; font-size: 0.95rem;
  cursor: pointer; padding: 0.25rem 0.4rem; border-radius: 4px; flex-shrink: 0;
  transition: color 0.15s;
}

.player-close:hover { color: crimson; }

.spotify-iframe { display: block; border: none; width: 100%; }

/* Pagination */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 0.3rem;
  margin-top: 1.5rem;
  flex-wrap: wrap;
}

.page-btn {
  padding: 0.4rem 0.7rem;
  background: #141414;
  color: #666;
  border: 1px solid #222;
  border-radius: 5px;
  font-size: 0.82rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.12s;
  min-width: 36px;
}

.page-btn:hover:not(:disabled) { background: #1e1e1e; color: #fff; border-color: #444; }
.page-btn.active { background: crimson; color: #fff; border-color: crimson; }
.page-btn:disabled { opacity: 0.3; cursor: default; }

.no-albums, .no-results { text-align: center; color: #444; padding: 2rem 0; font-size: 0.9rem; }

@media (max-width: 600px) {
  .genre-buttons { gap: 0.4rem; }
  .genre-btn { font-size: 0.75rem; padding: 0.4rem 0.75rem; }
  .search-row { flex-direction: column; align-items: stretch; }
  input[type='text'] { max-width: 100%; min-width: unset; }
  .band-grid { grid-template-columns: 1fr; }
  .albums-grid { grid-template-columns: repeat(auto-fill, minmax(75px, 1fr)); }
}
</style>
