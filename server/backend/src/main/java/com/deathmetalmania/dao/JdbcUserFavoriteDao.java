package com.deathmetalmania.dao;

import com.deathmetalmania.exception.DaoException;
import com.deathmetalmania.model.Favorite;
import com.deathmetalmania.model.dto.FavoriteEventDto;
import com.deathmetalmania.model.dto.UserFavoriteDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.RowMapper;  // for RowMapper interface
import java.sql.ResultSet;                      // for ResultSet in mapRow
import java.sql.SQLException;                   // for SQLException in mapRow


import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUserFavoriteDao implements UserFavoriteDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserFavoriteDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Band favorites use User_favorites table
    @Override
    public void addFavoriteByBandName(int userId, UserFavoriteDto userFavoriteDto) {
        try {
            // Step 1: Try to get band_id from the band name
            String selectBandIdSql = "SELECT band_id FROM bands WHERE name = ?";
            Integer bandId = null;
            try {
                bandId = jdbcTemplate.queryForObject(selectBandIdSql, Integer.class, userFavoriteDto.getBandName());
            } catch (EmptyResultDataAccessException e) {
                // Band not found – insert it with default values
                String insertBandSql = "INSERT INTO bands (name, genre, country) VALUES (?, ?, ?) RETURNING band_id";
                bandId = jdbcTemplate.queryForObject(insertBandSql,
                        Integer.class,
                        userFavoriteDto.getBandName(),
                        "Death Metal",    // default genre
                        "Unknown");       // default country
            }

            // Step 2: Now insert into user_favorites using the bandId
            String insertFavoriteSql = "INSERT INTO user_favorites (user_id, band_id, band_name, rating) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(insertFavoriteSql,
                    userId,
                    bandId,
                    userFavoriteDto.getBandName(),
                    userFavoriteDto.getRating());

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database while adding favorite", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation while adding favorite", e);
        } catch (Exception e) {
            throw new DaoException("Error adding favorite band for user ID: " + userId, e);
        }
    }


    @Override
    public List<Favorite> getFavoritesByUserId(int userId) {
        String sql = "SELECT uf.user_id, uf.band_id, uf.rating, uf.band_name " +
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

    // Event favorites use User_favorite_events table
    @Override
    public void addFavoriteEvent(int userId, FavoriteEventDto event) {
        String sql = """
            INSERT INTO user_favorite_events
            (user_id, event_id, event_name, local_date, local_time, city, state, venue, url, info)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try {
            jdbcTemplate.update(sql,
                    userId,
                    event.getEventId(),
                    event.getName(),
                    event.getLocalDate(),
                    event.getLocalTime(),
                    event.getCity(),
                    event.getState(),
                    event.getVenue(),
                    event.getUrl(),
                    event.getInfo()
            );
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database while adding favorite event", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation while adding favorite event", e);
        }
    }

    @Override
    public void deleteFavoriteEventById(int userId, String eventId) {
        String sql = "DELETE FROM User_favorite_events WHERE user_id = ? AND event_id = ?";
        try {
            jdbcTemplate.update(sql, userId, eventId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database while deleting favorite event", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation while deleting favorite event", e);
        }
    }

    @Override
    public List<FavoriteEventDto> getFavoriteEventsByUserId(int userId) {
        String sql = """
        SELECT event_id, event_name, local_date, local_time, city, state, venue, url, info
        FROM User_favorite_events WHERE user_id = ?
    """;

        return jdbcTemplate.query(sql, new RowMapper<FavoriteEventDto>() {
            @Override
            public FavoriteEventDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                FavoriteEventDto dto = new FavoriteEventDto();
                dto.setEventId(rs.getString("event_id"));
                dto.setName(rs.getString("event_name"));
                dto.setLocalDate(rs.getString("local_date"));
                dto.setLocalTime(rs.getString("local_time"));
                dto.setCity(rs.getString("city"));
                dto.setState(rs.getString("state"));
                dto.setVenue(rs.getString("venue"));
                dto.setUrl(rs.getString("url"));
                dto.setInfo(rs.getString("info"));
                return dto;
            }
        }, userId);
    }


    private Favorite mapRowToFavorite(SqlRowSet favorite) {
        Favorite fav = new Favorite();
        fav.setUserId(favorite.getInt("user_id"));
        fav.setBandId(favorite.getInt("band_id"));
        fav.setBandName(favorite.getString("band_name"));
        fav.setRating(favorite.getInt("rating"));
        return fav;
    }
}
