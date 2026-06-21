<template>
  <div class="about-wrap">

    <div class="hero">
      <div class="skulls">☠ ☠ ☠</div>
      <h1 class="alias">pseudocorpse</h1>
      <p class="tagline">true death metal slammer</p>
    </div>

    <div class="divider"></div>

    <div class="bio-grid">
      <div class="bio-card">
        <span class="bio-icon">💀</span>
        <h3>The Creed</h3>
        <p>Live for the ping.<br>Live for the guts.<br>It is always time to shred.</p>
      </div>
      <div class="bio-card">
        <span class="bio-icon">🌑</span>
        <h3>By Day</h3>
        <p>Builds things in the dark. Architects chaos into structure. Lurks where machines obey commands.</p>
      </div>
      <div class="bio-card">
        <span class="bio-icon">🩸</span>
        <h3>By Night</h3>
        <p>Causing the slamming. Ready to shred. Answering the call of the blastbeat at any hour.</p>
      </div>
    </div>

    <div class="divider"></div>

    <div class="manifesto">
      <h2 class="manifesto-title">The Slam Manifesto</h2>
      <ul class="manifesto-list">
        <li>The ping is not a sound — it is a feeling in the chest.</li>
        <li>Guts over glory, every single time.</li>
        <li>There is no wrong time to shred. There is only now, and later, which is also now.</li>
        <li>Death metal is not a genre — it is a way of life chosen by the select few.</li>
        <li>The blastbeat does not end. It continues. Somewhere, always, someone is blasting.</li>
      </ul>
    </div>

    <div class="divider"></div>

    <div class="gear-section">
      <h2 class="gear-title">Current Rotation</h2>
      <div class="genre-tags-row">
        <span class="gtag">Slam Death Metal</span>
        <span class="gtag">Brutal Death Metal</span>
        <span class="gtag">Goregrind</span>
        <span class="gtag">Cavernous Death Metal</span>
        <span class="gtag">Old School Death Metal</span>
        <span class="gtag">Crust Punk</span>
        <span class="gtag">Grindcore</span>
        <span class="gtag">Blackened Death Metal</span>
      </div>
    </div>

    <div class="divider"></div>

    <div class="cta-row">
      <router-link to="/donate" class="cta-btn">Keep the Slam Alive ☠</router-link>
      <router-link to="/oracle" class="cta-btn secondary">Consult the Oracle</router-link>
    </div>

  </div>
</template>

<script>
export default { name: 'AboutView' };
</script>

<style scoped>
.about-wrap {
  max-width: 900px;
  margin: 0 auto;
  padding: 3rem 1.5rem 5rem;
}

.hero {
  text-align: center;
  padding: 2rem 0 2.5rem;
}

.skulls {
  font-size: 1.2rem;
  letter-spacing: 0.6em;
  color: #222;
  margin-bottom: 1rem;
  animation: fade-skulls 4s ease-in-out infinite;
}

@keyframes fade-skulls {
  0%, 100% { color: #222; }
  50%       { color: #3a0008; }
}

.alias {
  font-size: 4rem;
  font-weight: 900;
  text-transform: uppercase;
  letter-spacing: 0.1em;
  color: crimson;
  margin: 0 0 0.5rem;
  text-shadow: 0 0 40px rgba(220,20,60,0.35), 0 0 80px rgba(220,20,60,0.1);
  line-height: 1;
}

.tagline {
  font-size: 1rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.25em;
  color: #444;
  margin: 0;
}

.divider {
  height: 1px;
  background: linear-gradient(to right, transparent, #1a1a1a, transparent);
  margin: 2.5rem 0;
}

.bio-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 1rem;
}

.bio-card {
  background: #111;
  border-radius: 12px;
  padding: 1.75rem 1.5rem;
  text-align: center;
  border-bottom: 3px solid #1a1a1a;
  transition: border-color 0.2s;
}

.bio-card:hover { border-color: crimson; }

.bio-icon { font-size: 2.2rem; display: block; margin-bottom: 0.85rem; }

.bio-card h3 {
  font-size: 0.78rem;
  font-weight: 800;
  text-transform: uppercase;
  letter-spacing: 0.15em;
  color: crimson;
  margin: 0 0 0.75rem;
}

.bio-card p {
  font-size: 0.92rem;
  line-height: 1.7;
  color: #888;
  margin: 0;
}

.manifesto-title, .gear-title {
  text-align: center;
  font-size: 0.85rem;
  font-weight: 800;
  text-transform: uppercase;
  letter-spacing: 0.15em;
  color: crimson;
  margin: 0 0 1.5rem;
}

.manifesto-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 0.85rem;
}

.manifesto-list li {
  position: relative;
  padding-left: 1.75rem;
  font-size: 0.95rem;
  line-height: 1.6;
  color: #666;
}

.manifesto-list li::before {
  content: '☠';
  position: absolute;
  left: 0;
  color: #2a0008;
  font-size: 0.85rem;
}

.genre-tags-row {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  justify-content: center;
}

.gtag {
  padding: 0.35rem 0.9rem;
  background: #111;
  border: 1px solid #1e1e1e;
  border-radius: 20px;
  font-size: 0.75rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.04em;
  color: #555;
  transition: color 0.15s, border-color 0.15s;
}

.gtag:hover { color: #ccc; border-color: #333; }

.cta-row {
  display: flex;
  gap: 1rem;
  justify-content: center;
  flex-wrap: wrap;
}

.cta-btn {
  padding: 0.75rem 2rem;
  background: crimson;
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 0.9rem;
  font-weight: 800;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  cursor: pointer;
  text-decoration: none;
  transition: background 0.15s;
}

.cta-btn:hover { background: #a30000; }

.cta-btn.secondary {
  background: transparent;
  color: crimson;
  border: 1px solid crimson;
}

.cta-btn.secondary:hover { background: crimson; color: #fff; }

@media (max-width: 600px) {
  .alias { font-size: 2.5rem; }
  .bio-grid { grid-template-columns: 1fr; }
}
</style>
