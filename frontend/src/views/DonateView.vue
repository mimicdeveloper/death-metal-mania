<template>
  <div class="donate-wrap">

    <div class="donate-hero">
      <h1 class="donate-title">Keep the Slam Alive</h1>
      <p class="donate-sub">This site runs on riffs and coffee. If it's given you a new band to devour, consider throwing a few bucks at the cause.</p>
    </div>

    <div class="methods-grid">

      <a href="https://www.paypal.me/pseudocorpse" target="_blank" rel="noopener" class="method-card">
        <div class="method-icon paypal-icon">
          <svg viewBox="0 0 24 24" fill="currentColor" width="32" height="32"><path d="M7.076 21.337H2.47a.641.641 0 01-.633-.74L4.944.901C5.026.382 5.474 0 5.998 0h7.46c2.57 0 4.578.543 5.69 1.81 1.01 1.15 1.304 2.42 1.012 4.287-.023.143-.047.288-.077.437-.983 5.05-4.349 6.797-8.647 6.797h-2.19c-.524 0-.968.382-1.05.9l-1.12 7.106zm14.146-14.42a3.35 3.35 0 00-.607-.541c-.013.076-.026.175-.041.254-.59 3.025-2.566 4.643-5.813 4.643h-2.19c-.086 0-.167.013-.242.036l-.875 5.555h1.887l.518-3.294c.082-.518.526-.9 1.05-.9h.657c3.298 0 5.88-1.341 6.64-5.21.28-1.404.02-2.45-.984-3.543z"/></svg>
        </div>
        <div class="method-info">
          <h3 class="method-name">PayPal</h3>
          <p class="method-handle">paypal.me/pseudocorpse</p>
          <p class="method-desc">Fast, easy, no account needed for guests</p>
        </div>
        <span class="method-arrow">→</span>
      </a>

      <a href="https://cash.app/$pseudocorpse" target="_blank" rel="noopener" class="method-card">
        <div class="method-icon cashapp-icon">
          <svg viewBox="0 0 24 24" fill="currentColor" width="32" height="32"><path d="M23.59 3.476A12.01 12.01 0 000 12a12.01 12.01 0 0023.59 8.524l-.002-.006A12.016 12.016 0 0023.59 3.476zm-6.963 8.602l-.84.236c.162.558.204 1.14.123 1.719a4.36 4.36 0 01-1.39 2.672 4.428 4.428 0 01-2.78 1.04v1.09a.69.69 0 01-.69.69h-.81a.69.69 0 01-.69-.69v-1.113a6.073 6.073 0 01-2.68-1.3.69.69 0 01-.04-.996l.571-.571a.69.69 0 01.928-.038 4.005 4.005 0 002.22.793c1.147 0 1.97-.542 1.97-1.32 0-.63-.42-1.02-1.7-1.38l-.983-.274C8.74 12.28 7.7 11.27 7.7 9.73c0-1.886 1.478-3.197 3.65-3.459V5.164a.69.69 0 01.69-.69h.81a.69.69 0 01.69.69v1.126c.81.117 1.565.43 2.212.904a.69.69 0 01.11.997l-.532.595a.69.69 0 01-.935.083 3.526 3.526 0 00-1.986-.614c-1.053 0-1.744.491-1.744 1.2 0 .588.392.96 1.572 1.3l.877.245c2.046.57 3.1 1.545 3.1 3.177 0 .124-.009.247-.025.37l.847-.237a.685.685 0 01.852.474l.22.786a.686.686 0 01-.479.852z"/></svg>
        </div>
        <div class="method-info">
          <h3 class="method-name">Cash App</h3>
          <p class="method-handle">$pseudocorpse</p>
          <p class="method-desc">Instant transfer, US only</p>
        </div>
        <span class="method-arrow">→</span>
      </a>

      <a href="https://ko-fi.com/pseudocorpse" target="_blank" rel="noopener" class="method-card">
        <div class="method-icon kofi-icon">☕</div>
        <div class="method-info">
          <h3 class="method-name">Ko-fi</h3>
          <p class="method-handle">ko-fi.com/pseudocorpse</p>
          <p class="method-desc">Buy a coffee — 0% platform fees</p>
        </div>
        <span class="method-arrow">→</span>
      </a>

      <a href="https://buymeacoffee.com/pseudocorpse" target="_blank" rel="noopener" class="method-card">
        <div class="method-icon bmac-icon">☕</div>
        <div class="method-info">
          <h3 class="method-name">Buy Me a Coffee</h3>
          <p class="method-handle">buymeacoffee.com/pseudocorpse</p>
          <p class="method-desc">One-click support</p>
        </div>
        <span class="method-arrow">→</span>
      </a>

    </div>

    <div class="thank-you">
      <span class="ty-skull">☠</span>
      <p class="ty-text">Every donation keeps the servers pinging and the slam alive. You are part of the horde now.</p>
    </div>

  </div>
</template>

<script>
export default { name: 'DonateView' };
</script>

<style scoped>
.donate-wrap {
  max-width: 700px;
  margin: 0 auto;
  padding: 3rem 1.5rem 5rem;
}

.donate-hero {
  text-align: center;
  margin-bottom: 2.5rem;
}

.donate-title {
  font-size: 2.5rem;
  font-weight: 900;
  text-transform: uppercase;
  letter-spacing: 0.1em;
  color: crimson;
  margin: 0 0 0.75rem;
}

.donate-sub {
  font-size: 0.95rem;
  color: #555;
  line-height: 1.7;
  max-width: 500px;
  margin: 0 auto;
}

.methods-grid {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  margin-bottom: 2.5rem;
}

.method-card {
  display: flex;
  align-items: center;
  gap: 1.25rem;
  padding: 1.25rem 1.5rem;
  background: #111;
  border-radius: 12px;
  border: 1px solid #1a1a1a;
  text-decoration: none;
  transition: border-color 0.15s, background 0.15s, transform 0.12s;
}

.method-card:hover {
  border-color: crimson;
  background: #141414;
  transform: translateX(4px);
}

.method-icon {
  width: 52px;
  height: 52px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  font-size: 1.8rem;
}

.paypal-icon  { background: #003087; color: #fff; }
.cashapp-icon { background: #00d64f; color: #fff; }
.kofi-icon    { background: #ff5e5b; color: #fff; }
.bmac-icon    { background: #ffdd00; color: #1a1a1a; }

.method-info { flex: 1; min-width: 0; }

.method-name {
  font-size: 1rem;
  font-weight: 800;
  color: #fff;
  margin: 0 0 0.2rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.method-handle {
  font-size: 0.82rem;
  color: crimson;
  margin: 0 0 0.2rem;
  font-family: monospace;
}

.method-desc {
  font-size: 0.75rem;
  color: #444;
  margin: 0;
}

.method-arrow {
  color: #333;
  font-size: 1.2rem;
  flex-shrink: 0;
  transition: color 0.15s, transform 0.15s;
}

.method-card:hover .method-arrow { color: crimson; transform: translateX(4px); }

.thank-you {
  text-align: center;
  padding: 2rem;
  background: #0d0d0d;
  border-radius: 12px;
  border: 1px dashed #1a1a1a;
}

.ty-skull { font-size: 2rem; display: block; margin-bottom: 0.75rem; }

.ty-text {
  font-size: 0.9rem;
  color: #444;
  line-height: 1.7;
  margin: 0;
  max-width: 420px;
  margin-inline: auto;
}

@media (max-width: 500px) {
  .donate-title { font-size: 1.8rem; }
  .method-card { flex-wrap: wrap; }
}
</style>
