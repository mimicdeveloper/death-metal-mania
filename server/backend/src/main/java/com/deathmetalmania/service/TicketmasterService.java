package com.deathmetalmania.service;

import com.deathmetalmania.model.api.ticketmaster.TicketmasterApi;

public interface TicketmasterService {

    /**
     * Search events by bandname via ticketmaster api
     * @param band
     * @return
     */
    TicketmasterApi searchEventsByBand(String band);

}