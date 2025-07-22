package com.deathmetalmania.dao;

import com.deathmetalmania.model.Favorite;
import com.deathmetalmania.model.dto.UserFavoriteDto;
import java.util.List;

public interface UserFavoriteDao {
    /**
     * Add favorite band by Bandname
     * @param userId
     * @param userFavoriteDto
     */
    void addFavoriteByBandName(int userId, UserFavoriteDto userFavoriteDto);

    /**
     * Get all available favorite bands
     * @param userId
     * @return list of favorite bands, empty list if none
     */
    List<Favorite> getFavoritesByUserId(int userId);

    /**
     * Delete favorite band by bandname
     * @param userId
     * @param bandName
     */
    void deleteFavoriteByBandName(int userId, String bandName);
}
