package com.deathmetalmania.dao;

import com.deathmetalmania.model.Band;
import java.util.List;

public interface BandDao {
    /**
     * Get all bands available
     * @return list of bands, empty list if none
     */
    List<Band> getAllBands();

    /**
     * Adds a new band
     * @param band the band entity to add
     */
    void add(Band band);  // Changed from BandDto to Band entity

    /**
     * Update a band's details
     * @param id the ID of the band to update
     * @param band the updated band entity
     */
    void updateBand(int id, Band band);  // Changed from BandDto to Band entity

    /**
     * Delete a band by ID
     * @param id the ID of the band to delete
     */
    void deleteById(int id);
}
