package com.deathmetalmania.service;

import com.deathmetalmania.model.api.spotify.AlbumResponse;
import com.deathmetalmania.model.api.spotify.BandDetails;
import com.deathmetalmania.model.api.spotify.SpotifyApi;
import com.deathmetalmania.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestSpotifyService implements SpotifyService {

    private final String BASE_URL = "https://api.spotify.com/v1/";
    private final SpotifyTokenService spotifyTokenService;

    // Inject SpotifyTokenService via constructor
    public RestSpotifyService(SpotifyTokenService spotifyTokenService) {
        this.spotifyTokenService = spotifyTokenService;
    }

    // Helper method to get current token dynamically
    private String getAccessToken() {
        return spotifyTokenService.getAccessToken();
    }

    @Override
    public SpotifyApi searchByBandName(String bandName) {
        String url = BASE_URL + "search?q=" + bandName + "&type=artist&limit=3";

        RestClient restClient = RestClient.create();
        SpotifyApi fullResults = restClient.get()
                .uri(url)
                .header("Authorization", "Bearer " + getAccessToken())
                .retrieve()
                .body(SpotifyApi.class);

        if (fullResults == null || fullResults.getArtists() == null || fullResults.getArtists().getItems().isEmpty()) {
            throw new ServiceException("No bands found matching: " + bandName);
        }

        return fullResults;
    }

    @Override
    public SpotifyApi searchDeathMetalBands() {
        RestClient restClient = RestClient.builder()
                .baseUrl("https://api.spotify.com/v1")
                .build();

        List<SpotifyApi.Artist> allArtists = new ArrayList<>();
        int limit = 50;
        int maxPages = 2;
        String query = "genre:\"death metal\"";  // Correct query string (not double encoded)

        for (int page = 0; page < maxPages; page++) {
            int offset = page * limit;

            SpotifyApi partialResults = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search")
                            .queryParam("q", query)
                            .queryParam("type", "artist")
                            .queryParam("limit", limit)
                            .queryParam("offset", offset)
                            .build())
                    .header("Authorization", "Bearer " + getAccessToken())
                    .retrieve()
                    .body(SpotifyApi.class);

            if (partialResults == null || partialResults.getArtists() == null || partialResults.getArtists().getItems() == null) {
                break;
            }

            allArtists.addAll(partialResults.getArtists().getItems());

            if (partialResults.getArtists().getItems().size() < limit) {
                break;
            }
        }

        if (allArtists.isEmpty()) {
            throw new ServiceException("No artists found using fallback query.");
        }

        SpotifyApi.ArtistResponse response = new SpotifyApi.ArtistResponse();
        response.setItems(allArtists);
        SpotifyApi finalResult = new SpotifyApi();
        finalResult.setArtists(response);

        return finalResult;
    }


    @Override
    public BandDetails getBandById(String spotifyId) {
        String url = BASE_URL + "artists/" + spotifyId;

        RestClient restClient = RestClient.create();
        BandDetails bandDetails = restClient.get()
                .uri(url)
                .header("Authorization", "Bearer " + getAccessToken())
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
                .header("Authorization", "Bearer " + getAccessToken())
                .retrieve()
                .body(AlbumResponse.class);

        if (albumsDetails == null || albumsDetails.getItems().isEmpty()) {
            throw new ServiceException("Albums not found for band ID: " + spotifyId);
        }

        return albumsDetails;
    }
}
