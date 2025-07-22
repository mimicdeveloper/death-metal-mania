package com.deathmetalmania.model;

public class Band {
    private Integer band_id;
    private String spotify_id;
    private String name;
    private String genre;
    private String country;

    public Integer getBand_id() {
        return band_id;
    }

    public void setBand_id(Integer band_id) {
        this.band_id = band_id;
    }

    public String getSpotify_id() {
        return spotify_id;
    }

    public void setSpotify_id(String spotify_id) {
        this.spotify_id = spotify_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
