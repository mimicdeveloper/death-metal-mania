<template>
  <section class="discover-section">

    <!-- Subgenre Playlists -->
    <div class="discover-block">
      <h2 class="discover-heading">
        <span class="heading-line"></span>
        Browse by Subgenre
        <span class="heading-line"></span>
      </h2>
      <div class="cards-scroll">
        <a
          v-for="sub in subgenres"
          :key="sub.label"
          :href="sub.url"
          target="_blank"
          rel="noopener noreferrer"
          class="genre-card"
          :style="{ '--accent': sub.color }"
        >
          <span class="genre-icon">{{ sub.icon }}</span>
          <span class="genre-label">{{ sub.label }}</span>
        </a>
      </div>
    </div>

    <!-- Podcasts -->
    <div class="discover-block">
      <h2 class="discover-heading">
        <span class="heading-line"></span>
        Death Metal Podcasts
        <span class="heading-line"></span>
      </h2>
      <div class="cards-scroll">
        <a
          v-for="pod in podcasts"
          :key="pod.name"
          :href="pod.url"
          target="_blank"
          rel="noopener noreferrer"
          class="podcast-card"
        >
          <span class="podcast-icon">🎙️</span>
          <span class="podcast-name">{{ pod.name }}</span>
          <span class="podcast-desc">{{ pod.desc }}</span>
        </a>
      </div>
    </div>

    <!-- Books coming soon -->
    <div class="discover-block books-block">
      <h2 class="discover-heading">
        <span class="heading-line"></span>
        Books for the Metalhead
        <span class="heading-line"></span>
      </h2>
      <div class="books-coming-soon">
        <span class="books-icon">📖</span>
        <p class="books-text">The Metalhead's Reading List</p>
        <span class="books-badge">Coming Soon</span>
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
      subgenres: [
        { label: 'Death Metal',             icon: '💀', color: '#dc143c', url: 'https://open.spotify.com/search/death%20metal/playlists' },
        { label: 'Slam Death Metal',        icon: '🩸', color: '#8b0000', url: 'https://open.spotify.com/search/slam%20death%20metal/playlists' },
        { label: 'Brutal Death Metal',      icon: '🔪', color: '#6b0000', url: 'https://open.spotify.com/search/brutal%20death%20metal/playlists' },
        { label: 'Technical Death Metal',   icon: '🎛️', color: '#1c4a1c', url: 'https://open.spotify.com/search/technical%20death%20metal/playlists' },
        { label: 'Melodic Death Metal',     icon: '🎸', color: '#2a0050', url: 'https://open.spotify.com/search/melodic%20death%20metal/playlists' },
        { label: 'Blackened Death Metal',   icon: '⚫', color: '#1a1a2e', url: 'https://open.spotify.com/search/blackened%20death%20metal/playlists' },
        { label: 'Old School Death Metal',  icon: '📼', color: '#5a3e00', url: 'https://open.spotify.com/search/old%20school%20death%20metal/playlists' },
        { label: 'Swedish Death Metal',     icon: '🪓', color: '#006aa7', url: 'https://open.spotify.com/search/swedish%20death%20metal/playlists' },
        { label: 'Finnish Death Metal',     icon: '🌲', color: '#003580', url: 'https://open.spotify.com/search/finnish%20death%20metal/playlists' },
        { label: 'Cavernous Death Metal',   icon: '🕳️', color: '#2a2a2a', url: 'https://open.spotify.com/search/cavernous%20death%20metal/playlists' },
        { label: 'Death-Doom Metal',        icon: '⚰️', color: '#1a0a2a', url: 'https://open.spotify.com/search/death%20doom%20metal/playlists' },
        { label: 'Hardcore Death Metal',    icon: '🔨', color: '#7a0000', url: 'https://open.spotify.com/search/hardcore%20death%20metal/playlists' },
        { label: 'Death & Roll',            icon: '🎸', color: '#4a0e00', url: 'https://open.spotify.com/search/death%20and%20roll/playlists' },
        { label: 'Death Thrash',            icon: '⚡', color: '#b8860b', url: 'https://open.spotify.com/search/death%20thrash%20metal/playlists' },
        { label: 'Progressive Death Metal', icon: '🌀', color: '#0a3a4a', url: 'https://open.spotify.com/search/progressive%20death%20metal/playlists' },
        { label: 'Sci-Fi Death Metal',      icon: '🛸', color: '#7b2fbe', url: 'https://open.spotify.com/search/sci%20fi%20death%20metal/playlists' },
        { label: 'Floridian Death Metal',   icon: '🐊', color: '#1a4a1a', url: 'https://open.spotify.com/search/florida%20death%20metal/playlists' },
        { label: 'New York Death Metal',    icon: '🗽', color: '#2a2a50', url: 'https://open.spotify.com/search/new%20york%20death%20metal/playlists' },
        { label: 'War Metal',               icon: '💣', color: '#2a1a00', url: 'https://open.spotify.com/search/war%20metal/playlists' },
        { label: 'Atmospheric Death Metal', icon: '🌫️', color: '#0a1a2a', url: 'https://open.spotify.com/search/atmospheric%20death%20metal/playlists' },
        { label: 'Industrial Death Metal',  icon: '⚙️', color: '#1a1a1a', url: 'https://open.spotify.com/search/industrial%20death%20metal/playlists' },
        { label: 'Goregrind',               icon: '🧠', color: '#3a0000', url: 'https://open.spotify.com/search/goregrind/playlists' },
        { label: 'Grindcore',               icon: '💥', color: '#2a0a00', url: 'https://open.spotify.com/search/grindcore/playlists' },
        { label: 'Cosmic Death Metal',      icon: '🌌', color: '#0a0a3a', url: 'https://open.spotify.com/search/cosmic%20death%20metal/playlists' },
        { label: 'Dissonant Death Metal',   icon: '🔊', color: '#1a1500', url: 'https://open.spotify.com/search/dissonant%20death%20metal/playlists' },
        { label: 'Crust Punk',              icon: '🏴', color: '#0a0a0a', url: 'https://open.spotify.com/search/crust%20punk/playlists' },
      ],
      podcasts: [
        { name: 'Heavy Hole',              desc: 'Deep cuts in death metal',           url: 'https://open.spotify.com/search/Heavy%20Hole%20podcast' },
        { name: 'The Garza Podcast',       desc: 'Metal talk with Marcos Garza',       url: 'https://open.spotify.com/search/Garza%20podcast%20metal' },
        { name: 'Metal Injection Livecast',desc: 'News, interviews & rants',           url: 'https://open.spotify.com/search/Metal%20Injection%20Livecast' },
        { name: 'Banger TV Podcast',       desc: 'Documentary-level metal deep dives', url: 'https://open.spotify.com/search/Banger%20TV%20podcast' },
        { name: 'No Clean Singing',        desc: 'Extreme music coverage',             url: 'https://www.nocleansinging.com' },
        { name: 'The End of Life Podcast', desc: 'Death metal philosophy & culture',   url: 'https://open.spotify.com/search/end%20of%20life%20death%20metal%20podcast' },
        { name: 'Extremely Bad Advice',    desc: 'Humor meets extreme metal',          url: 'https://open.spotify.com/search/extremely%20bad%20advice%20metal%20podcast' },
        { name: 'Obituarist',              desc: 'Obituary deep dives & death metal lore', url: 'https://open.spotify.com/search/obituarist%20death%20metal%20podcast' },
      ],
      labels: [
        { name: 'Maggot Stomp',          country: '🇺🇸 USA',    url: 'https://maggot-stomp.bandcamp.com' },
        { name: 'Seasons of Mist',       country: '🇫🇷 France', url: 'https://www.seasons-of-mist.com' },
        { name: 'Relapse Records',       country: '🇺🇸 USA',    url: 'https://www.relapserecords.com' },
        { name: 'Iron Bonehead',         country: '🇩🇪 Germany',url: 'https://ironbonehead.de' },
        { name: '20 Buck Spin',          country: '🇺🇸 USA',    url: 'https://www.20buckspin.com' },
        { name: 'Prosthetic Records',    country: '🇺🇸 USA',    url: 'https://www.prostheticrecords.com' },
        { name: 'Unique Leader',         country: '🇺🇸 USA',    url: 'https://www.uniqueleaderrecords.com' },
        { name: 'Dark Descent',          country: '🇺🇸 USA',    url: 'https://darkdescentrecords.bandcamp.com' },
        { name: 'Blood Harvest',         country: '🇸🇪 Sweden', url: 'https://bloodharvest.bandcamp.com' },
        { name: 'Profound Lore',         country: '🇨🇦 Canada', url: 'https://www.profoundlorerecords.com' },
        { name: 'Nuclear Blast',         country: '🇩🇪 Germany',url: 'https://www.nuclearblast.de' },
        { name: 'Metal Blade',           country: '🇺🇸 USA',    url: 'https://www.metalblade.com' },
        { name: 'Everlasting Spew',      country: '🇩🇪 Germany',url: 'https://everlastingspew.bandcamp.com' },
        { name: 'Sentient Ruin',         country: '🇺🇸 USA',    url: 'https://sentientruin.bandcamp.com' },
        { name: 'Willowtip',             country: '🇺🇸 USA',    url: 'https://willowtip.bandcamp.com' },
        { name: 'Transcending Obscurity',country: '🇮🇳 India',  url: 'https://www.transcendingobscurity.com' },
        { name: 'Testimony Records',     country: '🇩🇪 Germany',url: 'https://testimonyrecords.bandcamp.com' },
        { name: 'Century Media',         country: '🇩🇪 Germany',url: 'https://www.centurymedia.com' },
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

.discover-block { width: 100%; }

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

.heading-line { flex: 1; height: 1px; background: linear-gradient(to right, transparent, #2a2a2a); }
.discover-heading .heading-line:first-child { background: linear-gradient(to left, transparent, #2a2a2a); }

.cards-scroll {
  display: flex;
  gap: 0.75rem;
  overflow-x: auto;
  padding-bottom: 0.75rem;
  scrollbar-width: thin;
  scrollbar-color: #2a2a2a transparent;
}

.cards-scroll::-webkit-scrollbar { height: 4px; }
.cards-scroll::-webkit-scrollbar-track { background: transparent; }
.cards-scroll::-webkit-scrollbar-thumb { background: #2a2a2a; border-radius: 2px; }

.genre-card {
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  padding: 1.1rem 1rem;
  min-width: 140px;
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

.genre-card:hover { transform: translateY(-3px); box-shadow: 0 6px 24px rgba(0,0,0,0.5); }
.genre-card:hover::before { opacity: 0.18; }

.genre-icon { font-size: 1.8rem; line-height: 1; }

.genre-label {
  font-size: 0.72rem;
  font-weight: 700;
  color: #ddd;
  text-align: center;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  line-height: 1.3;
}

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

.label-name { font-size: 0.82rem; font-weight: 800; color: #fff; text-align: center; text-transform: uppercase; letter-spacing: 0.06em; }
.label-country { font-size: 0.7rem; color: #666; text-align: center; }

.podcast-card {
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.35rem;
  padding: 1rem 1rem 0.9rem;
  min-width: 148px;
  max-width: 148px;
  border-radius: 10px;
  background: #111;
  text-decoration: none;
  transition: transform 0.15s, background 0.2s, box-shadow 0.2s;
  border-top: 3px solid #2a2a2a;
}

.podcast-card:hover {
  transform: translateY(-3px);
  background: #161616;
  border-top-color: crimson;
  box-shadow: 0 4px 20px rgba(220, 20, 60, 0.12);
}

.podcast-icon { font-size: 1.6rem; line-height: 1; }
.podcast-name { font-size: 0.78rem; font-weight: 800; color: #fff; text-align: center; text-transform: uppercase; letter-spacing: 0.05em; }
.podcast-desc { font-size: 0.68rem; color: #666; text-align: center; line-height: 1.3; }

.books-block { opacity: 0.85; }

.books-coming-soon {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1.2rem 1.5rem;
  background: #111;
  border-radius: 10px;
  border: 1px dashed #2a2a2a;
}

.books-icon { font-size: 2rem; flex-shrink: 0; }
.books-text { font-size: 1rem; font-weight: 700; color: #555; margin: 0; flex: 1; text-transform: uppercase; letter-spacing: 0.05em; }

.books-badge {
  font-size: 0.7rem;
  font-weight: 800;
  letter-spacing: 0.1em;
  text-transform: uppercase;
  color: #333;
  background: #1a1a1a;
  border: 1px solid #2a2a2a;
  padding: 0.3rem 0.7rem;
  border-radius: 20px;
  white-space: nowrap;
}
</style>
