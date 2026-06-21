<template>
  <section class="discover-section">

    <!-- Genre Playlists -->
    <div class="discover-block">
      <h2 class="discover-heading">
        <span class="heading-line"></span>
        Browse by Genre
        <span class="heading-line"></span>
      </h2>
      <div class="cards-scroll">
        <a
          v-for="genre in genres"
          :key="genre.label"
          :href="genre.url"
          target="_blank"
          rel="noopener noreferrer"
          class="genre-card"
          :style="{ '--accent': genre.color }"
        >
          <span class="genre-icon">{{ genre.icon }}</span>
          <span class="genre-label">{{ genre.label }}</span>
        </a>
      </div>
    </div>

    <!-- Labels -->
    <div class="discover-block">
      <h2 class="discover-heading">
        <span class="heading-line"></span>
        Extreme Metal Labels
        <span class="heading-line"></span>
      </h2>
      <div class="cards-scroll">
        <a
          v-for="label in labels"
          :key="label.name"
          :href="label.url"
          target="_blank"
          rel="noopener noreferrer"
          class="label-card"
        >
          <span class="label-name">{{ label.name }}</span>
          <span class="label-country">{{ label.country }}</span>
        </a>
      </div>
    </div>

  </section>
</template>

<script>
export default {
  name: 'DiscoverSection',
  data() {
    return {
      genres: [
        {
          label: 'Slamming Death Metal',
          icon: '💀',
          color: '#dc143c',
          url: 'https://open.spotify.com/search/slamming%20death%20metal/playlists',
        },
        {
          label: 'Sci-Fi Death Metal',
          icon: '🛸',
          color: '#7b2fbe',
          url: 'https://open.spotify.com/search/sci%20fi%20death%20metal/playlists',
        },
        {
          label: 'Cavernous Death Metal',
          icon: '🕳️',
          color: '#2a2a2a',
          url: 'https://open.spotify.com/search/cavernous%20death%20metal/playlists',
        },
        {
          label: 'Brutal Death Metal',
          icon: '🩸',
          color: '#8b0000',
          url: 'https://open.spotify.com/search/brutal%20death%20metal/playlists',
        },
        {
          label: 'Blackened Death Metal',
          icon: '⚫',
          color: '#1a1a2e',
          url: 'https://open.spotify.com/search/blackened%20death%20metal/playlists',
        },
        {
          label: 'Death Doom Metal',
          icon: '⚰️',
          color: '#3d1a1a',
          url: 'https://open.spotify.com/search/death%20doom%20metal/playlists',
        },
        {
          label: 'Death Thrash',
          icon: '⚡',
          color: '#b8860b',
          url: 'https://open.spotify.com/search/death%20thrash%20metal/playlists',
        },
        {
          label: 'Swedish Death Metal',
          icon: '🪓',
          color: '#006aa7',
          url: 'https://open.spotify.com/search/swedish%20death%20metal/playlists',
        },
        {
          label: 'Finnish Death Metal',
          icon: '🌲',
          color: '#003580',
          url: 'https://open.spotify.com/search/finnish%20death%20metal/playlists',
        },
        {
          label: 'Old School Death Metal',
          icon: '📼',
          color: '#5a3e00',
          url: 'https://open.spotify.com/search/old%20school%20death%20metal/playlists',
        },
        {
          label: 'Technical Death Metal',
          icon: '🎛️',
          color: '#1c4a1c',
          url: 'https://open.spotify.com/search/technical%20death%20metal/playlists',
        },
        {
          label: 'Death &amp; Roll',
          icon: '🎸',
          color: '#4a0e00',
          url: 'https://open.spotify.com/search/death%20and%20roll/playlists',
        },
      ],
      labels: [
        {
          name: 'Maggot Stomp',
          country: '🇺🇸 USA',
          url: 'https://maggot-stomp.bandcamp.com',
        },
        {
          name: 'Seasons of Mist',
          country: '🇫🇷 France',
          url: 'https://www.seasons-of-mist.com',
        },
        {
          name: 'Relapse Records',
          country: '🇺🇸 USA',
          url: 'https://www.relapserecords.com',
        },
        {
          name: 'Iron Bonehead',
          country: '🇩🇪 Germany',
          url: 'https://ironbonehead.de',
        },
        {
          name: '20 Buck Spin',
          country: '🇺🇸 USA',
          url: 'https://www.20buckspin.com',
        },
        {
          name: 'Prosthetic Records',
          country: '🇺🇸 USA',
          url: 'https://www.prostheticrecords.com',
        },
        {
          name: 'Unique Leader',
          country: '🇺🇸 USA',
          url: 'https://www.uniqueleaderrecords.com',
        },
        {
          name: 'Dark Descent',
          country: '🇺🇸 USA',
          url: 'https://darkdescentrecords.bandcamp.com',
        },
        {
          name: 'Blood Harvest',
          country: '🇸🇪 Sweden',
          url: 'https://bloodharvest.bandcamp.com',
        },
        {
          name: 'Profound Lore',
          country: '🇨🇦 Canada',
          url: 'https://www.profoundlorerecords.com',
        },
        {
          name: 'Nuclear Blast',
          country: '🇩🇪 Germany',
          url: 'https://www.nuclearblast.de',
        },
        {
          name: 'Metal Blade',
          country: '🇺🇸 USA',
          url: 'https://www.metalblade.com',
        },
        {
          name: 'Everlasting Spew',
          country: '🇩🇪 Germany',
          url: 'https://everlastingspew.bandcamp.com',
        },
        {
          name: 'Sentient Ruin',
          country: '🇺🇸 USA',
          url: 'https://sentientruin.bandcamp.com',
        },
        {
          name: 'Willowtip',
          country: '🇺🇸 USA',
          url: 'https://willowtip.bandcamp.com',
        },
        {
          name: 'Transcending Obscurity',
          country: '🇮🇳 India',
          url: 'https://www.transcendingobscurity.com',
        },
        {
          name: 'Testimony Records',
          country: '🇩🇪 Germany',
          url: 'https://testimonyrecords.bandcamp.com',
        },
        {
          name: 'Century Media',
          country: '🇩🇪 Germany',
          url: 'https://www.centurymedia.com',
        },
      ],
    };
  },
};
</script>

