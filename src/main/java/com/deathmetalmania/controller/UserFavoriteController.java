package com.deathmetalmania.controller;

import com.deathmetalmania.dao.UserFavoriteDao;
import com.deathmetalmania.exception.InvalidDataException;
import com.deathmetalmania.model.Favorite;
import com.deathmetalmania.model.dto.UserFavoriteDto;
import com.deathmetalmania.service.UserFavoriteService;
import com.deathmetalmania.service.RestUserFavoriteService;
import com.deathmetalmania.service.UserService;
import com.deathmetalmania.exception.DaoException; // Import DaoException
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/favorites")

public class UserFavoriteController {

    private final UserFavoriteService userFavoriteService;
    private final UserService userService;


    public UserFavoriteController(UserFavoriteDao userFavoriteDao, UserService userService) {
        this.userFavoriteService = new RestUserFavoriteService(userFavoriteDao);
        this.userService = userService;
    }

    // ==========================
    // Get the authenticated user's favorite bands and ratings
    // ==========================
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(method = RequestMethod.GET)
    public List<Favorite> getFavoritesByUserId(Principal principal) {
        try {
            int authenticatedUserId = userService.getUserIdByUsername(principal.getName());
            List<Favorite> favorites = userFavoriteService.getFavoritesByUserId(authenticatedUserId);

            if (favorites == null || favorites.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No favorites found for the user");
            }
            return favorites;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving user favorites", e);
        }
    }

    // ==========================
    // Add a band to favorites with rating
    // ==========================
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(method = RequestMethod.POST)
    public void addFavorite(Principal principal, @RequestBody UserFavoriteDto userFavoriteDto) {
        try {
            if (userFavoriteDto.getBandId() == null || userFavoriteDto.getBandId() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or missing band ID");
            }

            int userId = userService.getUserIdByUsername(principal.getName());
            userFavoriteService.addFavoriteByBandName(userId, userFavoriteDto);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error while adding favorite", e);
        } catch (InvalidDataException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid favorite data", e);
        }
    }

    // ==========================
    // Remove a band from favorites
    // ==========================
    @PreAuthorize("hasRole('USER')")
    @RequestMapping(path = "/{bandName}", method = RequestMethod.DELETE)
    public void deleteFavoriteByBandName(Principal principal, @PathVariable String bandName) {
        try {
            int userId = userService.getUserIdByUsername(principal.getName());
            userFavoriteService.deleteFavoriteByBandName(userId, bandName);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error during deletion", e);
        } catch (InvalidDataException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid band name", e);
        }
    }
}
