package com.deathmetalmania.controller;

import com.deathmetalmania.model.Concert;
import com.deathmetalmania.model.api.ticketmaster.TicketmasterApi;
import com.deathmetalmania.service.EventService;
import com.deathmetalmania.exception.DaoException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // ==========================
    // Get all events (Authenticated User/Admin)
    // ==========================
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public List<Concert> getAllEvents() {
        try {
            return eventService.getAllEvents();
        } catch (DaoException e) {
            // Handle database or service errors
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve events from the database", e);
        }
    }

    // ==========================
    // Get specific event by ID (Authenticated User)
    // ==========================
    @RequestMapping(path = "/{eventId}", method = RequestMethod.GET)
    public Concert getEventById(@PathVariable int eventId) {
        try {
            Concert concert = eventService.getEventById(eventId);
            if (concert == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
            }
            return concert;
        } catch (DaoException e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve event details", e);
        }
    }

    // ==========================
    // Search events by band name
    // ==========================
    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public TicketmasterApi searchEvents(@RequestParam String band_name) {
        try {
            return eventService.searchEventsByBand(band_name);

        } catch (IllegalArgumentException e) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid band name or request parameters", e);

        } catch (DaoException e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to search events from the database or external service", e);
        }
    }
}
