package com.deathmetalmania.dao;

import com.deathmetalmania.model.Band;
import com.deathmetalmania.model.dto.BandDto;
import java.util.List;


public interface BandDao {
    /**
     * Get all bands available
     * @return list of bands, empty list if none
     */
    List<Band> getAllBands();

    /**
     *
     * @param band
     * Adds a new band
     */
    void add(Band band);

    /**
     * Update a band's details
     * @param id
     * @param bandDto
     */
    void updateBand(int id, BandDto bandDto);

    /**
     * Delete a band by ID
     * @param id
     */
    void deleteById(int id);
}