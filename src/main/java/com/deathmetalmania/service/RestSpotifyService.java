package com.deathmetalmania.service;

import com.deathmetalmania.model.api.spotify.AlbumResponse;
import com.deathmetalmania.model.api.spotify.BandDetails;
import com.deathmetalmania.model.api.spotify.SpotifyApi;
import com.deathmetalmania.exception.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


@Service
public class RestSpotifyService implements SpotifyService {

    private final String BASE_URL = "https://api.spotify.com/v1/";
    private String accessToken;
    public RestSpotifyService(@Value("${SPOTIFY_ACCESS_TOKEN}") String token) {
        this.accessToken = token;
    }

    @Override
    public SpotifyApi searchByBandName(String bandName) {
        String url = BASE_URL + "search?q=" + bandName + "&type=artist&limit=1&access_token=" + accessToken;

        RestClient restClient = RestClient.create();
        SpotifyApi fullResults = restClient.get()
                .uri(url)
                .retrieve()
                .body(SpotifyApi.class);

        if (fullResults == null || fullResults.getArtists() == null || fullResults.getArtists().getItems().isEmpty()) {
            throw new ServiceException("No bands found matching: " + bandName);
        }

        return fullResults;
    }

    @Override
    public SpotifyApi searchDeathMetalBands() {
        String url = BASE_URL + "search?q=genre%253deathmetal&type=artist&limit=50";

        RestClient restClient = RestClient.create();
        SpotifyApi fullResults = restClient.get()
                .uri(url)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(SpotifyApi.class);

        if (fullResults == null || fullResults.getArtists() == null) {
            throw new ServiceException("No death metal bands found");
        }

        return fullResults;
    }


    @Override
    public BandDetails getBandById(String spotifyId) {
        String url = BASE_URL + "artists/" + spotifyId;

        RestClient restClient = RestClient.create();
        BandDetails bandDetails = restClient.get()
                .uri(url)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(BandDetails.class);

        if (bandDetails == null) {
            throw new ServiceException("Band not found for Spotify ID: " + spotifyId);
        }

        return bandDetails;
    }


    @Override
    public AlbumResponse getAlbumsByBandId(String spotifyId) {
        String url = BASE_URL + "artists/" + spotifyId + "/albums";

        RestClient restClient = RestClient.create();
        AlbumResponse albumsDetails = restClient.get()
                .uri(url)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(AlbumResponse.class);

        if (albumsDetails == null || albumsDetails.getItems().isEmpty()) {
            throw new ServiceException("Albums not found for band ID: " + spotifyId);
        }

        return albumsDetails;
    }
}
