<template>
  <div class="admin-view">
    <h1>Admin Dashboard</h1>

    <!-- ========== USERS ========== -->
    <section class="admin-users-section">
      <h2>Manage Users</h2>
      <p class="status-message" v-if="statusMessage.user">{{ statusMessage.user }}</p>
      <div class="button-group">
        <button class="get-all" @click="getAllUsers">Get All Users</button>
        <button v-on:click="clearUsers" class="clear-button">Clear</button>
      </div>
      <div v-if="showUsersList && usersList.length" id="usersList">
        <div v-for="user in usersList" :key="user.profileId" class="user-card">
          <p><strong>Profile ID:</strong> {{ user.profileId }}</p>
          <p><strong>User ID:</strong> {{ user.userId }}</p>
          <p><strong>Name:</strong> {{ user.firstName }} {{ user.lastName }}</p>
          <p><strong>Email:</strong> {{ user.email }}</p>
          <button class="delete-button" @click="deleteUserById(user.userId)">Delete</button>
        </div>
      </div>

      <form v-on:submit.prevent="getUserById">
        <input v-model="userIdInput" type="number" placeholder="User ID" required />
        <button type="submit">Get User by ID</button>
      </form>

      <form v-on:submit.prevent="updateUser">
        <input v-model="updateUserId" type="number" placeholder="User ID" required />
        <input v-model="updateFirstName" type="text" placeholder="First Name" />
        <input v-model="updateLastName" type="text" placeholder="Last Name" />
        <input v-model="updateEmail" type="email" placeholder="Email" />
        <button type="submit">Update User</button>
      </form>
    </section>

    <hr class="section-divider" />

    <!-- ========== BANDS ========== -->
    <section class="admin-bands-section">
      <h2>Manage Bands</h2>
      <p class="status-message" v-if="statusMessage.band">{{ statusMessage.band }}</p>
      <div class="button-group">
        <button class="get-all" @click="getAllBands">Get All Bands</button>
        <button v-on:click="clearBands" class="clear-button">Clear</button>
      </div>
      <div v-if="showBandsList && bandsList.length" id="allBandsList">
        <div v-for="band in bandsList" :key="band.band_id" class="band-card">
          <p><strong>Band ID:</strong> {{ band.band_id }}</p>
          <p><strong>Band Name:</strong> {{ band.name }}</p>
          <p><strong>Genre:</strong> {{ band.genre }}</p>
          <p><strong>Country:</strong> {{ band.country }}</p>
          <button class="delete-button" @click="deleteBandById(band.band_id)">Delete</button>
        </div>
      </div>

      <form v-on:submit.prevent="addBand">
        <input v-model="bandSpotifyId" type="text" placeholder="Spotify ID" required />
        <input v-model="bandName" type="text" placeholder="Band Name" required />
        <input v-model="bandGenre" type="text" placeholder="Genre" required />
        <input v-model="bandCountry" type="text" placeholder="Country" required />
        <button type="submit">Add Band</button>
      </form>

      <form v-on:submit.prevent="updateBand">
        <input v-model="updateBandId" type="number" placeholder="Band ID" required />
        <input v-model="updateBandSpotifyId" type="text" placeholder="Spotify ID" required />
        <input v-model="updateBandName" type="text" placeholder="Band Name" required />
        <input v-model="updateBandGenre" type="text" placeholder="Genre" required />
        <input v-model="updateBandCountry" type="text" placeholder="Country" required />
        <button type="submit">Update Band</button>
      </form>
    </section>

    <hr class="section-divider" />

    <!-- ========== EVENTS ========== -->
    <section class="admin-events-section">
      <h2>Manage Events</h2>
      <p class="status-message" v-if="statusMessage.event">{{ statusMessage.event }}</p>
      <div class="button-group">
        <button class="get-all" @click="getAllEvents">Get All Events</button>
        <button v-on:click="clearEvents" class="clear-button">Clear</button>
      </div>
      <div v-if="showEventsList && eventsList.length" id="eventsList">
        <div v-for="event in eventsList" :key="event.eventId" class="event-card">
          <p><strong>Event ID:</strong> {{ event.eventId }}</p>
          <p><strong>Event Name:</strong> {{ event.name }}</p>
          <p><strong>Venue:</strong> {{ event.venue }}</p>
          <p><strong>City:</strong> {{ event.city }}</p>
          <p><strong>Country Code:</strong> {{ event.countryCode }}</p>
          <p><strong>Date:</strong> {{ event.date }}</p>
          <p><strong>Min Price:</strong> ${{ event.minPrice }}</p>
          <p><strong>Max Price:</strong> ${{ event.maxPrice }}</p>
          <p><strong>Info:</strong> {{ event.info }}</p>
          <button class="delete-button" @click="deleteEventById(event.eventId)">Delete</button>
        </div>
      </div>

      <form v-on:submit.prevent="addEvent">
        <input v-model="addBandId" type="number" placeholder="Band ID" required />
        <input v-model="addEventName" type="text" placeholder="Event Name" required />
        <input v-model="addEventDate" type="date" required />
        <input v-model="addVenue" type="text" placeholder="Venue" required />
        <input v-model="addCity" type="text" placeholder="City" required />
        <input v-model="addCountryCode" type="text" placeholder="Country Code" required />
        <input v-model="addMinPrice" type="number" placeholder="Min Price" required />
        <input v-model="addMaxPrice" type="number" placeholder="Max Price" required />
        <input v-model="addInfo" type="text" placeholder="Info" />
        <button type="submit">Add Event</button>
      </form>

      <form v-on:submit.prevent="updateEvent">
        <input v-model="updateBandId" type="number" placeholder="Band ID" required />
        <input v-model="updateEventId" type="number" placeholder="Event ID" required />
        <input v-model="updateEventName" type="text" placeholder="Event Name" required />
        <input v-model="updateEventDate" type="date" required />
        <input v-model="updateVenue" type="text" placeholder="Venue" required />
        <input v-model="updateCity" type="text" placeholder="City" required />
        <input v-model="updateCountryCode" type="text" placeholder="Country Code" required />
        <input v-model="updateMinPrice" type="number" placeholder="Min Price" required />
        <input v-model="updateMaxPrice" type="number" placeholder="Max Price" required />
        <input v-model="updateInfo" type="text" placeholder="Info" />
        <button type="submit">Update Event</button>
      </form>
    </section>
  </div>
