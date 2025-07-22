package com.deathmetalmania.service;

import com.deathmetalmania.model.Favorite;
import com.deathmetalmania.model.dto.UserFavoriteDto;
import com.deathmetalmania.model.dto.FavoriteEventDto;  // Import your event DTO

import java.util.List;

public interface UserFavoriteService {

    // Existing band-related methods
    List<Favorite> getFavoritesByUserId(int userId);

    void addFavoriteByBandName(int userId, UserFavoriteDto userFavoriteDto);

    void deleteFavoriteByBandName(int userId, String bandName);

    // New event-related methods
    List<FavoriteEventDto> getFavoriteEventsByUserId(int userId);

    void addFavoriteEvent(int userId, FavoriteEventDto favoriteEventDto);

    void deleteFavoriteEventById(int userId, String eventId);
}
