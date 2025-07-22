package com.deathmetalmania.service;

import com.deathmetalmania.model.Favorite;
import com.deathmetalmania.model.dto.UserFavoriteDto;

import java.util.List;

public interface UserFavoriteService {

    /**
     * Get all available favorite bands by user ID
     * @param userId
     * @return list of favorite bands, empty list if none
     */
    List<Favorite> getFavoritesByUserId(int userId);

    /**
     * Add favorite band by user ID and dto
     * @param userId
     * @param userFavoriteDto
     */
    void addFavoriteByBandName(int userId, UserFavoriteDto userFavoriteDto);

    /**
     * Delete favorite band by bandname
     * @param userId
     * @param bandName
     */
    void deleteFavoriteByBandName(int userId, String bandName);
    }


