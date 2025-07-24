package com.deathmetalmania.service;

import com.deathmetalmania.exception.ServiceException;
import com.deathmetalmania.model.api.spotify.AlbumResponse;
import com.deathmetalmania.model.api.spotify.BandDetails;
import com.deathmetalmania.model.api.spotify.SpotifyApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestSpotifyService implements SpotifyService {

    private static final Logger logger = LoggerFactory.getLogger(RestSpotifyService.class);
    private final String BASE_URL = "https://api.spotify.com/v1/";
    private final SpotifyTokenService spotifyTokenService;

    public RestSpotifyService(SpotifyTokenService spotifyTokenService) {
        this.spotifyTokenService = spotifyTokenService;
    }

    private String getAccessToken() {
        return spotifyTokenService.getAccessToken();
    }

    @Override
    public SpotifyApi searchByBandName(String bandName) {
        String url = BASE_URL + "search?q=" + bandName + "&type=artist&limit=3";

        logger.info("Searching for band name: {}", bandName);
        logger.debug("Outgoing URL: {}", url);

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
        String baseUrl = "https://api.spotify.com/v1/search";
        RestClient restClient = RestClient.builder().baseUrl(baseUrl).build();

        List<SpotifyApi.Artist> allArtists = new ArrayList<>();
        int limit = 50;
        int maxPages = 2;
        String encodedQuery = "genre%3A%22death%20metal%22";
        String decodedQuery = URLDecoder.decode(encodedQuery, StandardCharsets.UTF_8);

        logger.info("Starting search for: {}", decodedQuery);

        for (int page = 0; page < maxPages; page++) {
            int offset = page * limit;

            logger.debug("Requesting page {} with offset {} and limit {}", page + 1, offset, limit);

            SpotifyApi partialResults = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("q", encodedQuery)
                            .queryParam("type", "artist")
                            .queryParam("limit", String.valueOf(limit))
                            .queryParam("offset", String.valueOf(offset))
                            .build()
                    )
                    .header("Authorization", "Bearer " + getAccessToken())
                    .retrieve()
                    .body(SpotifyApi.class);

            if (partialResults == null || partialResults.getArtists() == null || partialResults.getArtists().getItems() == null) {
                logger.warn("No results returned for page {}", page + 1);
                break;
            }

            logger.info("Retrieved {} artists on page {}", partialResults.getArtists().getItems().size(), page + 1);
            allArtists.addAll(partialResults.getArtists().getItems());

            if (partialResults.getArtists().getItems().size() < limit) {
                break;
            }
        }

        if (allArtists.isEmpty()) {
            throw new ServiceException("No artists found using fallback query.");
        }

        List<SpotifyApi.Artist> filteredArtists = allArtists.stream()
                .filter(artist -> {
                    String artistName = artist.getName() != null ? artist.getName().toLowerCase() : "";
                    boolean isBannedBand = artistName.equals("chon") || artistName.equals("heavy//hitter");
                    boolean hasBlockedGenre = artist.getGenres() != null && artist.getGenres().stream().anyMatch(
                            genre -> {
                                String lowerGenre = genre.toLowerCase();
                                return lowerGenre.contains("black metal") ||
                                        lowerGenre.contains("metalcore") ||
                                        lowerGenre.contains("nu metal");
                            }
                    );
                    return !isBannedBand && !hasBlockedGenre;
                })
                .collect(Collectors.toList());

        logger.info("Filtered artists down to {} after genre and name filtering", filteredArtists.size());

        if (filteredArtists.isEmpty()) {
            throw new ServiceException("No suitable death metal artists found after filtering.");
        }

        SpotifyApi.ArtistResponse response = new SpotifyApi.ArtistResponse();
        response.setItems(filteredArtists);
        SpotifyApi finalResult = new SpotifyApi();
        finalResult.setArtists(response);

        return finalResult;
    }

    @Override
    public BandDetails getBandById(String spotifyId) {
        String url = BASE_URL + "artists/" + spotifyId;
        logger.info("Fetching band details for ID: {}", spotifyId);

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
        logger.info("Fetching albums for band ID: {}", spotifyId);

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
