package com.deathmetalmania.service;

import com.deathmetalmania.model.api.spotify.AlbumResponse;
import com.deathmetalmania.model.api.spotify.BandDetails;
import com.deathmetalmania.model.api.spotify.SpotifyApi;


public interface SpotifyService {
    /**
     * Search for death metal bands via Spotify Api
     * @return list of death metal bands
     */
    SpotifyApi searchDeathMetalBands();

    /**
     * Search for band ID by bandname via Spotify Api
     * @param bandName
     * @return
     */
    SpotifyApi searchByBandName(String bandName);

    /**
     * Get band details by Spotify ID
     * @param spotifyId
     * @return
     */
    BandDetails getBandById(String spotifyId);

    /**
     * Get Band's albums by Spotify ID
     * @param spotifyId
     * @return
     */
    AlbumResponse getAlbumsByBandId(String spotifyId);
}
