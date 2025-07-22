package com.deathmetalmania.model.api.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class SpotifyPlaylistResponse {

    @JsonProperty("items")
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static class Item {

        @JsonProperty("track")
        private Track track;

        public Track getTrack() {
            return track;
        }

        public void setTrack(Track track) {
            this.track = track;
        }
    }

    public static class Track {

        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("artists")
        private List<SimpleArtist> artists;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<SimpleArtist> getArtists() {
            return artists;
        }

        public void setArtists(List<SimpleArtist> artists) {
            this.artists = artists;
        }
    }

    public static class SimpleArtist {

        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
