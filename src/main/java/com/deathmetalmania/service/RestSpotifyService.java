package com.deathmetalmania.service;

import com.deathmetalmania.model.api.spotify.AlbumResponse;
import com.deathmetalmania.model.api.spotify.BandDetails;
import com.deathmetalmania.model.api.spotify.SpotifyApi;
import com.deathmetalmania.exception.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;


@Service
public class RestSpotifyService implements SpotifyService {

    private final String BASE_URL = "https://api.spotify.com/v1/";
    private String accessToken;
    public RestSpotifyService(@Value("${SPOTIFY_ACCESS_TOKEN}") String token) {
        this.accessToken = token;
    }

    @Override
    public SpotifyApi searchByBandName(String bandName) {
        String url = BASE_URL + "search?q=" + bandName + "&type=artist&limit=3&access_token=" + accessToken;

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
        String url = "https://api.spotify.com/v1/search";
        RestClient restClient = RestClient.builder().baseUrl(url).build();

        List<SpotifyApi.Artist> allValidArtists = new ArrayList<>();

        int limit = 50;
        int totalFetched = 0;
        int maxPages = 8; // Adjust this as needed to control how many pages you want

        for (int page = 0; page < maxPages; page++) {
            int offset = page * limit;

            SpotifyApi partialResults = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("q", "genre:\"death metal\"")
                            .queryParam("type", "artist")
                            .queryParam("limit", String.valueOf(limit))
                            .queryParam("offset", String.valueOf(offset))
                            .build()
                    )
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .body(SpotifyApi.class);

            if (partialResults == null || partialResults.getArtists() == null || partialResults.getArtists().getItems() == null) {
                break; // Stop if no more results
            }

            for (SpotifyApi.Artist artist : partialResults.getArtists().getItems()) {
                if (artist.getGenres() != null) {
                    String genresLower = artist.getGenres().toString().toLowerCase();
                    String artistName = artist.getName().toLowerCase();
                    String artistId = artist.getId();

                    if (!genresLower.contains("deathcore") &&
                            !genresLower.contains("thrash metal") &&
                            !genresLower.contains("black metal") &&
                            !genresLower.contains("melodic death metal") &&
                            !genresLower.contains("emo") &&
                            !artistName.equals("i see stars") &&
                            !artistId.equals("3iCJOi5YKh247eutgCyLFe")) {
                        allValidArtists.add(artist);
                    }
                }
            }






            totalFetched += partialResults.getArtists().getItems().size();

            // Optional: Break early if fewer than 50 returned (no more pages)
            if (partialResults.getArtists().getItems().size() < limit) {
                break;
            }
        }

        if (allValidArtists.isEmpty()) {
            throw new ServiceException("No valid death metal bands found (deathcore filtered out)");
        }

        // Wrap results in a SpotifyApi response
        SpotifyApi.ArtistResponse response = new SpotifyApi.ArtistResponse();
        response.setItems(allValidArtists);
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
