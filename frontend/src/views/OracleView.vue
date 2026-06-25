<template>
  <div class="oracle-wrap">

    <div class="oracle-header">
      <div class="oracle-eye">👁</div>
      <h1 class="oracle-title">Death Metal Oracle</h1>
      <p class="oracle-sub">Powered by Groq + Llama 3 · Free · Brutal · Knowledgeable</p>
    </div>

    <!-- Mode tabs -->
    <div class="mode-tabs">
      <button class="mode-tab" :class="{ active: mode === 'oracle' }" @click="mode = 'oracle'">☠ Oracle Chat</button>
      <button class="mode-tab" :class="{ active: mode === 'recommend' }" @click="mode = 'recommend'">🔮 Band Recommender</button>
    </div>

    <!-- ORACLE CHAT -->
    <div v-if="mode === 'oracle'" class="chat-section">
      <div class="chat-window" ref="chatWindow">
        <div class="chat-intro" v-if="chatMessages.length === 0">
          <p>Ask anything about extreme music. Bands, albums, scenes, subgenres, history — the Oracle knows.</p>
          <div class="starter-questions">
            <button v-for="q in starterQuestions" :key="q" class="starter-btn" @click="sendStarter(q)">{{ q }}</button>
          </div>
        </div>

        <div
          v-for="(msg, i) in chatMessages"
          :key="i"
          class="chat-bubble"
          :class="msg.role"
        >
          <span v-if="msg.role === 'assistant'" class="bubble-avatar">☠</span>
          <div class="bubble-content" v-html="formatMessage(msg.content)"></div>
          <span v-if="msg.role === 'user'" class="bubble-avatar user-avatar">👤</span>
        </div>

        <div v-if="isThinking" class="chat-bubble assistant thinking">
          <span class="bubble-avatar">☠</span>
          <div class="bubble-content"><span class="dots"><span>.</span><span>.</span><span>.</span></span></div>
        </div>
      </div>

      <div class="chat-input-row">
        <input
          type="text"
          v-model="chatInput"
          placeholder="Ask the Oracle..."
          @keyup.enter="sendChat"
          :disabled="isThinking"
          class="chat-input"
          ref="chatInput"
        />
        <button class="send-btn" @click="sendChat" :disabled="isThinking || !chatInput.trim()">
          {{ isThinking ? '...' : 'Send' }}
        </button>
        <button class="clear-btn" @click="clearChat" title="Clear chat">✕</button>
      </div>
    </div>

    <!-- BAND RECOMMENDER -->
    <div v-if="mode === 'recommend'" class="recommend-section">
      <p class="recommend-intro">Describe the sound, mood, or vibe you're after. The Oracle will summon 5 bands.</p>

      <div class="recommend-examples">
        <span class="ex-label">Examples:</span>
        <button
          v-for="ex in examples"
          :key="ex"
          class="ex-btn"
          @click="recommendInput = ex"
        >{{ ex }}</button>
      </div>

      <textarea
        v-model="recommendInput"
        placeholder="E.g. — brutal slam with gory imagery, super low tuned, bands like Waking the Cadaver..."
        class="recommend-textarea"
        rows="4"
        :disabled="isThinking"
      ></textarea>

      <button class="send-btn full" @click="sendRecommend" :disabled="isThinking || !recommendInput.trim()">
        {{ isThinking ? 'Consulting the void...' : '🔮 Summon Bands' }}
      </button>

      <div v-if="recommendResult" class="recommend-result">
        <div v-html="formatRecommendations(recommendResult)" class="reco-content"></div>

        <div v-if="loadingBands" class="bands-loading">
          <loading-spinner :spin="true" />
          <span>Summoning the bands from the void...</span>
        </div>

        <div v-if="recommendedBands.length > 0" class="reco-bands">
          <h4 class="reco-bands-title">▶ Listen — tap a band for albums &amp; song previews</h4>
          <div class="reco-band-grid">
            <band-card
              v-for="entry in recommendedBands"
              :key="entry.band.id"
              :band="entry.band"
              :description="entry.description"
            />
          </div>
        </div>
        <p v-else-if="!loadingBands && bandsFetched" class="reco-bands-none">
          Couldn't find these on Spotify to preview — the recommendations above still stand.
        </p>

        <button class="clear-btn-sm" @click="resetRecommend">New search</button>
      </div>
    </div>

  </div>
</template>

<script>
import api from '@/api';
import { useToast } from 'vue-toastification';
import BandCard from '@/components/BandCard.vue';
import LoadingSpinner from '@/components/LoadingSpinner.vue';

