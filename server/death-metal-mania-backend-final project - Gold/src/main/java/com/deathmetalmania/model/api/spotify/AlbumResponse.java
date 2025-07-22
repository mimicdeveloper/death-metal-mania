package com.deathmetalmania.model.api.spotify;

import java.util.List;

public class AlbumResponse {
    private List<Album> items;

    public List<Album> getItems() {
        return items;
    }

    public void setItems(List<Album> items) {
        this.items = items;
    }
}
