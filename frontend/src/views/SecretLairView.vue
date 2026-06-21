<template>
  <div class="lair-wrap">
    <div class="lair-header">
      <div class="lair-skull">☠</div>
      <h1 class="lair-title">The Secret Lair</h1>
      <p class="lair-subtitle">Goregrind · Crust Punk · Grindcore · D-Beat</p>
      <p class="lair-warning">You weren't supposed to find this.</p>
    </div>

    <!-- Genre buttons -->
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

    <loading-spinner :spin="isLoading" />

    <!-- Filter chips -->
    <div class="filter-bar" v-if="allGenres.length > 0">
      <span class="filter-label">Filter:</span>
      <div class="filter-chips">
        <button class="chip" :class="{ active: activeGenres.length === 0 }" @click="activeGenres = []; currentPage = 1;">All</button>
        <button
          v-for="genre in allGenres"
          :key="genre.label"
          class="chip"
          :class="{ active: activeGenres.some(g => g.label === genre.label) }"
          @click="toggleGenre(genre)"
        >{{ genre.label }}</button>
      </div>
    </div>

    <!-- Results meta -->
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
            <div v-else class="band-photo-placeholder">💀</div>
          </div>
          <div class="band-meta">
            <h3 class="band-name">{{ band.name }}</h3>
            <div class="genre-tags" v-if="band.genres && band.genres.length > 0">
              <span v-for="g in band.genres.slice(0, 3)" :key="g" class="genre-tag">{{ g }}</span>
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
            <h4 class="albums-title">Albums</h4>
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

    <div v-if="searched && !isLoading && displayedBands.length === 0" class="no-results">No results. The lair is quiet... for now.</div>

    <div class="escape-link">
      <router-link to="/" class="escape">← Back to the Surface</router-link>
    </div>
  </div>
</template>

<script>
import api from '@/api';
import LoadingSpinner from '@/components/LoadingSpinner.vue';
import { useToast } from 'vue-toastification';

const LAIR_GENRES = [
  { key: 'goregrind', label: 'Goregrind',   genre: 'goregrind',  allow: 'goregrind,gore,grindcore', block: '' },
  { key: 'crust',     label: 'Crust Punk',  genre: 'crust punk', allow: 'crust,d-beat,discharge',   block: '' },
  { key: 'grindcore', label: 'Grindcore',   genre: 'grindcore',  allow: 'grindcore,powerviolence',  block: '' },
  { key: 'noisecore', label: 'Noisecore',   genre: 'noisecore',  allow: 'noisecore,noise,harsh',    block: '' },
];

const PAGE_SIZE = 24;

export default {
  name: 'SecretLairView',
  components: { LoadingSpinner },
  setup() {
    return { toast: useToast() };
  },
  data() {
    return {
      genreButtons: LAIR_GENRES,
      activeButton: null,
      isLoading: false,
      searched: false,
      genreResults: [],
      allGenres: [],
      activeGenres: [],
      currentPage: 1,
      expandedBandId: null,
      expandedAlbumId: null,
      bandAlbums: {},
      loadingAlbums: new Set(),
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
          this.toast.success(`${this.genreResults.length} crawled out of the lair`);
        } else {
          this.toast.warning(`Nothing stirred in the lair for ${g.label}`);
        }
      } catch {
        this.genreResults = [];
        this.toast.error('The lair is sealed. Try again.');
      } finally {
        this.searched = true;
        this.isLoading = false;
      }
    },

    buildGenreList() {
      const approved = [
        { label: 'Goregrind',      match: 'goregrind' },
        { label: 'Grindcore',      match: 'grindcore' },
        { label: 'Crust Punk',     match: 'crust' },
        { label: 'D-Beat',         match: 'd-beat' },
        { label: 'Noisecore',      match: 'noisecore' },
        { label: 'Powerviolence',  match: 'powerviolence' },
        { label: 'Harsh Noise',    match: 'harsh' },
      ];
      this.allGenres = approved.filter(g =>
        this.genreResults.some(band =>
          band.genres && band.genres.some(bg => bg.toLowerCase().includes(g.match))
        )
      );
    },

    toggleGenre(genre) {
      const idx = this.activeGenres.findIndex(g => g.label === genre.label);
      if (idx === -1) this.activeGenres.push(genre);
      else this.activeGenres.splice(idx, 1);
      this.currentPage = 1;
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

    goPage(p) {
      if (p < 1 || p > this.totalPages) return;
      this.currentPage = p;
      this.expandedBandId = null;
      this.expandedAlbumId = null;
      window.scrollTo({ top: 0, behavior: 'smooth' });
    },
  },
};
</script>