</template>

<script>
import api from '@/api.js'; // your axios instance configured with Koyeb backend

export default {
  data() {
    return {
      // Raw reactive lists from backend, these hold actual data
      usersListRaw: [],
      bandsListRaw: [],
      eventsListRaw: [],

      // Booleans to control visibility
      showUsersList: false,
      showBandsList: false,
      showEventsList: false,

      // Status messages for each section
      statusMessage: {
        user: '',
        band: '',
        event: ''
      },

      // User input fields
      userIdInput: '',
      updateUserId: '',
      updateFirstName: '',
      updateLastName: '',
      updateEmail: '',

      // Band input fields
      bandSpotifyId: '',
      bandName: '',
      bandGenre: '',
      bandCountry: '',
      updateBandId: '',
      updateBandSpotifyId: '',
      updateBandName: '',
      updateBandGenre: '',
      updateBandCountry: '',

      // Event input fields
      addBandId: '',
      addEventName: '',
      addEventDate: '',
      addVenue: '',
      addCity: '',
      addCountryCode: '',
      addMinPrice: '',
      addMaxPrice: '',
      addInfo: '',
      updateEventId: '',
      updateEventName: '',
      updateEventDate: '',
      updateVenue: '',
      updateCity: '',
      updateCountryCode: '',
      updateMinPrice: '',
      updateMaxPrice: '',
      updateInfo: ''
    };
  },

  computed: {
    usersList() {
      return this.usersListRaw;
    },
    bandsList() {
      return this.bandsListRaw;
    },
    eventsList() {
      return this.eventsListRaw;
    }
  },

  methods: {
    getAuthHeader() {
      const token = localStorage.getItem('token');
      return {
        headers: {
          Authorization: `Bearer ${token}`
        }
      };
    },

    // --------------------
    // USER METHODS
    // --------------------
    async getAllUsers(clearStatus = true) {
      try {
        const res = await api.get('/admin/users', this.getAuthHeader());
        this.usersListRaw = res.data;
        this.showUsersList = true;
        if (clearStatus) {
          this.statusMessage.user = 'Users loaded successfully.';
        }
      } catch (err) {
        this.statusMessage.user = 'Failed to load users.';
        console.error('Error fetching users:', err);
      }
    },

    clearUsers() {
      this.usersListRaw = [];
      this.showUsersList = false;
      this.statusMessage.user = '';
    },

    async getUserById() {
      try {
        const res = await api.get(`/admin/users/${this.userIdInput}`, this.getAuthHeader());
        const user = res.data;
        this.statusMessage.user = `User found: ${user.firstName} ${user.lastName}, Email: ${user.email}`;
      } catch (error) {
        console.error(error);
        if (error.response && error.response.status === 404) {
          this.statusMessage.user = 'User not found.';
        } else {
          this.statusMessage.user = 'An error occurred.';
        }
      }
    },

    async updateUser() {
      try {
        const body = {
          firstName: this.updateFirstName,
          lastName: this.updateLastName,
          email: this.updateEmail
        };
        await api.put(`/admin/users/${this.updateUserId}`, body, this.getAuthHeader());
        this.statusMessage.user = 'User updated successfully.';

        // Clear inputs
        this.updateUserId = '';
        this.updateFirstName = '';
        this.updateLastName = '';
        this.updateEmail = '';

        await this.getAllUsers(false); // prevent overwriting status message
      } catch (error) {
        console.error(error);
        if (error.response && error.response.status === 404) {
          this.statusMessage.user = 'User not found.';
        } else {
          this.statusMessage.user = 'Failed to update user.';
        }
      }
    },

    async deleteUserById(userId) {
      try {
        await api.delete(`/admin/users/${userId}`, this.getAuthHeader());
        this.statusMessage.user = `User ${userId} deleted.`;
        this.getAllUsers(false); // Refresh list
      } catch (err) {
        this.statusMessage.user = `Failed to delete user ${userId}.`;
        console.error('Delete user error:', err);
      }
    },

    // --------------------
    // BAND METHODS
    // --------------------
    async getAllBands(clearStatus = true) {
      try {
        const res = await api.get('/bands', this.getAuthHeader());
        this.bandsListRaw = res.data;
        this.showBandsList = true;
        if (clearStatus) {
          this.statusMessage.band = 'Bands loaded successfully.';
        }
      } catch {
        this.statusMessage.band = 'Failed to load bands.';
      }
    },

    clearBands() {
      this.bandsListRaw = [];
      this.showBandsList = false;
      this.statusMessage.band = '';
    },

    async addBand() {
      try {
        const body = {
          spotify_id: this.bandSpotifyId,
          name: this.bandName,
          genre: this.bandGenre,
          country: this.bandCountry
        };
        await api.post('/admin/bands', body, this.getAuthHeader());
        this.statusMessage.band = 'Band added successfully.';

        // Clear inputs
        this.bandSpotifyId = '';
        this.bandName = '';
        this.bandGenre = '';
        this.bandCountry = '';

        await this.getAllBands(false); // prevent overwriting status message
      } catch {
        this.statusMessage.band = 'Failed to add band.';
      }
    },

    async updateBand() {
      try {
        const body = {
          spotify_id: this.updateBandSpotifyId,
          name: this.updateBandName,
          genre: this.updateBandGenre,
          country: this.updateBandCountry
        };
        await api.put(`/admin/bands/${this.updateBandId}`, body, this.getAuthHeader());
        this.statusMessage.band = 'Band updated successfully.';

        // Clear inputs
        this.updateBandId = '';
        this.updateBandSpotifyId = '';
        this.updateBandName = '';
        this.updateBandGenre = '';
        this.updateBandCountry = '';

        await this.getAllBands(false); // prevent overwriting status message
      } catch {
        this.statusMessage.band = 'Failed to update band.';
      }
    },

    async deleteBandById(bandId) {
      try {
        await api.delete(`/admin/bands/${bandId}`, this.getAuthHeader());
        this.statusMessage.band = `Band ${bandId} deleted.`;
        this.getAllBands(false); // Refresh list
      } catch (err) {
        this.statusMessage.band = `Failed to delete band ${bandId}.`;
        console.error('Delete band error:', err);
      }
    },

    // --------------------
    // EVENT METHODS
    // --------------------
    async getAllEvents(clearStatus = true) {
      try {
        const res = await api.get('/events', this.getAuthHeader());
        this.eventsListRaw = res.data;
        this.showEventsList = true;
        if (clearStatus) {
          this.statusMessage.event = 'Events loaded successfully.';
        }
      } catch {
        this.statusMessage.event = 'Failed to load events.';
      }
    },

    clearEvents() {
      this.eventsListRaw = [];
      this.showEventsList = false;
      this.statusMessage.event = '';
    },

    formatDate(date) {
      const [year, month, day] = date.split('-');
      return `${month}-${day}-${year}`;
    },

    async addEvent() {
      try {
        const body = {
          band_id: this.addBandId,
          name: this.addEventName,
          date: this.formatDate(this.addEventDate),
          venue: this.addVenue,
          city: this.addCity,
          countryCode: this.addCountryCode,
          minPrice: this.addMinPrice,
          maxPrice: this.addMaxPrice,
          info: this.addInfo
        };
        await api.post('/admin/events', body, this.getAuthHeader());
        this.statusMessage.event = 'Event added successfully.';

        // Clear inputs
        this.addBandId = '';
        this.addEventName = '';
        this.addEventDate = '';
        this.addVenue = '';
        this.addCity = '';
        this.addCountryCode = '';
        this.addMinPrice = '';
        this.addMaxPrice = '';
        this.addInfo = '';

        await this.getAllEvents(false); // prevent overwriting status message
      } catch {
        this.statusMessage.event = 'Failed to add event.';
      }
    },

    async updateEvent() {
      try {
        const body = {
          band_id: this.updateBandId,
          name: this.updateEventName,
          date: this.formatDate(this.updateEventDate),
          venue: this.updateVenue,
          city: this.updateCity,
          countryCode: this.updateCountryCode,
          minPrice: this.updateMinPrice,
          maxPrice: this.updateMaxPrice,
          info: this.updateInfo
        };
        await api.put(`/admin/events/${this.updateEventId}`, body, this.getAuthHeader());
        this.statusMessage.event = 'Event updated successfully.';

        // Clear inputs
        this.updateEventId = '';
        this.updateBandId = '';
        this.updateEventName = '';
        this.updateEventDate = '';
        this.updateVenue = '';
        this.updateCity = '';
        this.updateCountryCode = '';
        this.updateMinPrice = '';
        this.updateMaxPrice = '';
        this.updateInfo = '';

        await this.getAllEvents(false); // prevent overwriting status message
      } catch {
        this.statusMessage.event = 'Failed to update event.';
      }
    },

    async deleteEventById(eventId) {
      try {
        await api.delete(`/admin/events/${eventId}`, this.getAuthHeader());
        this.statusMessage.event = `Event ${eventId} deleted.`;
        this.getAllEvents(false); // Refresh list
      } catch (err) {
        this.statusMessage.event = `Failed to delete event ${eventId}.`;
        console.error('Delete event error:', err);
      }
    }
  }
};
</script>