export default {
  name: 'OracleView',
  components: { BandCard, LoadingSpinner },
  setup() {
    return { toast: useToast() };
  },
  data() {
    return {
      mode: 'oracle',
      chatMessages: [],
      chatInput: '',
      isThinking: false,
      recommendInput: '',
      recommendResult: '',
      recommendedBands: [],
      loadingBands: false,
      bandsFetched: false,
      starterQuestions: [
        'What are the most brutal slam albums of all time?',
        'Explain the difference between goregrind and death metal',
        'What happened to the Florida death metal scene?',
        'Best cavernous death metal bands?',
        'Give me a history of Swedish death metal',
      ],
      examples: [
        'Old school death metal, filthy production, Swedish chainsaw tone',
        'Brutal slam with gory artwork, super low tuned and heavy',
        'Cavernous, oppressive atmosphere, sounds like being buried alive',
        'Technical death metal but still brutal, not too clinical',
      ],
    };
  },
  methods: {
    async sendChat() {
      const message = this.chatInput.trim();
      if (!message || this.isThinking) return;

      this.chatMessages.push({ role: 'user', content: message });
      this.chatInput = '';
      this.isThinking = true;
      this.$nextTick(() => this.scrollChat());

      const history = this.chatMessages
        .slice(0, -1)
        .map(m => ({ role: m.role, content: m.content }));

      try {
        const res = await api.post('/ai/oracle', { message, history });
        this.chatMessages.push({ role: 'assistant', content: res.data.response });
      } catch {
        this.chatMessages.push({ role: 'assistant', content: 'The Oracle is silent. The void is unresponsive. Try again.' });
        this.toast.error('Oracle unreachable — is the backend running?');
      } finally {
        this.isThinking = false;
        this.$nextTick(() => this.scrollChat());
      }
    },

    async sendStarter(q) {
      this.chatInput = q;
      await this.sendChat();
    },

    async sendRecommend() {
      const description = this.recommendInput.trim();
      if (!description || this.isThinking) return;

      this.isThinking = true;
      this.recommendResult = '';
      this.recommendedBands = [];
      this.bandsFetched = false;

      try {
        const res = await api.post('/ai/recommend', { description });
        this.recommendResult = res.data.response;
        if (res.data.response?.includes('GROQ_API_KEY')) {
          this.toast.warning('Groq API key not set on Koyeb — see setup instructions');
          return;
        }
        await this.fetchRecommendedBands(res.data.response);
      } catch (err) {
        const msg = err?.response?.data?.message || '';
        this.recommendResult = msg || 'Backend unreachable. Check that Koyeb is running.';
        this.toast.error('Recommender failed — backend error');
      } finally {
        this.isThinking = false;
      }
    },

    parseRecommendations(text) {
      const out = [];
      for (const line of text.split('\n')) {
        const match = line.match(/^\s*\d*\.?\s*\*\*(.+?)\*\*(.*)$/);
        if (match) {
          out.push({
            name: match[1].trim(),
            description: match[2].replace(/^[\s—–:()-]+/, '').replace(/\*\*/g, '').trim(),
          });
        }
      }
      return out;
    },

    async fetchRecommendedBands(text) {
      const parsed = this.parseRecommendations(text);
      if (parsed.length === 0) return;

      this.loadingBands = true;
      try {
        const results = await Promise.all(
          parsed.map(async (item) => {
            try {
              const res = await api.get(`/bands/searchByBandName?bandName=${encodeURIComponent(item.name)}`);
              const items = res.data.artists?.items || [];
              const band = this.bestMatch(items, item.name);
              return band ? { band, description: item.description } : null;
            } catch {
              return null;
            }
          })
        );
        const seen = new Set();
        this.recommendedBands = results.filter((r) => {
          if (!r || seen.has(r.band.id)) return false;
          seen.add(r.band.id);
          return true;
        });
      } finally {
        this.loadingBands = false;
        this.bandsFetched = true;
      }
    },

    bestMatch(items, name) {
      if (items.length === 0) return null;
      const target = name.toLowerCase();
      const exact = items.find((b) => b.name.toLowerCase() === target);
      return exact || items[0];
    },

    resetRecommend() {
      this.recommendResult = '';
      this.recommendInput = '';
      this.recommendedBands = [];
      this.bandsFetched = false;
    },

    clearChat() {
      this.chatMessages = [];
      this.chatInput = '';
    },

    scrollChat() {
      const el = this.$refs.chatWindow;
      if (el) el.scrollTop = el.scrollHeight;
    },

    formatMessage(text) {
      return text
        .replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
        .replace(/\n/g, '<br>');
    },

    formatRecommendations(text) {
      const lines = text.split('\n').filter(l => l.trim());
      let html = '';
      for (const line of lines) {
        const match = line.match(/^\*\*(.+?)\*\*(.*)$/);
        if (match) {
          html += `<div class="reco-band"><span class="reco-name">${match[1]}</span><span class="reco-desc">${match[2].replace(/^[\s—–-]+/, '')}</span></div>`;
        } else if (line.trim()) {
          html += `<p class="reco-text">${line}</p>`;
        }
      }
      return html || `<p class="reco-text">${text.replace(/\n/g, '<br>')}</p>`;
    },
  },
};
</script>

