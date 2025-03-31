package com.deathmetalmania.controller;

import com.deathmetalmania.dao.*;
import com.deathmetalmania.exception.NotFoundException;
import com.deathmetalmania.exception.InvalidDataException;
import com.deathmetalmania.model.dto.BandDto;
import com.deathmetalmania.model.UserProfile;
import com.deathmetalmania.model.dto.UserProfileDto;
import com.deathmetalmania.model.dto.EventDto;
import com.deathmetalmania.model.Concert;
import com.deathmetalmania.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final UserDao userDao;
    private final BandService bandService;
    private final EventService eventService;


    public AdminController(UserDao userDao,
                           BandService bandService,
                           EventService eventService) {
        this.userDao = userDao;
        this.bandService = bandService;
        this.eventService = eventService;
    }

    // ==========================
    // User Management Endpoints
    // ==========================

    // Get all user profiles
    @RequestMapping(path = "/users", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserProfile> getAllUserProfiles() {
        List<UserProfile> users = userDao.getAllUserProfiles();
        if (users.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No users found");
        }
        return users;
    }

    // Get a user profile by ID
    @RequestMapping(path = "/users/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserProfile getUserProfileById(@PathVariable int id) {
        UserProfile userProfile = userDao.getUserProfileById(id);
        if (userProfile != null) {
            return userProfile;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    // Update a user profile
    @RequestMapping(path = "/users/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserProfile updateUserProfile(@PathVariable int id, @RequestBody UserProfileDto profile) {
        UserProfile existingProfile = userDao.getUserProfileById(id);

        if (existingProfile == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        try {
            return userDao.updateUserProfile(id, profile);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user profile data", e);
        }
    }

    // Delete a user account
    @RequestMapping(path = "/users/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserAccount(@PathVariable int id) {
        UserProfile existingProfile = userDao.getUserProfileById(id);
        if (existingProfile == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userDao.deleteUserById(id);
    }

    // ==========================
    // Band Management Endpoints
    // ==========================

    // Add a new band
    @RequestMapping(path = "/bands", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public void addBand(@RequestBody BandDto bandDto) {
        try {
            bandService.addBand(bandDto);
        } catch (InvalidDataException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid band data", e);
        }
    }

    // Update a band
    @RequestMapping(path = "/bands/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateBand(@PathVariable int id, @RequestBody BandDto bandDto) {
        try {
            bandService.updateBand(id, bandDto);
        } catch (InvalidDataException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid band data", e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Band not found", e);
        }
    }

    // Delete a band
    @RequestMapping(path = "/bands/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBand(@PathVariable int id) {
        try {
            bandService.deleteBand(id);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Band not found", e);
        }
    }

    // ==========================
    // Event Management Endpoints
    // ==========================

    // Create an event
    @RequestMapping(path = "/events", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Concert createEvent(@RequestBody @Valid EventDto eventDto) {
        try {
            return eventService.createEvent(eventDto);
        } catch (InvalidDataException e) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid event data", e);
        }
    }


    // Update an event
    @RequestMapping(path = "/events/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateEvent(@PathVariable int id, @RequestBody @Valid EventDto eventDto) {
        try {
            eventService.updateEvent(id, eventDto);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found", e);
        } catch (InvalidDataException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid event data", e);
        }
    }

    // Delete an event
    @RequestMapping(path = "/events/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable int id) {
        try {
            eventService.deleteEvent(id);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found", e);
        }
    }
}
