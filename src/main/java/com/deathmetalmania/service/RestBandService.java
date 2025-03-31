package com.deathmetalmania.service;

import com.deathmetalmania.dao.BandDao;
import com.deathmetalmania.exception.InvalidDataException;
import com.deathmetalmania.exception.ServiceException;  // Import custom exception
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

            // Map BandDto to Band
            Band band = new Band();
            band.setBand_id(bandDto.getBand_id());
            band.setName(bandDto.getName());
            band.setGenre("Death Metal");
            band.setCountry(bandDto.getCountry());

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
            bandDao.updateBand(id, bandDto);
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