<style scoped>
.oracle-wrap {
  max-width: 820px;
  margin: 0 auto;
  padding: 2rem 1.5rem 5rem;
}

.oracle-header {
  text-align: center;
  margin-bottom: 2rem;
}

.oracle-eye {
  font-size: 2.5rem;
  display: block;
  margin-bottom: 0.5rem;
  animation: eye-pulse 3s ease-in-out infinite;
}

@keyframes eye-pulse {
  0%, 100% { transform: scale(1); opacity: 1; }
  50%       { transform: scale(1.1); opacity: 0.7; }
}

.oracle-title {
  font-size: 2.2rem;
  font-weight: 900;
  text-transform: uppercase;
  letter-spacing: 0.1em;
  color: crimson;
  margin: 0 0 0.4rem;
}

.oracle-sub {
  font-size: 0.78rem;
  color: #333;
  text-transform: uppercase;
  letter-spacing: 0.1em;
  margin: 0;
}

/* Tabs */
.mode-tabs {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
  border-bottom: 1px solid #1a1a1a;
  padding-bottom: 0;
}

.mode-tab {
  padding: 0.65rem 1.25rem;
  background: none;
  border: none;
  border-bottom: 2px solid transparent;
  color: #444;
  font-size: 0.85rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  cursor: pointer;
  transition: color 0.15s, border-color 0.15s;
  margin-bottom: -1px;
}

