package com.deathmetalmania.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Concert {
    private Long eventId;  // Changed to camelCase
    private int bandId;    // Changed to camelCase
    private String name;
    private LocalDate date;
    private String venue;
    private String city;
    private String countryCode;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String info;

    // Getters and setters
    public Long getEventId() {
        return eventId;
    }

    public int getBandId() {
        return bandId;
    }

    public void setBandId(int bandId) {
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

    // Implement the setter for eventId
    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
}