<style scoped>
.lair-wrap {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem 1.5rem 4rem;
}

.lair-header {
  text-align: center;
  padding: 2rem 0 2.5rem;
}

.lair-skull {
  font-size: 3.5rem;
  animation: pulse-skull 3s ease-in-out infinite;
}

@keyframes pulse-skull {
  0%, 100% { opacity: 1; transform: scale(1); }
  50%       { opacity: 0.6; transform: scale(0.95); }
}

.lair-title {
  font-size: 2.5rem;
  font-weight: 900;
  text-transform: uppercase;
  letter-spacing: 0.12em;
  color: crimson;
  margin: 0.5rem 0 0.3rem;
  text-shadow: 0 0 30px rgba(220, 20, 60, 0.4);
}

.lair-subtitle {
  font-size: 0.88rem;
  font-weight: 700;
  letter-spacing: 0.15em;
  text-transform: uppercase;
  color: #444;
  margin: 0 0 0.5rem;
}

.lair-warning {
  font-size: 0.75rem;
  color: #2a2a2a;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  margin: 0;
}

.genre-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 0.6rem;
  justify-content: center;
  margin-bottom: 1.5rem;
}

.genre-btn {
  padding: 0.5rem 1.25rem;
  background: #0d0d0d;
  color: #666;
  border: 1px solid #1e1e1e;
  border-radius: 6px;
  font-size: 0.82rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  cursor: pointer;
  transition: all 0.15s;
}

