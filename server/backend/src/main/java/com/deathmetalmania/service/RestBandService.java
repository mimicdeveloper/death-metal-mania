package com.deathmetalmania.service;

import com.deathmetalmania.dao.BandDao;
import com.deathmetalmania.exception.InvalidDataException;
import com.deathmetalmania.exception.ServiceException;
import com.deathmetalmania.model.Band;
import com.deathmetalmania.model.dto.BandDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestBandService implements BandService {

    private final BandDao bandDao;
    private final SpotifyService spotifyService;

    public RestBandService(BandDao bandDao, SpotifyService spotifyService) {
        this.bandDao = bandDao;
        this.spotifyService = spotifyService;
    }

    @Override
    public List<Band> getAllBands() {
        try {
            return bandDao.getAllBands();
        } catch (Exception e) {
            throw new ServiceException("Error retrieving all bands", e);
        }
    }

    @Override
    public void addBand(BandDto bandDto) {
        try {
            // Validation logic
            if (bandDto.getName() == null || bandDto.getName().trim().isEmpty()) {
                throw new InvalidDataException("Band name cannot be empty");
            }
            if (bandDto.getCountry() == null || bandDto.getCountry().trim().isEmpty()) {
                throw new InvalidDataException("Band country cannot be empty");
            }
            if (bandDto.getGenre() == null || bandDto.getGenre().trim().isEmpty()) {
                throw new InvalidDataException("Band genre cannot be empty");
            }
            if (bandDto.getSpotify_id() == null || bandDto.getSpotify_id().trim().isEmpty()) {
                throw new InvalidDataException("Spotify ID cannot be empty");
            }

            // Map BandDto to Band entity
            Band band = new Band();
            band.setName(bandDto.getName());
            band.setGenre(bandDto.getGenre());
            band.setCountry(bandDto.getCountry());
            band.setSpotify_id(bandDto.getSpotify_id());

            // Use the entity, not the DTO, in DAO
            bandDao.add(band);

        } catch (InvalidDataException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Error adding the band", e);
        }
    }

    @Override
    public void updateBand(int id, BandDto bandDto) {
        try {
            // Map BandDto to Band entity
            Band band = new Band();
            band.setName(bandDto.getName());
            band.setGenre(bandDto.getGenre());
            band.setCountry(bandDto.getCountry());
            band.setSpotify_id(bandDto.getSpotify_id());

            // Pass the Band entity to the DAO method
            bandDao.updateBand(id, band);

        } catch (Exception e) {
            throw new ServiceException("Error updating the band with id: " + id, e);
        }
    }

    @Override
    public void deleteBand(int id) {
        try {
            bandDao.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException("Error deleting the band with id: " + id, e);
        }
    }
}
