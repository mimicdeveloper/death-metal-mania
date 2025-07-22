package com.deathmetalmania.service;

import com.deathmetalmania.dao.UserFavoriteDao;
import com.deathmetalmania.model.Favorite;
import com.deathmetalmania.model.dto.FavoriteEventDto;
import com.deathmetalmania.model.dto.UserFavoriteDto;
import com.deathmetalmania.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RestUserFavoriteService implements UserFavoriteService {

    private final UserFavoriteDao userFavoriteDao;

    public RestUserFavoriteService(UserFavoriteDao userFavoriteDao) {
        this.userFavoriteDao = userFavoriteDao;
    }

    @Override
    public List<Favorite> getFavoritesByUserId(int userId) {
        try {
            List<Favorite> favorites = userFavoriteDao.getFavoritesByUserId(userId);
            if (favorites == null || favorites.isEmpty()) {
                throw new ServiceException("No favorites found for user ID: " + userId);
            }
            return favorites;
        } catch (Exception e) {
            throw new ServiceException("Error retrieving favorites for user ID: " + userId, e);
        }
    }

    @Override
    public void addFavoriteByBandName(int userId, UserFavoriteDto userFavoriteDto) {
        try {
            userFavoriteDao.addFavoriteByBandName(userId, userFavoriteDto);
        } catch (Exception e) {
            throw new ServiceException("Error adding favorite band for user ID: " + userId, e);
        }
    }

    @Override
    public void deleteFavoriteByBandName(int userId, String bandName) {
        try {
            userFavoriteDao.deleteFavoriteByBandName(userId, bandName);
        } catch (Exception e) {
            throw new ServiceException("Error deleting favorite band " + bandName + " for user ID: " + userId, e);
        }
    }

    // New event-related methods

    @Override
    public List<FavoriteEventDto> getFavoriteEventsByUserId(int userId) {
        try {
            List<FavoriteEventDto> events = userFavoriteDao.getFavoriteEventsByUserId(userId);
            if (events == null || events.isEmpty()) {
                throw new ServiceException("No favorite events found for user ID: " + userId);
            }
            return events;
        } catch (Exception e) {
            throw new ServiceException("Error retrieving favorite events for user ID: " + userId, e);
        }
    }

    @Override
    public void addFavoriteEvent(int userId, FavoriteEventDto favoriteEventDto) {
        try {
            userFavoriteDao.addFavoriteEvent(userId, favoriteEventDto);
        } catch (Exception e) {
            throw new ServiceException("Error adding favorite event for user ID: " + userId, e);
        }
    }

    @Override
    public void deleteFavoriteEventById(int userId, String eventId) {
        try {
            userFavoriteDao.deleteFavoriteEventById(userId, eventId);
        } catch (Exception e) {
            throw new ServiceException("Error deleting favorite event " + eventId + " for user ID: " + userId, e);
        }
    }

}
