package com.deathmetalmania.dao;

import com.deathmetalmania.model.Concert;
import java.util.List;

public interface EventDao {
    /**
     * Get all events available
     * @return list of events, empty list if none
     */
    List<Concert> getAllEvents();

    /**
     * Get single event details by ID
     * @param id
     * @return return event by ID
     */
    Concert getEventById(int id);

    /**
     * Create a new event
     * @param concert
     * @return new concert details
     */
    Concert createEvent(Concert concert);

    /**
     * Update event detail by ID
     * @param id
     * @param concert
     */
    void updateEvent(int id, Concert concert);

    /**
     * Delete single event by ID
     * @param id
     */
    void deleteEvent(int id);

}
