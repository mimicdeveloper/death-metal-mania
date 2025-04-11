package com.deathmetalmania.dao;

import com.deathmetalmania.exception.DaoException;
import com.deathmetalmania.model.Band;
import com.deathmetalmania.model.dto.BandDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcBandDao implements BandDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcBandDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Band> getAllBands() {
        String sql = "SELECT * FROM Bands";
        List<Band> bands = new ArrayList<>();
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                bands.add(mapRowToBand(results));
            }
        } catch (Exception e) {
            throw new DaoException("Error fetching all bands from the database.", e);
        }
        return bands;
    }

    @Override
    public void add(Band band) {
        String sql = "INSERT INTO bands (spotify_id, name, genre, country) VALUES (?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql,
                    band.getSpotify_id(),
                    band.getName(),
                    band.getGenre(),
                    band.getCountry()
            );
        } catch (Exception e) {
            throw new DaoException("Error adding band to the database.", e);
        }
    }

    @Override
    public void updateBand(int id, BandDto bandDto) {
        String sql = "UPDATE bands SET name = ?, genre = ?, country = ? " +
                     "WHERE band_id = ?";
        try {
            jdbcTemplate.update(sql, bandDto.getName(), bandDto.getGenre(), bandDto.getCountry(), id);
        } catch (Exception e) {
            throw new DaoException("Error updating band in the database.", e);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM bands WHERE band_id = ?";
        try {
            jdbcTemplate.update(sql, id);
        } catch (Exception e) {
            throw new DaoException("Error deleting band from the database.", e);
        }
    }

    private Band mapRowToBand(SqlRowSet rs) {
        Band band = new Band();
        band.setBand_id(rs.getInt("band_id"));
        band.setSpotify_id(rs.getString("spotify_id"));
        band.setName(rs.getString("name"));
        band.setGenre(rs.getString("genre"));
        band.setCountry(rs.getString("country"));
        return band;
    }
}
