package com.deathmetalmania.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EventDto {

    @NotNull(message = "Band ID cannot be null")
    @JsonProperty("band_id")
    private Integer bandId;

    @NotBlank(message = "Event name cannot be blank")
    @Size(min = 1, max = 100, message = "Event name must be between 1 and 100 characters")
    private String name;

    @NotNull(message = "Event date cannot be null")
    @DateTimeFormat(pattern = "MM-dd-yyyy")
    private LocalDate date;

    @NotBlank(message = "Venue cannot be blank")
    @Size(min = 1, max = 100, message = "Venue must be between 1 and 100 characters")
    private String venue;

    @NotBlank(message = "City cannot be blank")
    @Size(min = 1, max = 100, message = "City must be between 1 and 100 characters")
    private String city;

    @NotBlank(message = "Country code cannot be blank")
    @Size(min = 2, max = 3, message = "Country code must be between 2 and 3 characters")
    private String countryCode;

    @NotNull(message = "Minimum price cannot be null")
    @Min(value = 0, message = "Minimum price must be at least 0")
    private BigDecimal minPrice;

    @NotNull(message = "Maximum price cannot be null")
    @Min(value = 0, message = "Maximum price must be at least 0")
    private BigDecimal maxPrice;

    @Size(max = 500, message = "Information must be less than 500 characters")
    private String info;

    // Getters and Setters for all fields, without event_id

    public Integer getBandId() {
        return bandId;
    }

    public void setBandId(Integer bandId) {
        this.bandId = bandId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
