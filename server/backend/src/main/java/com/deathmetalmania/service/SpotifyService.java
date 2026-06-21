package com.deathmetalmania.service;

import com.deathmetalmania.model.api.spotify.AlbumResponse;
import com.deathmetalmania.model.api.spotify.BandDetails;
import com.deathmetalmania.model.api.spotify.SpotifyApi;


public interface SpotifyService {
    SpotifyApi searchDeathMetalBands();
    SpotifyApi searchBySubgenre(String genreKeyword, String[] allowTerms, String[] blockTerms);
    SpotifyApi searchByBandName(String bandName);
    BandDetails getBandById(String spotifyId);
    AlbumResponse getAlbumsByBandId(String spotifyId);
}