<style scoped>
/* Your existing styles here, unchanged */
.status-message {
  color: white;
  font-weight: bold;
  margin: 0.5rem 0 1rem;
}

.button-group {
  margin-bottom: 1.5rem;
  display: flex;
  gap: 0.75rem;
}

.admin-view {
  color: #fff;
  padding: 2rem;
}

h1 {
  font-size: 2rem;
  text-align: center;
  margin-bottom: 2rem;
  color: #fff;
  font-weight: bold;
}

section {
  padding: 2rem;
  background-color: #2b2b2b;
  border-radius: 8px;
  max-width: 900px;
  margin: 2rem auto;
}

section h2 {
  font-size: 2rem;
  margin-bottom: 1.5rem;
  text-align: center;
  color: #fff;
  font-weight: bold;
}

form {
  background-color: #333;
  padding: 2rem;
  border-radius: 8px;
  width: 100%;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.5);
  margin-bottom: 2rem;
}

form label {
  display: block;
  margin: 0.5rem 0 0.25rem;
  color: #fff;
}

form input {
  width: 100%;
  padding: 0.75rem;
  margin-bottom: 1.5rem;
  background: #1a1a1a;
  color: #fff;
  border: 1px solid #444;
  border-radius: 4px;
}

form button {
  background-color: crimson;
  color: white;
  padding: 0.75rem 1.5rem;
  font-size: 1rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  display: block;
  margin: 1rem auto 0;
}