.genre-btn:hover:not(:disabled) { background: #151515; color: #ccc; border-color: #333; }
.genre-btn.active { background: #330010; color: crimson; border-color: crimson; }
.genre-btn:disabled { opacity: 0.4; cursor: not-allowed; }

.filter-bar {
  display: flex;
  align-items: flex-start;
  gap: 0.6rem;
  margin-bottom: 1.25rem;
  flex-wrap: wrap;
}

.filter-label { color: #333; font-size: 0.75rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.08em; padding-top: 0.3rem; }
.filter-chips { display: flex; flex-wrap: wrap; gap: 0.35rem; }

.chip {
  padding: 0.2rem 0.7rem;
  border-radius: 20px;
  font-size: 0.72rem;
  font-weight: 600;
  cursor: pointer;
  border: 1px solid #1e1e1e;
  background: #0d0d0d;
  color: #555;
  transition: all 0.12s;
}

.chip:hover { border-color: crimson; color: #fff; }
.chip.active { background: #1a0008; border-color: crimson; color: crimson; }

.results-meta { display: flex; justify-content: space-between; font-size: 0.78rem; color: #333; margin-bottom: 0.75rem; }

.band-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 0.75rem;
  align-items: start;
}

.band-card { background: #0a0a0a; border: 1px solid #111; border-radius: 10px; overflow: hidden; }
.band-card.expanded { border-color: #1a0008; box-shadow: 0 0 20px rgba(220,20,60,0.1); }

.band-header { display: flex; align-items: center; gap: 0.85rem; padding: 0.85rem; cursor: pointer; user-select: none; transition: background 0.12s; }
.band-header:hover { background: #0d0d0d; }

.band-photo-wrap { flex-shrink: 0; }
.band-photo { width: 64px; height: 64px; border-radius: 8px; object-fit: cover; filter: grayscale(30%); }
.band-photo-placeholder { width: 64px; height: 64px; border-radius: 8px; background: #0d0d0d; display: flex; align-items: center; justify-content: center; font-size: 1.8rem; }

.band-meta { flex: 1; min-width: 0; }
.band-name { font-size: 0.95rem; font-weight: 700; color: #ccc; margin: 0 0 0.3rem; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.genre-tags { display: flex; flex-wrap: wrap; gap: 0.2rem; }
.genre-tag { background: #0d0d0d; color: #444; font-size: 0.65rem; padding: 0.1rem 0.4rem; border-radius: 10px; text-transform: capitalize; }
.chevron { color: #222; font-size: 0.7rem; flex-shrink: 0; }

.albums-section { padding: 0 0.85rem 0.85rem; border-top: 1px solid #111; }
.albums-loading { display: flex; justify-content: center; padding: 1rem; }
.albums-title { font-size: 0.72rem; font-weight: 700; text-transform: uppercase; letter-spacing: 0.08em; color: #333; margin: 0.85rem 0 0.65rem; }

.albums-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(90px, 1fr)); gap: 0.5rem; }
.album-tile { cursor: pointer; border-radius: 6px; overflow: hidden; transition: transform 0.12s; }
.album-tile:hover { transform: scale(1.05); }
.album-tile.selected { outline: 2px solid crimson; outline-offset: 2px; }
.album-cover-wrap { position: relative; aspect-ratio: 1; border-radius: 5px; overflow: hidden; }
.album-cover { width: 100%; height: 100%; object-fit: cover; display: block; filter: grayscale(20%); }
.album-cover-placeholder { width: 100%; height: 100%; background: #0d0d0d; display: flex; align-items: center; justify-content: center; font-size: 1.5rem; }
.album-play-overlay { position: absolute; inset: 0; background: rgba(0,0,0,0.7); display: flex; align-items: center; justify-content: center; font-size: 1.2rem; color: crimson; opacity: 0; transition: opacity 0.12s; }
.album-cover-wrap:hover .album-play-overlay, .album-tile.selected .album-play-overlay { opacity: 1; }
.album-name { font-size: 0.65rem; font-weight: 600; color: #888; margin: 0.25rem 0 0; text-align: center; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.album-year { font-size: 0.6rem; color: #333; margin: 0; text-align: center; }

.player-panel { margin-top: 0.85rem; background: #060606; border-radius: 8px; overflow: hidden; border: 1px solid #111; }
.player-inner { display: flex; flex-direction: column; }
.player-info { display: flex; align-items: center; gap: 0.75rem; padding: 0.75rem; border-bottom: 1px solid #111; }
.player-thumb { width: 38px; height: 38px; border-radius: 4px; object-fit: cover; flex-shrink: 0; }
.player-text { flex: 1; min-width: 0; }
.player-album-name { font-size: 0.85rem; font-weight: 700; color: #ccc; margin: 0; }
.player-year { font-size: 0.68rem; color: #333; margin: 0.1rem 0 0; }
.player-close { background: none; border: none; color: #333; font-size: 0.95rem; cursor: pointer; padding: 0.25rem 0.4rem; border-radius: 4px; flex-shrink: 0; transition: color 0.15s; }
.player-close:hover { color: crimson; }
.spotify-iframe { display: block; border: none; width: 100%; }

.pagination { display: flex; justify-content: center; gap: 0.3rem; margin-top: 1.5rem; flex-wrap: wrap; }
.page-btn { padding: 0.4rem 0.7rem; background: #0a0a0a; color: #444; border: 1px solid #111; border-radius: 5px; font-size: 0.82rem; font-weight: 600; cursor: pointer; transition: all 0.12s; min-width: 36px; }
.page-btn:hover:not(:disabled) { background: #111; color: #ccc; border-color: #333; }
.page-btn.active { background: #1a0008; color: crimson; border-color: crimson; }
.page-btn:disabled { opacity: 0.3; cursor: default; }

.no-albums, .no-results { text-align: center; color: #333; padding: 2rem 0; font-size: 0.9rem; }

.escape-link { text-align: center; margin-top: 3rem; }
.escape { color: #333; font-size: 0.8rem; text-decoration: none; text-transform: uppercase; letter-spacing: 0.08em; transition: color 0.15s; }
.escape:hover { color: crimson; }

@media (max-width: 600px) {
  .band-grid { grid-template-columns: 1fr; }
  .lair-title { font-size: 1.8rem; }
}
</style>
