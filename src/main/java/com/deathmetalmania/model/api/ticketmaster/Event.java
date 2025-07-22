package com.deathmetalmania.model.api.ticketmaster;

import java.util.List;

public class Event {

    private String name;
    private String type;
    private String id;
    private String url;
    private Sales sales;
    private Dates dates;
    private String info;
    private String pleaseNote;
    private List<PriceRange> priceRanges;
    private Links links;
    private Embedded__1 embedded;
    private Accessibility accessibility;
    private AgeRestrictions ageRestrictions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Sales getSales() {
        return sales;
    }

    public void setSales(Sales sales) {
        this.sales = sales;
    }

    public Dates getDates() {
        return dates;
    }

    public void setDates(Dates dates) {
        this.dates = dates;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPleaseNote() {
        return pleaseNote;
    }

    public void setPleaseNote(String pleaseNote) {
        this.pleaseNote = pleaseNote;
    }

    public List<PriceRange> getPriceRanges() {
        return priceRanges;
    }

    public void setPriceRanges(List<PriceRange> priceRanges) {
        this.priceRanges = priceRanges;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public Embedded__1 getEmbedded() {
        return embedded;
    }

    public void setEmbedded(Embedded__1 embedded) {
        this.embedded = embedded;
    }

    public Accessibility getAccessibility() {
        return accessibility;
    }

    public void setAccessibility(Accessibility accessibility) {
        this.accessibility = accessibility;
    }

    public AgeRestrictions getAgeRestrictions() {
        return ageRestrictions;
    }

    public void setAgeRestrictions(AgeRestrictions ageRestrictions) {
        this.ageRestrictions = ageRestrictions;
    }
}