form button:hover {
  background-color: #a30000;
}

button.get-all {
  background-color: crimson;
  color: white;
  padding: 0.75rem 1.5rem;
  font-size: 1rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  display: inline-block;
  margin: 1rem 1rem 0;
}

button.clear-button {
  background-color: crimson;
  color: white;
  padding: 0.75rem 1.5rem;
  font-size: 1rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  display: inline-block;
  margin: 1rem 1rem 0;
}

button.get-all:hover {
  background-color: #a30000;
}

button.clear-button:hover {
  background-color: #a30000;
}

#usersList,
#allBandsList,
#eventsList {
  margin-top: 1.5rem;
}

.user-card,
.band-card,
.event-card {
  background-color: #333;
  border-radius: 8px;
  padding: 1rem;
  margin-bottom: 1rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.5);
}

.user-card p,
.band-card p,
.event-card p {
  margin-bottom: 0.5rem;
}

.section-divider {
  border: 1px solid #444;
  margin: 2rem 0;
}

button[type='submit']:hover {
  background-color: #a30000;
}

button.delete-button {
  background-color: crimson;
  color: white;
  padding: 0.75rem 1.5rem;
  font-size: 1rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  display: inline-block;
  margin: 1rem 1rem 0;
}

button.delete-button:hover {
  background-color: #a30000;
}
</style>
