package com.deathmetalmania.dao;

import com.deathmetalmania.exception.DaoException;
import com.deathmetalmania.model.Favorite;
import com.deathmetalmania.model.dto.UserFavoriteDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUserFavoriteDao implements UserFavoriteDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserFavoriteDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFavoriteByBandName(int userId, UserFavoriteDto userFavoriteDto) {
        String insertSql = "INSERT INTO User_favorites (user_id, band_id, band_name, rating) " +
                           "VALUES (?, ?, ?, ?)";
        try {
            jdbcTemplate.update(insertSql, userId, userFavoriteDto.getBandId(), userFavoriteDto.getBandName(), userFavoriteDto.getRating());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database while adding favorite", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation while adding favorite", e);
        }
    }

    @Override
    public List<Favorite> getFavoritesByUserId(int userId) {
        String sql = "SELECT uf.user_id, uf.band_id, uf.band_name, uf.rating " +
                     "FROM User_favorites uf " +
                     "WHERE uf.user_id = ?";

        List<Favorite> favorites = new ArrayList<>();
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            while (results.next()) {
                favorites.add(mapRowToFavorite(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database while retrieving favorites", e);
        }
        return favorites;
    }

    @Override
    public void deleteFavoriteByBandName(int userId, String bandName) {
        String sql = "DELETE FROM User_favorites " +
                     "WHERE user_id = ? AND band_name = ?";
        try {
            jdbcTemplate.update(sql, userId, bandName);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database while deleting favorite", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation while deleting favorite", e);
        }
    }

    private Favorite mapRowToFavorite(SqlRowSet favorite) {
        Favorite fav = new Favorite();
        fav.setUserId(favorite.getInt("user_id"));
        fav.setBandId(favorite.getInt("band_id"));
        fav.setBandName(favorite.getString("band_name")); // Retrieve the band name from the query
        fav.setRating(favorite.getInt("rating"));
        return fav;
    }
}
