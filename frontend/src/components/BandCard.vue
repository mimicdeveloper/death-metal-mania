<template>
  <div class="band-card" :class="{ expanded: expanded }">
    <div class="band-header" @click="toggleBand">
      <div class="band-photo-wrap">
        <img v-if="band.images && band.images.length > 0" :src="band.images[0].url" :alt="band.name" class="band-photo" />
        <div v-else class="band-photo-placeholder">🤘</div>
      </div>
      <div class="band-meta">
        <h3 class="band-name">{{ band.name }}</h3>
        <p v-if="description" class="band-reason">{{ description }}</p>
        <div class="genre-tags" v-if="band.genres && band.genres.length > 0">
          <span v-for="g in band.genres.slice(0, 3)" :key="g" class="genre-tag">{{ g }}</span>
        </div>
        <div class="popularity-bar" v-if="band.popularity !== undefined">
          <div class="pop-track"><div class="pop-fill" :style="{ width: band.popularity + '%' }"></div></div>
          <span class="pop-value">{{ band.popularity }}</span>
        </div>
      </div>
      <span class="chevron">{{ expanded ? '▲' : '▼' }}</span>
    </div>

    <!-- Albums -->
    <div v-if="expanded" class="albums-section" @click.stop>
      <div v-if="loadingAlbums" class="albums-loading">
        <loading-spinner :spin="true" />
      </div>
      <div v-else-if="albums && albums.length > 0">
        <h4 class="albums-title">Albums — tap to preview</h4>
        <div class="albums-grid">
          <div
            v-for="album in albums"
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
          <template v-for="album in albums" :key="album.id">
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
</template>

<script>
import api from '@/api';
import LoadingSpinner from '@/components/LoadingSpinner.vue';
import { useToast } from 'vue-toastification';

export default {
  name: 'BandCard',
  components: { LoadingSpinner },
  props: {
    band: { type: Object, required: true },
    description: { type: String, default: '' },
  },
  setup() {
    return { toast: useToast() };
  },
  data() {
    return {
      expanded: false,
      albums: null,
      loadingAlbums: false,
      expandedAlbumId: null,
    };
  },
  methods: {
    async toggleBand() {
      if (this.expanded) {
        this.expanded = false;
        this.expandedAlbumId = null;
        return;
      }
      this.expanded = true;
      this.expandedAlbumId = null;
      if (this.albums === null) {
        this.loadingAlbums = true;
        try {
          const response = await api.get(`/bands/${encodeURIComponent(this.band.id)}/albums`);
          this.albums = response.data.items || [];
        } catch {
          this.albums = [];
          this.toast.error('Could not load albums.');
        } finally {
          this.loadingAlbums = false;
        }
      }
    },
    toggleAlbum(albumId) {
      this.expandedAlbumId = this.expandedAlbumId === albumId ? null : albumId;
    },
  },
};
</script>

<style scoped>
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

.band-reason {
  font-size: 0.78rem;
  color: #888;
  line-height: 1.45;
  margin: 0 0 0.4rem;
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

.no-albums { text-align: center; color: #444; padding: 2rem 0; font-size: 0.9rem; }
</style>
