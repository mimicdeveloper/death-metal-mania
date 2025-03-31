package com.deathmetalmania.controller;

import com.deathmetalmania.dao.BandDao;
import com.deathmetalmania.exception.DaoException;
import com.deathmetalmania.model.Band;
import com.deathmetalmania.model.api.spotify.AlbumResponse;
import com.deathmetalmania.model.api.spotify.BandDetails;
import com.deathmetalmania.model.api.spotify.SpotifyApi;
import com.deathmetalmania.service.BandService;
import com.deathmetalmania.service.RestBandService;
import com.deathmetalmania.service.RestSpotifyService;
import com.deathmetalmania.service.SpotifyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/bands")
public class BandController {

    private final SpotifyService spotifyService;
    private final BandService bandService;

    // Constructor-based dependency injection
    public BandController(SpotifyService spotifyService, BandService bandService) {
        this.spotifyService = spotifyService;
        this.bandService = bandService;
    }

    //Retrieve all bands
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public List<Band> getAllBands() {
        try {
            List<Band> bands = bandService.getAllBands();
            if (bands.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No bands found");
            }
            return bands;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurred while fetching bands", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error fetching bands", e);
        }
    }


    //Search bands by Death Metal genre
    @RequestMapping(path = "/searchByDeathMetalGenre", method = RequestMethod.GET)
    public SpotifyApi searchDeathMetalBands() {
        try {
            SpotifyApi spotifyApi = spotifyService.searchDeathMetalBands();
            if (spotifyApi == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No death metal bands found");
            }
            return spotifyApi;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurred while fetching death metal bands", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error fetching death metal bands", e);
        }
    }

    //Search by band name
    @RequestMapping(path = "/searchByBandName", method = RequestMethod.GET)
    public SpotifyApi searchByBandName(@RequestParam String bandName) {
        try {
            SpotifyApi spotifyApi = spotifyService.searchByBandName(bandName);
            if (spotifyApi == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Band not found");
            }
            return spotifyApi;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurred while searching for band by name", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error searching for band by name", e);
        }
    }

    // Retrieve band details by Spotify Id
    @RequestMapping(path = "/{spotifyId}", method = RequestMethod.GET)
    public BandDetails getBandById(@PathVariable("spotifyId") String spotifyId) {
        try {
            BandDetails bandDetails = spotifyService.getBandById(spotifyId);
            if (bandDetails == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Band not found");
            }
            return bandDetails;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurred while fetching band details", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Band details not found", e);
        }
    }

    // Retrieve albums by band from Spotify Id
    @RequestMapping(path = "/{spotifyId}/albums", method = RequestMethod.GET)
    public AlbumResponse getAlbumsByBandId(@PathVariable("spotifyId") String spotifyId) {
        try {
            AlbumResponse albumResponse = spotifyService.getAlbumsByBandId(spotifyId);
            if (albumResponse == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Albums not found for this band");
            }
            return albumResponse;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurred while fetching albums for this band", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error fetching albums for this band", e);
        }
    }
}
