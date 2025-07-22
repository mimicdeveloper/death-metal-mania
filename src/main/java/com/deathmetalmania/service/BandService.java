package com.deathmetalmania.service;

import com.deathmetalmania.model.Band;
import com.deathmetalmania.model.dto.BandDto;

import java.util.List;

public interface BandService {
    /**
     * Get all available bands
     * @return list of bands, empty list if none
     */
    List<Band> getAllBands();

    /**
     * Add a band by DTO
     * @param bandDto
     */
    void addBand(BandDto bandDto);

    /**
     * Update a band by id and dto
     * @param id
     * @param bandDto
     */
    void updateBand(int id, BandDto bandDto);

    /**
     * Delete a band by id
     * @param id
     */
    void deleteBand(int id);
}
