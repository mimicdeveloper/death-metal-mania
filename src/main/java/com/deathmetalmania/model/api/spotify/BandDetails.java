package com.deathmetalmania.model.api.spotify;

import java.util.List;

public class BandDetails {
    private String id;
    private String name;
    private List<String> genres;
    private int popularity;
    private Followers followers; // Reference to a separate Followers class

    // Constructor
    public BandDetails() {}

    public BandDetails(String id, String name, List<String> genres, int popularity, Followers followers) {
        this.id = id;
        this.name = name;
        this.genres = genres;
        this.popularity = popularity;
        this.followers = followers;
    }

    // Getters and Setters
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

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public Followers getFollowers() {
        return followers;
    }

    public void setFollowers(Followers followers) {
        this.followers = followers;
    }

    // Inner class for Followers
    public static class Followers {
        private int total; // Total count of followers

        // Constructor
        public Followers() {}

        public Followers(int total) {
            this.total = total;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }
}
