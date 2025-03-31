package com.deathmetalmania.service;

import com.deathmetalmania.dao.UserFavoriteDao;
import com.deathmetalmania.model.Favorite;
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
}
