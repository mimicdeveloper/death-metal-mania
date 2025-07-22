
package com.deathmetalmania.model.api.ticketmaster;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Embedded {
    @JsonProperty("events")
    private List<Event> events;

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

}
