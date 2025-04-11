package com.deathmetalmania.dao;

import com.deathmetalmania.model.Concert;
import com.deathmetalmania.exception.DaoException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcEventDao implements EventDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcEventDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Concert> getAllEvents() {
        String sql = "SELECT * FROM Events";
        List<Concert> concerts = new ArrayList<>();
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                concerts.add(mapRowToEvent(results));
            }
        } catch (Exception e) {
            throw new DaoException("Error fetching all events from the database.", e);
        }
        return concerts;
    }

    @Override
    public Concert getEventById(int id) {
        String sql = "SELECT event_id, band_id, name, dates, venue, city, country_code, min_price, max_price " +
                "FROM Events WHERE event_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                return mapRowToEvent(results);
            }
        } catch (Exception e) {
            throw new DaoException("Error fetching event by ID from the database.", e);
        }
        return null;
    }

    @Override
    public Concert createEvent(Concert concert) {
        // Update the SQL to include the 'info' column
        String sql = "INSERT INTO Events (band_id, name, dates, venue, city, country_code, min_price, max_price, info) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING event_id";  // Added info field to SQL

        try {
            // Retrieve the event_id after insertion, including the 'info' field
            Long eventId = jdbcTemplate.queryForObject(sql, Long.class,
                    concert.getBandId(),
                    concert.getName(),
                    java.sql.Date.valueOf(concert.getDate()),  // convert LocalDate to SQL Date
                    concert.getVenue(),
                    concert.getCity(),
                    concert.getCountryCode(),
                    concert.getMinPrice(),
                    concert.getMaxPrice(),
                    concert.getInfo());  // Include info field

            // Optionally, set the eventId in the Concert object (if you need to)
            concert.setEventId(eventId);  // Make sure Concert has setEventId and eventId fields

        } catch (Exception e) {
            throw new DaoException("Error creating event in the database.", e);
        }
        return concert;
    }


    @Override
    public void updateEvent(int id, Concert concert) {
        String sql = "UPDATE Events SET band_id = ?, name = ?, dates = ?, venue = ?, city = ?, country_code = ?, " +
                "min_price = ?, max_price = ? WHERE event_id = ?";
        try {
            jdbcTemplate.update(sql, concert.getBandId(), concert.getName(), concert.getDate(), concert.getVenue(),
                    concert.getCity(), concert.getCountryCode(), concert.getMinPrice(), concert.getMaxPrice(), id);
        } catch (Exception e) {
            throw new DaoException("Error updating event in the database.", e);
        }
    }

    @Override
    public void deleteEvent(int id) {
        String sql = "DELETE FROM Events WHERE event_id = ?";
        try {
            jdbcTemplate.update(sql, id);
        } catch (Exception e) {
            throw new DaoException("Error deleting event from the database.", e);
        }
    }

    private Concert mapRowToEvent(SqlRowSet rs) {
        Concert concert = new Concert();
        concert.setBandId(rs.getInt("band_id"));
        concert.setName(rs.getString("name"));
        concert.setDate(rs.getDate("dates").toLocalDate());
        concert.setVenue(rs.getString("venue"));
        concert.setCity(rs.getString("city"));
        concert.setCountryCode(rs.getString("country_code"));
        concert.setMinPrice(rs.getBigDecimal("min_price"));
        concert.setMaxPrice(rs.getBigDecimal("max_price"));
        concert.setEventId(rs.getLong("event_id"));  // Updated to camelCase
        return concert;
    }
}