<style scoped>
.discover-section {
  display: flex;
  flex-direction: column;
  gap: 2.5rem;
  padding-bottom: 1rem;
}

.discover-block {
  width: 100%;
}

.discover-heading {
  display: flex;
  align-items: center;
  gap: 1rem;
  font-size: 0.85rem;
  font-weight: 800;
  text-transform: uppercase;
  letter-spacing: 0.15em;
  color: crimson;
  margin: 0 0 1.25rem;
}

.heading-line {
  flex: 1;
  height: 1px;
  background: linear-gradient(to right, transparent, #2a2a2a);
}

.discover-heading .heading-line:first-child {
  background: linear-gradient(to left, transparent, #2a2a2a);
}

/* Horizontal scroll row */
.cards-scroll {
  display: flex;
  gap: 0.75rem;
  overflow-x: auto;
  padding-bottom: 0.75rem;
  scrollbar-width: thin;
  scrollbar-color: #2a2a2a transparent;
}

.cards-scroll::-webkit-scrollbar {
  height: 4px;
}
.cards-scroll::-webkit-scrollbar-track {
  background: transparent;
}
.cards-scroll::-webkit-scrollbar-thumb {
  background: #2a2a2a;
  border-radius: 2px;
}

/* Genre cards */
.genre-card {
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  padding: 1.1rem 1rem;
  min-width: 130px;
  border-radius: 10px;
  background: #111;
  text-decoration: none;
  transition: transform 0.15s, box-shadow 0.2s;
  border-bottom: 3px solid var(--accent, crimson);
  position: relative;
  overflow: hidden;
}

.genre-card::before {
  content: '';
  position: absolute;
  inset: 0;
  background: radial-gradient(circle at 50% 120%, var(--accent, crimson) 0%, transparent 70%);
  opacity: 0.08;
  transition: opacity 0.2s;
}

.genre-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.5);
}

.genre-card:hover::before {
  opacity: 0.18;
}

.genre-icon {
  font-size: 1.8rem;
  line-height: 1;
}

.genre-label {
  font-size: 0.75rem;
  font-weight: 700;
  color: #ddd;
  text-align: center;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  line-height: 1.3;
}

/* Label cards */
.label-card {
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 0.3rem;
  padding: 0.9rem 1.2rem;
  min-width: 130px;
  border-radius: 10px;
  background: #111;
  text-decoration: none;
  transition: transform 0.15s, background 0.2s, box-shadow 0.2s;
  border-left: 3px solid #2a2a2a;
}

.label-card:hover {
  transform: translateY(-3px);
  background: #161616;
  border-left-color: crimson;
  box-shadow: 0 4px 20px rgba(220, 20, 60, 0.12);
}

.label-name {
  font-size: 0.82rem;
  font-weight: 800;
  color: #fff;
  text-align: center;
  text-transform: uppercase;
  letter-spacing: 0.06em;
}

.label-country {
  font-size: 0.7rem;
  color: #666;
  text-align: center;
}
</style>
