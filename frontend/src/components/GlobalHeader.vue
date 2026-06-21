<template>
  <header>
    <h1>
      <img
        src="/img/deathmetalmanialogo.png"
        alt="Death Metal Mania Logo"
        class="logo"
        @click="handleLogoClick"
        :class="{ 'logo-buzz': buzzing }"
        title="Death Metal Mania"
      />
    </h1>
    <div v-if="clickCount > 0 && clickCount < 13" class="click-counter" :style="{ opacity: clickCount > 8 ? 1 : 0.5 }">
      {{ 13 - clickCount }}
    </div>
  </header>
</template>

<script>
import { useToast } from 'vue-toastification';

export default {
  name: 'GlobalHeader',
  setup() {
    return { toast: useToast() };
  },
  data() {
    return {
      clickCount: 0,
      clickTimer: null,
      buzzing: false,
    };
  },
  methods: {
    handleLogoClick() {
      clearTimeout(this.clickTimer);
      this.clickCount++;

      if (this.clickCount >= 13) {
        this.clickCount = 0;
        this.buzzing = true;
        setTimeout(() => { this.buzzing = false; }, 600);
        this.toast.info('☠ You found the secret lair... enter if you dare ☠', {
          timeout: 6000,
          onClick: () => this.$router.push('/secret-lair'),
        });
        this.$router.push('/secret-lair');
      } else {
        this.clickTimer = setTimeout(() => {
          this.clickCount = 0;
        }, 3000);
      }
    },
  },
};
</script>

<style scoped>
header {
  background-color: #0a0a0a;
  text-align: center;
  padding: 1rem 0;
  position: relative;
}

.logo {
  max-width: 500px;
  width: 100%;
  height: auto;
  display: block;
  margin: 0 auto;
  cursor: pointer;
  user-select: none;
}

@keyframes buzz {
  0%   { transform: rotate(-2deg) scale(1.01); }
  25%  { transform: rotate(2deg) scale(1.02); }
  50%  { transform: rotate(-2deg) scale(1.01); }
  75%  { transform: rotate(2deg) scale(1.02); }
  100% { transform: rotate(0) scale(1); }
}

.logo-buzz { animation: buzz 0.5s ease-in-out; }

.click-counter {
  position: absolute;
  bottom: 0.4rem;
  right: 1rem;
  font-size: 0.65rem;
  font-weight: 700;
  color: crimson;
  letter-spacing: 0.06em;
  text-transform: uppercase;
  pointer-events: none;
  transition: opacity 0.2s;
}
</style>
