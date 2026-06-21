package com.deathmetalmania.model.api.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class SpotifyApi {

    @JsonProperty("artists")
    private ArtistResponse artists;

    public ArtistResponse getArtists() { return artists; }
    public void setArtists(ArtistResponse artists) { this.artists = artists; }

    public static class ArtistResponse {

        @JsonProperty("items")
        private List<Artist> items;

        public List<Artist> getItems() { return items; }
        public void setItems(List<Artist> items) { this.items = items; }
    }

    public static class Artist {

        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("popularity")
        private int popularity;

        @JsonProperty("genres")
        private List<String> genres;

        @JsonProperty("images")
        private List<Image> images;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public int getPopularity() { return popularity; }
        public void setPopularity(int popularity) { this.popularity = popularity; }

        public List<String> getGenres() { return genres; }
        public void setGenres(List<String> genres) { this.genres = genres; }

        public List<Image> getImages() { return images; }
        public void setImages(List<Image> images) { this.images = images; }
    }

    public static class Image {

        @JsonProperty("url")
        private String url;

        @JsonProperty("height")
        private Integer height;

        @JsonProperty("width")
        private Integer width;

        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }

        public Integer getHeight() { return height; }
        public void setHeight(Integer height) { this.height = height; }

        public Integer getWidth() { return width; }
        public void setWidth(Integer width) { this.width = width; }
    }

}
