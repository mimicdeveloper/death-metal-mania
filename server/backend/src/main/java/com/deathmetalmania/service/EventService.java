package com.deathmetalmania.service;

import com.deathmetalmania.model.Concert;
import com.deathmetalmania.model.api.ticketmaster.TicketmasterApi;
import com.deathmetalmania.model.dto.EventDto;
import java.util.List;

public interface EventService {

    /**
     * Get all events available
     * @return list of all events, empty list if none
     */
    List<Concert> getAllEvents();

    /**
     * Get event by ID
     * @param id
     * @return
     */
    Concert getEventById(int id);

    /**
     * Create event by dto
     * @param eventDto
     * @return event created
     */

    Concert createEvent(EventDto eventDto);

    /**
     * Update event by id and dto
     * @param id
     * @param eventDto
     */
    void updateEvent(int id, EventDto eventDto);

    /**
     * Delete event by id
     * @param id
     */
    void deleteEvent(int id);

    /**
     * Search events by band name by ticketmaster api
     * @param bandName
     * @return
     */
    TicketmasterApi searchEventsByBand(String bandName);

}
