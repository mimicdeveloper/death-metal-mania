package com.deathmetalmania.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BandDto {

    @JsonProperty("band_id")
    private Integer band_id;

    @JsonProperty("spotify_id")
    @NotBlank(message = "Spotify ID cannot be blank")
    @Size(min = 1, max = 50, message = "Spotify ID must be between 1 and 50 characters")
    private String spotify_id;

    @JsonProperty("name")
    @NotBlank(message = "Band name cannot be blank")
    @Size(min = 1, max = 100, message = "Band name must be between 1 and 100 characters")
    private String name;

    @JsonProperty("genre")
    @NotBlank(message = "Genre cannot be blank")
    @Size(min = 1, max = 50, message = "Genre must be between 1 and 50 characters")
    private String genre;

    @JsonProperty("country")
    @NotBlank(message = "Country cannot be blank")
    @Size(min = 1, max = 50, message = "Country must be between 1 and 50 characters")
    private String country;

    public BandDto() {}

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