.mode-tab:hover { color: #aaa; }
.mode-tab.active { color: crimson; border-bottom-color: crimson; }

/* Chat */
.chat-window {
  background: #0a0a0a;
  border: 1px solid #1a1a1a;
  border-radius: 12px;
  padding: 1.25rem;
  min-height: 360px;
  max-height: 520px;
  overflow-y: auto;
  margin-bottom: 0.75rem;
  scroll-behavior: smooth;
  scrollbar-width: thin;
  scrollbar-color: #1a1a1a transparent;
}

.chat-intro p { color: #333; font-size: 0.9rem; margin: 0 0 1.25rem; text-align: center; }

.starter-questions {
  display: flex;
  flex-wrap: wrap;
  gap: 0.4rem;
  justify-content: center;
}

.starter-btn {
  padding: 0.3rem 0.7rem;
  background: #111;
  color: #555;
  border: 1px solid #1a1a1a;
  border-radius: 20px;
  font-size: 0.72rem;
  cursor: pointer;
  transition: all 0.12s;
  text-align: left;
}

.starter-btn:hover { color: #ccc; border-color: crimson; }

/* Chat bubbles */
.chat-bubble {
  display: flex;
  align-items: flex-start;
  gap: 0.65rem;
  margin-bottom: 1rem;
}

.chat-bubble.user {
  flex-direction: row-reverse;
}

.bubble-avatar {
  flex-shrink: 0;
  font-size: 1.1rem;
  line-height: 1.5;
  width: 28px;
  text-align: center;
}

.bubble-content {
  max-width: 75%;
  padding: 0.65rem 0.95rem;
  border-radius: 10px;
  font-size: 0.88rem;
  line-height: 1.65;
}

.chat-bubble.assistant .bubble-content {
  background: #111;
  color: #bbb;
  border-bottom-left-radius: 3px;
}

.chat-bubble.user .bubble-content {
  background: #1a0008;
  color: #ddd;
  border-bottom-right-radius: 3px;
  text-align: right;
}

.chat-bubble.thinking .bubble-content {
  background: #0d0d0d;
  color: #333;
}

/* Typing dots */
.dots span {
  animation: dot-blink 1.4s ease-in-out infinite;
  font-size: 1.4rem;
  line-height: 0;
}

.dots span:nth-child(2) { animation-delay: 0.2s; }
.dots span:nth-child(3) { animation-delay: 0.4s; }

@keyframes dot-blink {
  0%, 80%, 100% { opacity: 0.1; }
  40%            { opacity: 1; }
}

/* Chat input */
.chat-input-row {
  display: flex;
  gap: 0.5rem;
  align-items: center;
}

.chat-input {
  flex: 1;
  padding: 0.65rem 1rem;
  background: #111;
  border: 1px solid #1e1e1e;
  border-radius: 8px;
  color: #fff;
  font-size: 0.9rem;
  transition: border-color 0.15s;
}

.chat-input:focus { outline: none; border-color: crimson; }
.chat-input::placeholder { color: #333; }
.chat-input:disabled { opacity: 0.5; }

.send-btn {
  padding: 0.65rem 1.25rem;
  background: crimson;
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 0.88rem;
  font-weight: 700;
  cursor: pointer;
  white-space: nowrap;
  transition: background 0.15s;
}

.send-btn:hover:not(:disabled) { background: #a30000; }
.send-btn:disabled { opacity: 0.4; cursor: not-allowed; }
.send-btn.full { width: 100%; margin-top: 0.75rem; padding: 0.85rem; font-size: 1rem; }

.clear-btn {
  background: none;
  border: 1px solid #1e1e1e;
  color: #333;
  font-size: 0.8rem;
  padding: 0.65rem 0.7rem;
  border-radius: 8px;
  cursor: pointer;
  transition: color 0.15s, border-color 0.15s;
}

.clear-btn:hover { color: crimson; border-color: crimson; }

/* Recommender */
.recommend-section { display: flex; flex-direction: column; }

.recommend-intro {
  color: #444;
  font-size: 0.9rem;
  margin: 0 0 1rem;
  text-align: center;
}

.recommend-examples {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  flex-wrap: wrap;
  margin-bottom: 0.85rem;
}

.ex-label { font-size: 0.7rem; color: #333; text-transform: uppercase; letter-spacing: 0.06em; white-space: nowrap; }

.ex-btn {
  padding: 0.25rem 0.6rem;
  background: #111;
  color: #444;
  border: 1px solid #1a1a1a;
  border-radius: 16px;
  font-size: 0.7rem;
  cursor: pointer;
  transition: all 0.12s;
}

.ex-btn:hover { color: #ccc; border-color: #333; }

.recommend-textarea {
  padding: 0.85rem 1rem;
  background: #111;
  border: 1px solid #1e1e1e;
  border-radius: 10px;
  color: #ddd;
  font-size: 0.9rem;
  line-height: 1.6;
  resize: vertical;
  transition: border-color 0.15s;
}

.recommend-textarea:focus { outline: none; border-color: crimson; }
.recommend-textarea::placeholder { color: #2a2a2a; }
.recommend-textarea:disabled { opacity: 0.5; }

.recommend-result {
  margin-top: 1.25rem;
  background: #0a0a0a;
  border: 1px solid #1a1a1a;
  border-radius: 12px;
  padding: 1.5rem;
}

.clear-btn-sm {
  display: block;
  margin: 1.25rem auto 0;
  background: none;
  border: 1px solid #1e1e1e;
  color: #333;
  font-size: 0.78rem;
  padding: 0.4rem 1rem;
  border-radius: 20px;
  cursor: pointer;
  transition: color 0.15s, border-color 0.15s;
}

.clear-btn-sm:hover { color: crimson; border-color: crimson; }

/* Reco results */
:deep(.reco-band) {
  display: flex;
  flex-direction: column;
  gap: 0.2rem;
  padding: 0.85rem 0;
  border-bottom: 1px solid #111;
}

:deep(.reco-band:last-child) { border-bottom: none; }

:deep(.reco-name) {
  font-size: 1rem;
  font-weight: 800;
  color: crimson;
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

:deep(.reco-desc) {
  font-size: 0.85rem;
  color: #777;
  line-height: 1.5;
}

:deep(.reco-text) {
  font-size: 0.88rem;
  color: #666;
  line-height: 1.6;
  margin: 0.5rem 0;
}

/* Recommended playable bands */
.bands-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.6rem;
  padding: 1.5rem 0 0.5rem;
  color: #555;
  font-size: 0.82rem;
}

.reco-bands {
  margin-top: 1.5rem;
  padding-top: 1.25rem;
  border-top: 1px solid #161616;
}

.reco-bands-title {
  font-size: 0.72rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  color: crimson;
  margin: 0 0 0.85rem;
}

.reco-band-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 0.6rem;
}

.reco-bands-none {
  margin-top: 1.25rem;
  font-size: 0.8rem;
  color: #444;
  text-align: center;
}

@media (max-width: 600px) {
  .oracle-title { font-size: 1.6rem; }
  .bubble-content { max-width: 85%; }
  .mode-tab { padding: 0.5rem 0.75rem; font-size: 0.75rem; }
}
</style>
