import { createStore as _createStore } from 'vuex';
import axios from 'axios';

export function createStore(currentToken, currentUser) {
  let store = _createStore({
    state: {
      token: currentToken || '',
      user: currentUser || {},
      favoriteEvents: [],
    },
    mutations: {
      SET_AUTH_TOKEN(state, token) {
        state.token = token;
        localStorage.setItem('token', token);
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      },
      SET_USER(state, user) {
        state.user = user;
        localStorage.setItem('user', JSON.stringify(user));
      },
      LOGOUT(state) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        state.token = '';
        state.user = {};
        state.favoriteEvents = [];
        axios.defaults.headers.common = {};
      },
      SET_FAVORITE_EVENTS(state, events) {
        state.favoriteEvents = events;
      },
      ADD_FAVORITE_EVENT(state, event) {
        const exists = state.favoriteEvents.some(e => e.id === event.id);
        if (!exists) {
          state.favoriteEvents.push(event);
        }
      },
      REMOVE_FAVORITE_EVENT(state, eventId) {
        state.favoriteEvents = state.favoriteEvents.filter(e => e.id !== eventId);
      }
    },
    actions: {
      async fetchFavoriteEvents({ commit, state }) {
        try {
          const response = await axios.get('http://localhost:9000/favorites/events', {
            headers: {
              Authorization: `Bearer ${state.token}`,
            },
          });
          commit('SET_FAVORITE_EVENTS', response.data);
        } catch (error) {
          console.error('Error fetching favorite events:', error);
          commit('SET_FAVORITE_EVENTS', []);
        }
      },
      async addFavoriteEvent({ commit, state }, event) {
        try {
          await axios.post(
            'http://localhost:9000/favorites/events',
            {
              eventId: event.id,
              eventName: event.name,
              localDate: event.dates?.start?.localDate,
              localTime: event.dates?.start?.localTime,
              city: event.city,
              state: event.state,
              venue: event.venue,
              url: event.url,
              info: event.info || '',
            },
            {
              headers: {
                Authorization: `Bearer ${state.token}`,
              },
            }
          );
          commit('ADD_FAVORITE_EVENT', event);
          return true;
        } catch (error) {
          console.error('Error adding favorite event:', error);
          return false;
        }
      },
      async removeFavoriteEvent({ commit, state }, eventId) {
        try {
          await axios.delete(`http://localhost:9000/favorites/events/${eventId}`, {
            headers: {
              Authorization: `Bearer ${state.token}`,
            },
          });
          commit('REMOVE_FAVORITE_EVENT', eventId);
          return true;
        } catch (error) {
          console.error('Error removing favorite event:', error);
          return false;
        }
      }
    },
  });

  if (currentToken) {
    axios.defaults.headers.common['Authorization'] = `Bearer ${currentToken}`;
  }

  return store;
}
