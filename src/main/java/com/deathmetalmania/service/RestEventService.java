package com.deathmetalmania.service;

import com.deathmetalmania.dao.EventDao;
import com.deathmetalmania.exception.InvalidDataException;
import com.deathmetalmania.exception.ServiceException;  // Import custom exception
import com.deathmetalmania.model.Concert;
import com.deathmetalmania.model.dto.EventDto;
import com.deathmetalmania.model.api.ticketmaster.TicketmasterApi;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RestEventService implements EventService {

    private final EventDao eventDao;
    private final RestTicketmasterService restTicketmasterService;


    public RestEventService(EventDao eventDao, RestTicketmasterService restTicketmasterService) {
        this.eventDao = eventDao;
        this.restTicketmasterService = restTicketmasterService;
    }

    @Override
    public List<Concert> getAllEvents() {
        try {
            return eventDao.getAllEvents();
        } catch (Exception e) {
            throw new ServiceException("Error retrieving all events", e);
        }
    }

    @Override
    public Concert getEventById(int id) {
        try {
            return eventDao.getEventById(id);
        } catch (Exception e) {
            throw new ServiceException("Error retrieving event with ID: " + id, e);
        }
    }

    @Override
    public Concert createEvent(EventDto eventDto) {
        try {
            // Validate input data
            if (eventDto.getName() == null || eventDto.getName().trim().isEmpty()) {
                throw new InvalidDataException("Event name cannot be empty");
            }
            if (eventDto.getDate() == null) {
                throw new InvalidDataException("Event date cannot be null");
            }


            Concert concert = new Concert();
            concert.setEvent_id(eventDto.getEvent_id());
            concert.setBandId(eventDto.getBand_id());
            concert.setName(eventDto.getName());
            concert.setDate(eventDto.getDate());
            concert.setVenue(eventDto.getVenue());
            concert.setCity(eventDto.getCity());
            concert.setCountryCode(eventDto.getCountryCode());
            concert.setMinPrice(eventDto.getMinPrice());
            concert.setMaxPrice(eventDto.getMaxPrice());
            concert.setInfo(eventDto.getInfo());

            return eventDao.createEvent(concert);
        } catch (InvalidDataException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error creating the event", e);
        }
    }

    @Override
    public void updateEvent(int id, EventDto eventDto) {
        try {
            // Convert EventDto to Concert
            Concert concert = new Concert();
            concert.setEvent_id(id);
            concert.setBandId(eventDto.getBand_id());
            concert.setName(eventDto.getName());
            concert.setDate(eventDto.getDate());
            concert.setVenue(eventDto.getVenue());
            concert.setCity(eventDto.getCity());
            concert.setCountryCode(eventDto.getCountryCode());
            concert.setInfo(eventDto.getInfo());
            concert.setMinPrice(eventDto.getMinPrice());
            concert.setMaxPrice(eventDto.getMaxPrice());

            eventDao.updateEvent(id, concert);
        } catch (Exception e) {
            throw new ServiceException("Error updating event with ID: " + id, e);
        }
    }

    @Override
    public void deleteEvent(int id) {
        try {
            eventDao.deleteEvent(id);
        } catch (Exception e) {
            throw new ServiceException("Error deleting event with ID: " + id, e);
        }
    }


    @Override
    public TicketmasterApi searchEventsByBand(String bandName) {
        try {
            return restTicketmasterService.searchEventsByBand(bandName);
        } catch (Exception e) {
            throw new ServiceException("Error searching events for band: " + bandName, e);
        }
    }
}
