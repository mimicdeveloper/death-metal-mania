
package com.deathmetalmania.model.api.ticketmaster;

import java.util.List;



public class Attraction__1 {

    private String name;
    private String type;
    private String id;
    private Boolean test;
    private String url;
    private String locale;
    private ExternalLinks externalLinks;
    private List<Image__2> images;
    private List<Classification__1> classifications;
    private UpcomingEvents__1 upcomingEvents;
    private Links__2 links;

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

    public Boolean getTest() {
        return test;
    }

    public void setTest(Boolean test) {
        this.test = test;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public ExternalLinks getExternalLinks() {
        return externalLinks;
    }

    public void setExternalLinks(ExternalLinks externalLinks) {
        this.externalLinks = externalLinks;
    }

    public List<Image__2> getImages() {
        return images;
    }

    public void setImages(List<Image__2> images) {
        this.images = images;
    }

    public List<Classification__1> getClassifications() {
        return classifications;
    }

    public void setClassifications(List<Classification__1> classifications) {
        this.classifications = classifications;
    }

    public UpcomingEvents__1 getUpcomingEvents() {
        return upcomingEvents;
    }

    public void setUpcomingEvents(UpcomingEvents__1 upcomingEvents) {
        this.upcomingEvents = upcomingEvents;
    }

    public Links__2 getLinks() {
        return links;
    }

    public void setLinks(Links__2 links) {
        this.links = links;
    }

}
