package com.deathmetalmania.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

/**
 * UserFavoriteDto is a class used to hold the user's favorite band's information and their rating.
 */
public class UserFavoriteDto {

    @Min(value = 1, message = "Band ID must be greater than 0")
    private Integer bandId;  // Changed from int to Integer

    @NotBlank(message = "Band name cannot be blank")
    private String bandName;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;

    public UserFavoriteDto(Integer bandId, String bandName, int rating) { // Updated constructor
        this.bandId = bandId;
        this.bandName = bandName;
        this.rating = rating;
    }

    // Getters and Setters

    public Integer getBandId() { // Return type remains Integer
        return bandId;
    }

    public void setBandId(Integer bandId) {
        this.bandId = bandId;
    }

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
